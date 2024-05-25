package com.sparta.spartatodoproject.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartatodoproject.dto.TodoListResponseDto;
import com.sparta.spartatodoproject.dto.TodoResponseDto;
import com.sparta.spartatodoproject.service.TodoService;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/todo")
@RestController
public class TodoController {
	private final TodoService todoService;

	@PostMapping
	public ResponseEntity<TodoResponseDto> addTodo(@RequestBody TodoAddRequestDto requestDto) {
		TodoResponseDto responseDto = todoService.addTodo(requestDto);
		return ResponseEntity.ok().body(responseDto);
	}

	@GetMapping("/{id}")
	public ResponseEntity<TodoResponseDto> getTodoById(
		@PathVariable("id") long id) {
		TodoResponseDto responseDto = todoService.getTodo(id);
		return ResponseEntity.ok().body(responseDto);
	}

	@GetMapping
	public ResponseEntity<TodoListResponseDto> getAllTodos() {
		return ResponseEntity.ok().body(todoService.getTodoList());
	}

	@PutMapping("/{id}")
	public ResponseEntity<TodoResponseDto> updateTodo(
		@RequestBody TodoAddRequestDto requestDto, @PathVariable("id") long id) {
		TodoResponseDto responseDto = todoService.updateTodo(requestDto, id);
		return ResponseEntity.ok().body(responseDto);
	}
}
