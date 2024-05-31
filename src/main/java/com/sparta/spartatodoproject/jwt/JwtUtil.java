package com.sparta.spartatodoproject.jwt;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.security.Key;
import java.util.Base64;
import java.util.Date;

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
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {
	// Header KEY 값(쿠키의 이름 값)
	public static final String AUTHORIZATION_HEADER = "Authorization";
	// 사용자 권한 값의 KEY(권한을 구분하기 위한 값)
	public static final String AUTHORIZATION_KEY = "auth";
	// Token 식별자 ('Bearer '가 붙어있으면 토큰이라는 형식적인 규칙)
	public static final String BEARER_PREFIX = "Bearer ";
	// 토큰 만료시간
	private final long ACCESS_TOKEN_TIME = 60 * 60 * 1000L; // 60분
	private final long REFRESH_TOKEN_TIME = 10 * 24 * 60 * 60 * 1000L; // 10일

	@Value("${jwt.secret.key}") // Base64 Encode 한 SecretKey
	private String secretKey;
	private Key key;
	private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

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
	public String createAccessToken(String username, UserRoleEnum role) {
		Date date = new Date(); //날짜 관련 클래스

		return BEARER_PREFIX +
			Jwts.builder()
				.setSubject(username) // 사용자 식별자값(ID)
				.claim(AUTHORIZATION_KEY, role) // 사용자 권한
				.setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME)) // 만료 시간
				.setIssuedAt(date) // 발급일
				.signWith(key, signatureAlgorithm) // 암호화 알고리즘
				.compact();
	}

	public String createRefreshTaken() {
		Date date = new Date(); //날짜 관련 클래스

		return BEARER_PREFIX +
			Jwts.builder()
				.setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME)) // 만료 시간
				.setIssuedAt(date) // 발급일
				.signWith(key, signatureAlgorithm) // 암호화 알고리즘
				.compact();
	}

	// JWT Cookie 에 저장
	public void addJwtToHeader(String token, HttpServletResponse res) {
		try {
			// 공백이 불가능해서 encoding 진행
			token = tokenEncode(token);

			res.addHeader(AUTHORIZATION_HEADER, BEARER_PREFIX + token);

		} catch (UnsupportedEncodingException e) {
			log.error(e.getMessage());
		}
	}

	public static String tokenEncode(String token) throws UnsupportedEncodingException {
		token = URLEncoder.encode(token, "utf-8")
			.replaceAll("\\+", "%20");
		return token;
	}

	// JWT 토큰 substring
	public String substringToken(String tokenValue) {
		if (StringUtils.hasText(tokenValue) && tokenValue.startsWith(BEARER_PREFIX)) {
			return tokenValue.substring(7);
		}
		log.error("Not Found Token");
		throw new NullPointerException("Not Found Token");
	}

	// 토큰 검증
	public boolean validateToken(String token) {
		try {
			Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
			return true;
		} catch (SecurityException | MalformedJwtException | SignatureException e) {
			log.error("Invalid JWT signature, 유효하지 않는 JWT 서명 입니다.");
			// } catch (ExpiredJwtException e) {
			// 	log.error("Expired JWT token, 만료된 JWT token 입니다.");
		} catch (UnsupportedJwtException e) {
			log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰 입니다.");
		} catch (IllegalArgumentException e) {
			log.error("JWT claims is empty, 잘못된 JWT 토큰 입니다.");
		}
		return false;
	}

	// 토큰에서 사용자 정보 가져오기
	public Claims getUserInfoFromToken(String token) {
		return Jwts.parserBuilder().setSigningKey(key).build()
			.parseClaimsJws(token).getBody();
	}

	// HttpServletRequest : JWT 가져오기
	public String getTokenFromRequest(String headerName, HttpServletRequest req) {

		if (req.getHeader(headerName) != null)
			return substringToken(req.getHeader(headerName));

		return null;
	}

	public Claims getClaimsFromExpiredToken(String token) {
		try {
			return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(substringToken((token))).getBody();
		} catch (ExpiredJwtException e) {
			return e.getClaims();
		}
	}

}