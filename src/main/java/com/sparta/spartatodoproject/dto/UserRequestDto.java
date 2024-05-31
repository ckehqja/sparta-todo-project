package com.sparta.spartatodoproject.dto;

import jakarta.validation.constraints.Pattern;
import lombok.Getter;

@Getter
public class UserRequestDto {
	private String nickname;
	@Pattern(regexp = "^[A-Za-z0-9]{4,10}$", message = "대소문자, 숫자를 4~10자 사이 입력")
	private String username;
	@Pattern(regexp = "^[A-Za-z0-9]{8,15}$", message = "대소문자, 숫자를 8~15자 사이 입력")
	private String password;
	private String role;

}
