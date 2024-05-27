package com.sparta.spartatodoproject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartatodoproject.CommonResponse;
import com.sparta.spartatodoproject.dto.LoginRequestDto;
import com.sparta.spartatodoproject.dto.UserRequestDto;
import com.sparta.spartatodoproject.dto.UserResponseDto;
import com.sparta.spartatodoproject.entity.UserRoleEnum;
import com.sparta.spartatodoproject.jwt.JwtUtil;
import com.sparta.spartatodoproject.service.UserService;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.headers.Header;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	private final UserService userService;
	private final JwtUtil jwtUtil;

	@PostMapping("/signup")
	public ResponseEntity<CommonResponse<UserResponseDto>> addUser(
		@Valid @RequestBody UserRequestDto requestDto) {
		UserResponseDto userResponseDto = userService.signup(requestDto);
		return ResponseEntity.ok().body(CommonResponse.<UserResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("회원가입 완료")
			.data(userResponseDto)
			.build());
	}

	@PostMapping("/login")
	public ResponseEntity<CommonResponse<UserResponseDto>> login(
		@Valid @RequestBody LoginRequestDto requestDto) {
		UserResponseDto responseDto = userService.login(requestDto);
		return ResponseEntity.ok().body(CommonResponse.<UserResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("로그인 성공")
			.data(responseDto)
			.build());
	}

	@GetMapping("/create-jwt")
	public String createJwt(HttpServletResponse res) {
		// Jwt 생성
		String token = jwtUtil.createToken("Robbie", UserRoleEnum.USER);

		// Jwt 쿠키 저장
		jwtUtil.addJwtToHeader(token, res);

		return "createJwt : " + token;
	}

	@GetMapping("/get-jwt")
	public String getJwt(@RequestHeader(JwtUtil.AUTHORIZATION_HEADER) String value) {
		// JWT 토큰 substring
		String token = jwtUtil.substringToken(value);
		log.info("getJwt : " + token);

		// 토큰 검증
		if (!jwtUtil.validateToken(token))
			throw new IllegalArgumentException("Token Error");

		// 토큰에서 사용자 정보 가져오기
		Claims info = jwtUtil.getUserInfoFromToken(token);
		// 사용자 username
		String username = info.getSubject();
		System.out.println("username = " + username);
		// 사용자 권한
		String authority = (String)info.get(JwtUtil.AUTHORIZATION_KEY);
		System.out.println("authority = " + authority);

		return "getJwt : " + username + ", " + authority;
	}
}
