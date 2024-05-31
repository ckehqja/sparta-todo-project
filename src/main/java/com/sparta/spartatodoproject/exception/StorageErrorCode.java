package com.sparta.spartatodoproject.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StorageErrorCode implements ErrorCode {

	FILE_NOT_FOUND(HttpStatus.NOT_FOUND,
		"파일이 없습니다."),
	FILE_TYPE_MISMATCH(HttpStatus.NOT_FOUND,
		"파일 타입이 맞지 안습니다.");

	private final HttpStatus httpStatus;
	private final String message;
}
