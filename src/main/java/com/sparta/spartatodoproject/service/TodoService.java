package com.sparta.spartatodoproject.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.spartatodoproject.dto.TodoAddRequestDto;
import com.sparta.spartatodoproject.dto.TodoListResponseDto;
import com.sparta.spartatodoproject.dto.TodoResponseDto;
import com.sparta.spartatodoproject.entity.Todo;
import com.sparta.spartatodoproject.entity.User;
import com.sparta.spartatodoproject.exception.MismatchException;
import com.sparta.spartatodoproject.exception.TodoErrorCode;
import com.sparta.spartatodoproject.exception.NotFoundException;
import com.sparta.spartatodoproject.jwt.JwtService;
import com.sparta.spartatodoproject.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TodoService {

	private final TodoRepository todoRepository;
	private final JwtService jwtService;

	public TodoResponseDto addTodo(TodoAddRequestDto requestDto, String token) {
		User user = jwtService.tokenUser(token);
		Todo todo = todoRepository.save(new Todo(requestDto, user));
		return new TodoResponseDto(todo);
	}

	public TodoResponseDto getTodo(long id) {
		Todo todo = todoRepository.findById(id).orElseThrow(
			() -> new NotFoundException(TodoErrorCode.TODO_NOT_FOUND));

		return new TodoResponseDto(todo);
	}

	public TodoListResponseDto getTodoList() {
		List<Todo> todoList = todoRepository.findAllByOrderByCreatedAtDesc();
		TodoListResponseDto todoListResponseDto = new TodoListResponseDto();
		for (Todo todo : todoList) {
			todoListResponseDto.getTodoResponseDtoList()
				.add(new TodoResponseDto(todo));
		}
		return todoListResponseDto;
	}

	@Transactional
	public TodoResponseDto updateTodo(TodoAddRequestDto requestDto, long id, String token) {
		Todo todo = todoRepository.findById(id).orElseThrow(
			() -> new NotFoundException(TodoErrorCode.TODO_NOT_FOUND));

		User user = jwtService.tokenUser(token);

		if(!user.equals(todo.getUser()))
			throw new MismatchException(TodoErrorCode.USER_MISMATCH);

		todo.update(requestDto);
		return new TodoResponseDto(todo);
	}

	public void deleteTodo(String token, Long id) {
		Todo todo = todoRepository.findById(id).orElseThrow(
			() -> new NotFoundException(TodoErrorCode.TODO_NOT_FOUND));

		User user = jwtService.tokenUser(token);

		if(!user.equals(todo.getUser()))
			throw new MismatchException(TodoErrorCode.USER_MISMATCH);

		todoRepository.delete(todo);
	}
}
