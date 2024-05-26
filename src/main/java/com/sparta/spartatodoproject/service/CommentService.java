package com.sparta.spartatodoproject.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.spartatodoproject.dto.CommentEditRequestDto;
import com.sparta.spartatodoproject.dto.CommentRequestDto;
import com.sparta.spartatodoproject.dto.CommentResponseDto;
import com.sparta.spartatodoproject.entity.Comment;
import com.sparta.spartatodoproject.entity.Todo;
import com.sparta.spartatodoproject.exception.CommentErrorCode;
import com.sparta.spartatodoproject.exception.MismatchException;
import com.sparta.spartatodoproject.exception.TodoErrorCode;
import com.sparta.spartatodoproject.exception.NotFoundException;
import com.sparta.spartatodoproject.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	private final CommentRepository commentRepository;
	private final TodoRepository todoRepository;

	public CommentResponseDto addComment(CommentRequestDto requestDto) {
		Todo todo = todoRepository.findById(requestDto.getTodoId()).orElseThrow(
			() -> new NotFoundException(TodoErrorCode.TODO_NOT_FOUND));
		Comment comment = commentRepository.save(new Comment(requestDto, todo));
		return new CommentResponseDto(comment);
	}

	@Transactional
	public CommentResponseDto updateComment(long id, CommentEditRequestDto requestDto) {
		Comment comment = commentRepository.findById(id).orElseThrow(
			() -> new NotFoundException(CommentErrorCode.COMMENT_NOT_FOUND));

		if(requestDto.getTodoId() != comment.getTodo().getId())
			throw new MismatchException(CommentErrorCode.ID_MISMATCH);

		comment.update(requestDto.getContents());
		return new CommentResponseDto(comment);
	}
}
