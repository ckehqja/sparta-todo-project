package com.sparta.spartatodoproject.controller;

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
import com.sparta.spartatodoproject.jwt.JwtUtil;
import com.sparta.spartatodoproject.service.RefreshTokenService;
import com.sparta.spartatodoproject.service.UserService;

import io.jsonwebtoken.Claims;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "UserController", description = "유저 관리 api")
@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {

	private final UserService userService;
	private final RefreshTokenService refreshTokenService;
	private final JwtUtil jwtUtil;

	@Operation(description = "유저 등록")
	@Parameter(description = "@RequestBody 유저 dto ")
	@PostMapping("/signup")
	public ResponseEntity<CommonResponse<UserResponseDto>> addUser(
		@Valid @RequestBody UserRequestDto requestDto) {

		UserResponseDto userResponseDto = userService.signup(requestDto);
		return ResponseEntity.ok().body(CommonResponse.<UserResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("유저 등록")
			.data(userResponseDto)
			.build());
	}

	@Operation(description = "로그인 + access & refresh token 발급")
	@Parameter(description = "@RequestBody 로그인 dto")
	@PostMapping("/login")
	public ResponseEntity<CommonResponse<UserResponseDto>> login(
		@RequestBody LoginRequestDto requestDto) {

		User user = userService.login(requestDto);

		String accessToken = jwtUtil.createAccessToken(user.getUsername(), user.getRole());
		String refreshToken = jwtUtil.createRefreshTaken();

		log.info("access token: {}", accessToken);
		log.info("refresh token: {}", refreshToken);

		refreshTokenService.saveRefreshToken(refreshToken, user);

		return ResponseEntity.ok()
			.header(HttpHeaders.AUTHORIZATION, accessToken)
			.header("Refresh-Token", refreshToken)
			.body(CommonResponse.<UserResponseDto>builder()
				.statusCode(HttpStatus.OK.value())
				.message("로그인 성공")
				.data(new UserResponseDto(user))
				.build());
	}

	@Operation(description = "리프레쉬 토큰 확인 후 어세스 토큰 재발급")
	@Parameter(description = "@RequestHeader accessToken, @RequestHeader refreshToken")
	@GetMapping("/refresh")
	public ResponseEntity<CommonResponse<String>> refreshToken(
		@RequestHeader(JwtUtil.AUTHORIZATION_HEADER) String accessToken,
		@RequestHeader(JwtUtil.REFRESH_TOKEN) String refreshToken) {

		Claims claims = jwtUtil.getClaimsFromExpiredToken(accessToken);
		String role = claims.get(JwtUtil.AUTHORIZATION_KEY, String.class);
		UserRoleEnum roleEnum = UserRoleEnum.valueOf(role);
		String username = claims.getSubject();

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

	@Operation(description = "유저 삭제")
	@Parameter(description = "@PathVariable 유저 id, @RequestHeader 토큰")
	@DeleteMapping("/{id}")
	public ResponseEntity<CommonResponse> deleteUser(
		@PathVariable int id, @RequestHeader(JwtUtil.AUTHORIZATION_HEADER) String token) {

		String username = refreshTokenService.tokenUsername(token);
		userService.delete(id, username);

		return ResponseEntity.ok().body(
			CommonResponse.builder()
				.statusCode(HttpStatus.OK.value())
				.message("삭제 완료")
				.build());
	}
}
