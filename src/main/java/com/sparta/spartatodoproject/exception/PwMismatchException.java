package com.sparta.spartatodoproject.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PwMismatchException extends RuntimeException {
	private final ErrorCode errorCode;
}
