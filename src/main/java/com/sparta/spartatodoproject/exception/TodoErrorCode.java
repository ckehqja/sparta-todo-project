package com.sparta.spartatodoproject.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum TodoErrorCode implements ErrorCode{

	PW_MISMATCH(HttpStatus.BAD_REQUEST,
		"비밀번호 불일치"),
	TODO_NOT_FOUND(HttpStatus.NOT_FOUND,
		"일정이 없습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}