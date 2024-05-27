package com.sparta.spartatodoproject.filter;

import java.io.IOException;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import com.sparta.spartatodoproject.entity.User;
import com.sparta.spartatodoproject.jwt.JwtUtil;
import com.sparta.spartatodoproject.repository.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "AuthFilter")
@Component
@Order(1)
public class AuthFilter implements Filter {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	public AuthFilter(UserRepository userRepository, JwtUtil jwtUtil) {
		this.userRepository = userRepository;
		this.jwtUtil = jwtUtil;
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
		FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		String url = httpServletRequest.getRequestURI();

		if (StringUtils.hasText(url) &&
			(url.startsWith("/user") || url.startsWith("/css") || url.startsWith("/js")
				|| url.startsWith("/swagger-ui.html/*") || url.startsWith("/swagger-ui/index.html"))
		) {

			log.info("인증처리 하지 않는 url" + url);

			// 회원가입, 로그인 관련 API 는 인증 필요없이 요청 진행
			chain.doFilter(request, response); // 다음 Filter 로 이동
		} else {
			log.info("인증처리 url" + url);

			// 나머지 API 요청은 인증 처리 진행
			// 토큰 확인
			String accessToken = jwtUtil.getTokenFromRequest(
				jwtUtil.AUTHORIZATION_HEADER, httpServletRequest);

			String refreshToken = jwtUtil.getTokenFromRequest(
				"Refresh-Token", httpServletRequest);

			if (StringUtils.hasText(accessToken) && StringUtils.hasText(refreshToken)) { // 토큰이 존재하면 검증 시작
				if (jwtUtil.validateToken(refreshToken)) {
					// 토큰 검증

					try {            // 토큰에서 사용자 정보 가져오기
						Claims info = jwtUtil.getUserInfoFromToken(accessToken);

						User user = userRepository.findByUsername(info.getSubject()).orElseThrow(() ->
							new NullPointerException("Not Found User")
						);

						log.info("{} 로그인 성공 ", user.getUsername());
					} catch (ExpiredJwtException e) {
						httpServletResponse.sendRedirect("/user/refresh");
					}
					chain.doFilter(request, response);

				}

				// 다음 Filter 로 이동
			} else {
				throw new IllegalArgumentException("Not Found Token");
			}
		}
	}

}