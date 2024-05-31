package com.sparta.spartatodoproject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartatodoproject.CommonResponse;
import com.sparta.spartatodoproject.dto.LoginRequestDto;
import com.sparta.spartatodoproject.dto.UserRequestDto;
import com.sparta.spartatodoproject.dto.UserResponseDto;
import com.sparta.spartatodoproject.entity.User;
import com.sparta.spartatodoproject.entity.UserRoleEnum;
import com.sparta.spartatodoproject.jwt.JwtService;
import com.sparta.spartatodoproject.jwt.JwtUtil;
import com.sparta.spartatodoproject.service.TodoService;
import com.sparta.spartatodoproject.service.UserService;

import io.jsonwebtoken.Claims;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {
	private static final Logger log = LoggerFactory.getLogger(UserController.class);
	private final UserService userService;
	private final JwtUtil jwtUtil;
	private final JwtService jwtService;
	private final TodoService todoService;

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
		@RequestBody LoginRequestDto requestDto) {
		User user = userService.login(requestDto);
		String accessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole());

		String refreshToken = jwtUtil.createRefreshTaken();

		log.info("access token: {}", accessToken);
		log.info("refresh token: {}", refreshToken);

		jwtService.saveRefreshToken(refreshToken, user);

		return ResponseEntity.ok()
			.header(HttpHeaders.AUTHORIZATION, accessToken)
			.header("Refresh-Token", refreshToken)
			.body(CommonResponse.<UserResponseDto>builder()
				.statusCode(HttpStatus.OK.value())
				.message("로그인 성공")
				.data(new UserResponseDto(user))
				.build());
	}

	@GetMapping("/refresh")
	public ResponseEntity<CommonResponse<String>> refreshToken(
		@RequestHeader(JwtUtil.AUTHORIZATION_HEADER) String accessToken,
		@RequestHeader("Refresh-Token") String refreshToken
	) {
		Claims claims = jwtUtil.getClaimsFromExpiredToken(accessToken);
		String role = claims.get("auth", String.class);
		UserRoleEnum roleEnum = UserRoleEnum.valueOf(role);
		String username = claims.getSubject();
		log.info("role  : " + role);

		String newAccessToken = jwtUtil.createAccessToken(username, roleEnum);

		return ResponseEntity.ok()
			.header(HttpHeaders.AUTHORIZATION, newAccessToken)
			.header("Refresh-Token", refreshToken)
			.body(CommonResponse.<String>builder()
				.statusCode(HttpStatus.OK.value())
				.message("토큰 재발급 완료")
				.data(newAccessToken + "     <-  access token 변경하세요")
				.build());

	}

	@DeleteMapping("/{id}")
	public ResponseEntity<CommonResponse> deleteUser(
		@PathVariable int id, @RequestHeader(JwtUtil.AUTHORIZATION_HEADER) String token
	) {
		userService.delete(id, token);
		return ResponseEntity.ok().body(
			CommonResponse.builder()
				.statusCode(HttpStatus.OK.value())
				.message("삭제 완료")
				.build());
	}
}
