package com.sparta.spartatodoproject.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.spartatodoproject.dto.TodoAddRequestDto;
import com.sparta.spartatodoproject.dto.TodoListResponseDto;
import com.sparta.spartatodoproject.dto.TodoResponseDto;
import com.sparta.spartatodoproject.entity.Todo;
import com.sparta.spartatodoproject.exception.MismatchException;
import com.sparta.spartatodoproject.exception.TodoErrorCode;
import com.sparta.spartatodoproject.exception.NotFoundException;
import com.sparta.spartatodoproject.repository.TodoRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TodoService {

	private static final Logger log = LoggerFactory.getLogger(TodoService.class);
	private final TodoRepository todoRepository;

	public TodoResponseDto addTodo(TodoAddRequestDto requestDto) {
		Todo todo = todoRepository.save(new Todo(requestDto));
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
	public TodoResponseDto updateTodo(TodoAddRequestDto requestDto, long id) {
		Todo todo = todoRepository.findById(id).orElseThrow(
			() -> new NotFoundException(TodoErrorCode.TODO_NOT_FOUND));

		if (!requestDto.getPassword().equals(todo.getPassword()))
			throw new MismatchException(TodoErrorCode.PW_MISMATCH);

		todo.update(requestDto);
		return new TodoResponseDto(todo);
	}

	public void deleteTodo(String password, Long id) {
		Todo todo = todoRepository.findById(id).orElseThrow(
			() -> new NotFoundException(TodoErrorCode.TODO_NOT_FOUND));

		if (!password.equals(todo.getPassword()))
			throw new MismatchException(TodoErrorCode.PW_MISMATCH);

		todoRepository.delete(todo);
	}
}
