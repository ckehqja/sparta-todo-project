package com.sparta.spartatodoproject.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartatodoproject.CommonResponse;
import com.sparta.spartatodoproject.dto.LoginRequestDto;
import com.sparta.spartatodoproject.dto.UserRequestDto;
import com.sparta.spartatodoproject.dto.UserResponseDto;
import com.sparta.spartatodoproject.security.UserDetailsServiceImpl;
import com.sparta.spartatodoproject.service.UserService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/user")
@RestController
public class UserController {
	private final UserService userService;

	@PostMapping
	public ResponseEntity<CommonResponse<UserResponseDto>> addUser(
		@Valid @RequestBody UserRequestDto requestDto) {
		UserResponseDto userResponseDto = userService.addUser(requestDto);
		return ResponseEntity.ok().body(CommonResponse.<UserResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("회원가입 완료")
			.data(userResponseDto)
			.build());
	}
}
