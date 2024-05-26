package com.sparta.spartatodoproject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class TodoAddRequestDto {
	@NotBlank
	private String title;
	private String manager;
	private String password;
	private String contents;

}
