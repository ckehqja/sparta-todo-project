package com.sparta.spartatodoproject.dto;

import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class UserRequestDto {
	private String nickname;
	@Size(min = 4, max = 10)
	private String username;
	private String password;
	private String role;

}
