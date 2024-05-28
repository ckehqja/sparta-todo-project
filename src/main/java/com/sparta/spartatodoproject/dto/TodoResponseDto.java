package com.sparta.spartatodoproject.dto;

import java.time.LocalDateTime;

import com.sparta.spartatodoproject.entity.Todo;

import lombok.Getter;

@Getter
public class TodoResponseDto {
	private long id;
	private String title;
	private String contents;
	private String manager;
	private LocalDateTime createdDate;

	public TodoResponseDto(Todo todo) {
		this.id = todo.getId();
		this.title = todo.getTitle();
		this.contents = todo.getContents();
		this.manager = todo.getUser().getNickname();
		this.createdDate = todo.getCreatedAt();
	}
}
