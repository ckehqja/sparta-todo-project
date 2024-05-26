package com.sparta.spartatodoproject.service;

import org.springframework.stereotype.Service;

import com.sparta.spartatodoproject.dto.CommentRequestDto;
import com.sparta.spartatodoproject.dto.CommentResponseDto;
import com.sparta.spartatodoproject.entity.Comment;
import com.sparta.spartatodoproject.entity.Todo;
import com.sparta.spartatodoproject.exception.ErrorCode;
import com.sparta.spartatodoproject.exception.TodoErrorCode;
import com.sparta.spartatodoproject.exception.TodoNotFoundException;
import com.sparta.spartatodoproject.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	private final CommentRepository commentRepository;
	private final TodoRepository todoRepository;

	public CommentResponseDto addComment(CommentRequestDto requestDto) {
		Todo todo = todoRepository.findById(requestDto.getTodoId()).orElseThrow(
			() -> new TodoNotFoundException(TodoErrorCode.TODO_NOT_FOUND));
		Comment comment = commentRepository.save(new Comment(requestDto, todo));
		return new CommentResponseDto(comment);
	}
}
