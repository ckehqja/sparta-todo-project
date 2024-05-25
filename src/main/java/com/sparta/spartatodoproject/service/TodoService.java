package com.sparta.spartatodoproject.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.spartatodoproject.controller.TodoAddRequestDto;
import com.sparta.spartatodoproject.dto.TodoListResponseDto;
import com.sparta.spartatodoproject.dto.TodoResponseDto;
import com.sparta.spartatodoproject.entity.Todo;
import com.sparta.spartatodoproject.repository.TodoRepository;

import jakarta.persistence.EntityNotFoundException;
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
			() -> new EntityNotFoundException("Todo with id "
				+ id + " not found"));
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
			() -> new EntityNotFoundException("Todo with id "
				+ id + " not found")
		);
		if(!requestDto.getPassword().equals(todo.getPassword())){
			log.info(requestDto.getPassword(), todo.getPassword());
			throw new IllegalArgumentException("비밀번호 불일치");
		}
		todo.update(requestDto);
		return new TodoResponseDto(todo);
	}
}
