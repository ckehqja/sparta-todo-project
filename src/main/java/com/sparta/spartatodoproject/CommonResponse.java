package com.sparta.spartatodoproject;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CommonResponse<T> {
	private Integer statusCode;
	private String message;
	private T data;
}
