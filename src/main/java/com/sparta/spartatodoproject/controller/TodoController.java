package com.sparta.spartatodoproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartatodoproject.CommonResponse;
import com.sparta.spartatodoproject.dto.TodoAddRequestDto;
import com.sparta.spartatodoproject.dto.TodoListResponseDto;
import com.sparta.spartatodoproject.dto.TodoResponseDto;
import com.sparta.spartatodoproject.service.TodoService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/todo")
@RestController
public class TodoController {
	private final TodoService todoService;

	@PostMapping
	public ResponseEntity<CommonResponse<TodoResponseDto>> addTodo(
		@Valid @RequestBody TodoAddRequestDto requestDto) {
		TodoResponseDto responseDto = todoService.addTodo(requestDto);
		return ResponseEntity.ok().body(CommonResponse.<TodoResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("일정 저장")
			.data(responseDto).build());
	}

	@GetMapping("/{id}")
	public ResponseEntity<CommonResponse<TodoResponseDto>> getTodoById(
		@PathVariable("id") long id) {
		TodoResponseDto responseDto = todoService.getTodo(id);
		return ResponseEntity.ok().body(CommonResponse.<TodoResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("일정 조회")
			.data(responseDto).build());
	}

	@GetMapping
	public ResponseEntity<CommonResponse<TodoListResponseDto>> getAllTodos() {
		return ResponseEntity.ok().body(CommonResponse.<TodoListResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("일정 전체 조회")
			.data(todoService.getTodoList()).build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<CommonResponse<TodoResponseDto>> updateTodo(
		@RequestBody TodoAddRequestDto requestDto, @PathVariable("id") long id) {
		TodoResponseDto responseDto = todoService.updateTodo(requestDto, id);
		return ResponseEntity.ok().body(CommonResponse.<TodoResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("일정 저장")
			.data(responseDto).build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<CommonResponse> deleteTodo(
		String password, @PathVariable Long id) {
		todoService.deleteTodo(password, id);
		return ResponseEntity.ok().body(CommonResponse.<TodoResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("일정 삭제")
			.build());
	}
}
