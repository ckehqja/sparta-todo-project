package com.sparta.spartatodoproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentRequestDto {

	@NotBlank
	private String contents;
	@NotNull
	private Long todoId;

}
