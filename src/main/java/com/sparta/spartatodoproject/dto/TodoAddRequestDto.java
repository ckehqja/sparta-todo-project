package com.sparta.spartatodoproject.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TodoAddRequestDto {
	@NotBlank
	@Size(min = 1, max = 200)
	private String title;
	@Email
	private String manager;
	@NotBlank
	private String password;
	private String contents;

}
