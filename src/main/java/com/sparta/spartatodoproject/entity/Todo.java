package com.sparta.spartatodoproject.entity;

import com.sparta.spartatodoproject.dto.TodoAddRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@NoArgsConstructor
public class Todo extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private String title;
	private String contents;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "users_id")
	private User user;


	public Todo(TodoAddRequestDto requestDto, User user) {
		this.title = requestDto.getTitle();
		this.contents = requestDto.getContents();
		this.user = user;
	}

	public void update(TodoAddRequestDto requestDto) {
		this.title = requestDto.getTitle();
		this.contents = requestDto.getContents();
	}

	public Todo(String title, String contents, User user) {
		this.title = title;
		this.contents = contents;
		this.user = user;
	}
}
