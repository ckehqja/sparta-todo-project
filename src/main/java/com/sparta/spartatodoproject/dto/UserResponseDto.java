package com.sparta.spartatodoproject.dto;

import java.time.LocalDateTime;

import com.sparta.spartatodoproject.entity.User;

import lombok.Getter;

@Getter
public class UserResponseDto {
	private long id;
	private String nickname;
	private String username;
	private String password;
	private String role;
	private LocalDateTime createdAt;

	public UserResponseDto(User user) {
		this.id = user.getId();
		this.nickname = user.getNickname();
		this.username = user.getUsername();
		this.password = user.getPassword();
		this.role = user.getRole().toString();
		this.createdAt = user.getCreatedAt();
	}
}
