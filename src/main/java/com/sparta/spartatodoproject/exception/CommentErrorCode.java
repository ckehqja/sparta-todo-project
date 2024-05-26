package com.sparta.spartatodoproject.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum CommentErrorCode implements ErrorCode {

	COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND,
		"댓글이 없습니다."),
	ID_MISMATCH(HttpStatus.NOT_FOUND,
		"댓글과 일정이 일치하지 안습니다." );

	private final HttpStatus httpStatus;
	private final String message;
}
