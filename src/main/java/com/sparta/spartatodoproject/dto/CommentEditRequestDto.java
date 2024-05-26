package com.sparta.spartatodoproject.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class CommentEditRequestDto {
	@NotBlank
	String contents;
	@NotNull
	long todoId;
}
