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

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name="todo controller", description = "일정 관리 api")
@RequiredArgsConstructor
@RequestMapping("/todo")
@RestController
public class TodoController {
	private final TodoService todoService;

	@Operation(description = "일정 등록")
	@Parameter(description = "dto를 받아 유효검사")
	@PostMapping
	public ResponseEntity<CommonResponse<TodoResponseDto>> addTodo(
		@Valid @RequestBody TodoAddRequestDto requestDto) {
		TodoResponseDto responseDto = todoService.addTodo(requestDto);
		return ResponseEntity.ok().body(CommonResponse.<TodoResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("일정 저장")
			.data(responseDto).build());
	}

	@Operation(description = "일정 조회")
	@Parameter(description = "경로값을 받아와서 조회")
	@GetMapping("/{id}")
	public ResponseEntity<CommonResponse<TodoResponseDto>> getTodoById(
		@PathVariable("id") long id) {
		TodoResponseDto responseDto = todoService.getTodo(id);
		return ResponseEntity.ok().body(CommonResponse.<TodoResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("일정 조회")
			.data(responseDto).build());
	}

	@Operation(description = "일정 전체 생성일 기준 내림차순으로 조회")
	@GetMapping
	public ResponseEntity<CommonResponse<TodoListResponseDto>> getAllTodos() {
		return ResponseEntity.ok().body(CommonResponse.<TodoListResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("일정 전체 조회")
			.data(todoService.getTodoList()).build());
	}

	@Operation(description = "일정 수정, 비밀번호가 일치해야 함")
	@Parameter(description = "수정할 일정을 경로로 받고 수정 dto 와 검증을 위한 비밀번호를 받아온다.")
	@PutMapping("/{id}")
	public ResponseEntity<CommonResponse<TodoResponseDto>> updateTodo(
		@Valid @RequestBody TodoAddRequestDto requestDto, @PathVariable("id") long id) {
		TodoResponseDto responseDto = todoService.updateTodo(requestDto, id);
		return ResponseEntity.ok().body(CommonResponse.<TodoResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("일정 저장")
			.data(responseDto).build());
	}

	@Operation(description = "일정 삭제, 비밀번호가 일치해야 함")
	@Parameter(description = "삭제할 일정을 경로로 받고 검증을 위한 비밀번호를 받아온다.")
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
