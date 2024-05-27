package com.sparta.spartatodoproject.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.spartatodoproject.dto.CommentEditRequestDto;
import com.sparta.spartatodoproject.dto.CommentRequestDto;
import com.sparta.spartatodoproject.dto.CommentResponseDto;
import com.sparta.spartatodoproject.entity.Comment;
import com.sparta.spartatodoproject.entity.Todo;
import com.sparta.spartatodoproject.entity.User;
import com.sparta.spartatodoproject.exception.CommentErrorCode;
import com.sparta.spartatodoproject.exception.MismatchException;
import com.sparta.spartatodoproject.exception.NotFoundException;
import com.sparta.spartatodoproject.exception.TodoErrorCode;
import com.sparta.spartatodoproject.exception.UserErrorCode;
import com.sparta.spartatodoproject.jwt.JwtUtil;
import com.sparta.spartatodoproject.repository.CommentRepository;
import com.sparta.spartatodoproject.repository.TodoRepository;
import com.sparta.spartatodoproject.repository.UserRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class CommentService {
	private final CommentRepository commentRepository;
	private final TodoRepository todoRepository;
	private final UserRepository userRepository;
	private final JwtUtil jwtUtil;

	public CommentResponseDto addComment(CommentRequestDto requestDto, String token) {
		User user = tokenUser(token);

		Todo todo = todoRepository.findById(requestDto.getTodoId()).orElseThrow(
			() -> new NotFoundException(TodoErrorCode.TODO_NOT_FOUND)
		);

		Comment comment = commentRepository.save(new Comment(requestDto, todo, user));
		return new CommentResponseDto(comment);
	}

	@Transactional
	public CommentResponseDto updateComment(long id,
		CommentEditRequestDto requestDto, String token) {
		User user = tokenUser(token);

		Comment comment = commentRepository.findById(id).orElseThrow(
			() -> new NotFoundException(CommentErrorCode.COMMENT_NOT_FOUND));

		if(!user.equals(comment.getUser()))
			throw new MismatchException(CommentErrorCode.ID_MISMATCH);

		if (requestDto.getTodoId() != comment.getTodo().getId())
			throw new MismatchException(CommentErrorCode.ID_MISMATCH);

		comment.update(requestDto.getContents());
		return new CommentResponseDto(comment);
	}

	public void deleteComment(long id, Long todoId, String token) {
		User user = tokenUser(token);

		if (todoId == null)
			throw new MismatchException(TodoErrorCode.ID_NOT_FOUND);

		Comment comment = commentRepository.findById(id).orElseThrow(
			() -> new NotFoundException(CommentErrorCode.COMMENT_NOT_FOUND)
		);

		if(!user.equals(comment.getUser()))
			throw new MismatchException(CommentErrorCode.ID_MISMATCH);

		if (comment.getTodo().getId() != todoId)
			throw new MismatchException(CommentErrorCode.ID_MISMATCH);

		todoRepository.findById(todoId).orElseThrow(
			() -> new NotFoundException(TodoErrorCode.TODO_NOT_FOUND)
		);

		commentRepository.delete(comment);
	}

	private User tokenUser(String token) {
		token = jwtUtil.substringToken(token);
		jwtUtil.validateToken(token);
		Claims info = jwtUtil.getUserInfoFromToken(token);
		String username = info.getSubject();
		return userRepository.findByUsername(username).orElseThrow(
			() -> new NotFoundException(UserErrorCode.USER_NOT_FOUND));
	}
}
