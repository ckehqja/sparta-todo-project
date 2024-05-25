package com.sparta.spartatodoproject.controller;

import lombok.Getter;

@Getter
public class TodoAddRequestDto {
	private String title;
	private String manager;
	private String password;
	private String contents;

}
