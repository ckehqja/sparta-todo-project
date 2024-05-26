package com.sparta.spartatodoproject.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class TodoNotFoundException extends RuntimeException {
	private final ErrorCode errorCode;
}
