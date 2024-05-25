package com.sparta.spartatodoproject.entity;

import com.sparta.spartatodoproject.controller.TodoAddRequestDto;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Todo extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String title;
	private String contents;
	private String manager;
	private String password;

	public Todo(TodoAddRequestDto requestDto) {
		this.title = requestDto.getTitle();
		this.contents = requestDto.getContents();
		this.manager = requestDto.getManager();
		this.password = requestDto.getPassword();
	}

	public void update(TodoAddRequestDto requestDto) {
		this.title = requestDto.getTitle();
		this.contents = requestDto.getContents();
		this.manager = requestDto.getManager();
	}
}
