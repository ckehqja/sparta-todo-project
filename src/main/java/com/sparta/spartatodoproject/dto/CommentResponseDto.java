package com.sparta.spartatodoproject.dto;

import java.time.LocalDateTime;

import com.sparta.spartatodoproject.entity.Comment;

import lombok.Getter;

@Getter
public class CommentResponseDto {
	private final long id;
	private final String contents;
	private final String userId;
	private final long todoId;
	private final LocalDateTime createdDate;

	public CommentResponseDto(Comment comment) {
		this.id = comment.getId();
		this.contents = comment.getContents();
		this.userId = comment.getUserId();
		this.todoId = comment.getTodo().getId();
		this.createdDate = comment.getCreatedAt();
	}
}
