package com.sparta.spartatodoproject.jwt;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sparta.spartatodoproject.entity.UserRoleEnum;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtUtil {
	// Header KEY 값(쿠키의 이름 값)
	public static final String AUTHORIZATION_HEADER = "Authorization";
	// 사용자 권한 값의 KEY(권한을 구분하기 위한 값)
	public static final String AUTHORIZATION_KEY = "auth";
	// Token 식별자 ('Bearer '가 붙어있으면 토큰이라는 형식적인 규칙)
	public static final String BEARER_PREFIX = "Bearer ";
	// 토큰 만료시간
	private final long TOKEN_TIME = 60 * 60 * 1000L; // 60분

	@Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
	private String secretKey;
	private Key key;
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

	// 로그 설정
	public static final Logger logger = LoggerFactory.getLogger("JWT 관련 로그");

	//생성자가 만들어지고난 후 실행
	//Base64로 인코더 데어있는 비밀키를 디코더 시킨 후 키에다가 담는다
	@PostConstruct
	public void init() {
		byte[] bytes = Base64.getDecoder().decode(secretKey);
		key = Keys.hmacShaKeyFor(bytes);
	}

	//쿠키를 반환하는 방법
	// 1. 헤더에 담기 - 코드가 짮다.
	// 2. 쿠키생성 후 쿠키에 담기 - 만료기한 설정, 추가 정보 담을 수 있음 <-
	// 토큰 생성
	public String createToken(String username, UserRoleEnum role) {
		Date date = new Date(); //날짜 관련 클래스

		return BEARER_PREFIX +
			Jwts.builder()
				.setSubject(username) // 사용자 식별자값(ID)
				.claim(AUTHORIZATION_KEY, role) // 사용자 권한
				.setExpiration(new Date(date.getTime() + TOKEN_TIME)) // 만료 시간
				.setIssuedAt(date) // 발급일
				.signWith(key, signatureAlgorithm) // 암호화 알고리즘
				.compact();
	}

	// JWT Cookie 에 저장
	public void addJwtToCookie(String token, HttpServletResponse res) {
		try {
			// Cookie Value 에는 공백이 불가능해서 encoding 진행
			token = URLEncoder.encode(token, "utf-8")
				.replaceAll("\\+", "%20");

			Cookie cookie = new Cookie(AUTHORIZATION_HEADER, token); // Name-Value
			cookie.setPath("/");

			// Response 객체에 Cookie 추가
			res.addCookie(cookie);
		} catch (UnsupportedEncodingException e) {
			logger.error(e.getMessage());
		}
	}

	// JWT 토큰 substring
	public String substringToken(String tokenValue) {
		if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
			return tokenValue.substring(7);
		}
		logger.error("Not Found Token");
		throw new NullPointerException("Not Found Token");
	}

	// 토큰 검증
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException | SignatureException e) {
			logger.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
		} catch (ExpiredJwtException e) {
			logger.error("Expired JWT token, 만료된 JWT token 입니다.");
		} catch (UnsupportedJwtException e) {
			logger.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
		} catch (IllegalArgumentException e) {
			logger.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
		}
		return false;
	}

	// 토큰에서 사용자 정보 가져오기
	public Claims getUserInfoFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build()
			.parseClaimsJws(token).getBody();
	}

	// HttpServletRequest 에서 Cookie Value : JWT 가져오기
	public String getTokenFromRequest(HttpServletRequest req) {
		Cookie[] cookies = req.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals(AUTHORIZATION_HEADER)) {
					try {
						return URLDecoder.decode(cookie.getValue(), "UTF-8"); // Encode 되어 넘어간 Value 다시 Decode
					} catch (UnsupportedEncodingException e) {
						return null;
					}
				}
			}
		}
		return null;
	}
}