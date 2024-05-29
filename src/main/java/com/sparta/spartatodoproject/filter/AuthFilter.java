package com.sparta.spartatodoproject.filter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

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
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "AuthFilter")
@Component
@Order(1)
@RequiredArgsConstructor
public class AuthFilter implements Filter {

	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	private List<String> excludedUrls;
	// private List<String> restrictedUrls;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 필터 초기화 시 예외 처리 URL 목록을 설정합니다.
		excludedUrls = Arrays.asList("/user", "/css", "/js", "/swagger-ui.html/**"
			, "/swagger-ui/index.html"); // 예외 URL을 추가합니다.
	}

	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
		FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpServletRequest = (HttpServletRequest)request;
		HttpServletResponse httpServletResponse = (HttpServletResponse)response;
		String url = httpServletRequest.getRequestURI();
		String method = httpServletRequest.getMethod();

		//예외 url 아니면 검증
		if (excludedUrls.stream().anyMatch(url::startsWith)) {
			log.info("통과 {} {} ", method, url);

			// 예외 URL에 해당하면 필터를 건너뛰고 다음 필터나 서블릿으로 요청을 전달합니다.
			chain.doFilter(request, response);
		} else {
			log.info("인증처리 {} {} ", method, url);

			// 요청이 제한된 URL에 대한 GET 요청이 아닌 경우 확인합니다.
			if ("GET".equalsIgnoreCase(method)) {
				// 제한된 URL에 대한 GET 요청이 아니면 405 Method Not Allowed 응답을 반환합니다.
				// httpResponse.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED, "Only GET method is allowed for this URL.");
				log.info("get 요청은 모두 통과");
				chain.doFilter(request, response);

				return;
			}

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