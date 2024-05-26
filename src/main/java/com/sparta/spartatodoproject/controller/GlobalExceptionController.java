package com.sparta.spartatodoproject.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.sparta.spartatodoproject.exception.CommonErrorCode;
import com.sparta.spartatodoproject.exception.ErrorCode;
import com.sparta.spartatodoproject.exception.ErrorResponse;
import com.sparta.spartatodoproject.exception.PwMismatchException;
import com.sparta.spartatodoproject.exception.TodoNotFoundException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionController extends ResponseEntityExceptionHandler {

	@ExceptionHandler(TodoNotFoundException.class)
	public ResponseEntity<Object> handleCustomException(TodoNotFoundException e) {
		ErrorCode errorCode = e.getErrorCode();
		return handleExceptionInternal(errorCode);
	}

	@ExceptionHandler(PwMismatchException.class)
	public ResponseEntity<Object> handleCustomException(PwMismatchException e) {
		ErrorCode errorCode = e.getErrorCode();
		return handleExceptionInternal(errorCode);
	}

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException e,
		HttpHeaders headers, HttpStatusCode status, WebRequest request) {
		log.warn("handleIllegalArgument", e);
		ErrorCode errorCode = CommonErrorCode.INVALID_PARAMETER;
		return handleExceptionInternal(e, errorCode);

	}

	@ExceptionHandler({Exception.class})
	public ResponseEntity<Object> handleAllException(Exception ex) {
		log.warn("handleAllException", ex);
		ErrorCode errorCode = CommonErrorCode.INTERNAL_SERVER_ERROR;
		return handleExceptionInternal(errorCode);
	}

	private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(makeErrorResponse(errorCode));
	}

	private ErrorResponse makeErrorResponse(ErrorCode errorCode) {
		return ErrorResponse.builder()
			.code(errorCode.name())
			.message(errorCode.getMessage())
			.httpStatus(errorCode.getHttpStatus())
			.build();
	}

	private ResponseEntity<Object> handleExceptionInternal(ErrorCode errorCode, String message) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(makeErrorResponse(errorCode, message));
	}

	private ErrorResponse makeErrorResponse(ErrorCode errorCode, String message) {
		return ErrorResponse.builder()
			.code(errorCode.name())
			.message(message)
			.httpStatus(errorCode.getHttpStatus())
			.build();
	}

	private ResponseEntity<Object> handleExceptionInternal(BindException e, ErrorCode errorCode) {
		return ResponseEntity.status(errorCode.getHttpStatus())
			.body(makeErrorResponse(e, errorCode));
	}

	private ErrorResponse makeErrorResponse(BindException e, ErrorCode errorCode) {
		List<ErrorResponse.ValidationError> validationErrorList = e.getBindingResult()
			.getFieldErrors()
			.stream()
			.map(ErrorResponse.ValidationError::of)
			.collect(Collectors.toList());

		return ErrorResponse.builder()
			.code(errorCode.name())
			.message(errorCode.getMessage())
			.httpStatus(errorCode.getHttpStatus())
			.errors(validationErrorList)
			.build();
	}
}
