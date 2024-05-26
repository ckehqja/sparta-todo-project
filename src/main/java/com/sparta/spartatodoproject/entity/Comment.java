package com.sparta.spartatodoproject.entity;

import com.sparta.spartatodoproject.dto.CommentRequestDto;

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
@NoArgsConstructor
@Entity
public class Comment extends Timestamped {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;
	private String contents;
	private String userId;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "todo_id")
	private Todo todo;

	public Comment(CommentRequestDto requestDto, Todo todo) {
		this.contents = requestDto.getContents();
		this.userId = requestDto.getUserId();
		this.todo = todo;
	}

	public void update(String contents) {
		this.contents = contents;
	}
}
