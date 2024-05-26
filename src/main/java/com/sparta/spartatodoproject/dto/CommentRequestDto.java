package com.sparta.spartatodoproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentRequestDto {

	@NotBlank
	private String contents;
	private String userId;
	@NotNull
	private Long todoId;

}
