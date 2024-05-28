package com.sparta.spartatodoproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class TodoAddRequestDto {
	@NotBlank
	@Size(min = 1, max = 200)
	private String title;
	private String contents;

}
