package com.sparta.spartatodoproject.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.spartatodoproject.CommonResponse;
import com.sparta.spartatodoproject.dto.TodoAddRequestDto;
import com.sparta.spartatodoproject.dto.TodoListResponseDto;
import com.sparta.spartatodoproject.dto.TodoResponseDto;
import com.sparta.spartatodoproject.service.RefreshTokenService;
import com.sparta.spartatodoproject.jwt.JwtUtil;
import com.sparta.spartatodoproject.service.TodoService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Tag(name = "todo controller", description = "일정 관리 api")
@RequiredArgsConstructor
@RequestMapping("/todo")
@RestController
public class TodoController {

	private final TodoService todoService;
	private final RefreshTokenService jwtService;

	@Operation(description = "일정 등록 + 파일 등록")
	@Parameter(description = "이미지 파일을 받기위해 @RequestPart 로 이미지와 dto 를 받고 사용자 정보는 토큰으로 해결")
	@PostMapping
	public ResponseEntity<CommonResponse<TodoResponseDto>> addTodo(
		@Valid @RequestPart TodoAddRequestDto requestDto,
		@RequestPart(value = "image", required = false) MultipartFile file,
		@RequestHeader(JwtUtil.AUTHORIZATION_HEADER) String token) throws IOException {
		String username = jwtService.tokenUsername(token);
		//사용자 토큰이 없으면 오류발생
		TodoResponseDto responseDto = todoService.addTodo(requestDto, username, file);

		return ResponseEntity.ok().body(CommonResponse.<TodoResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("일정 저장")
			.data(responseDto).build());
	}

	@Operation(description = "일정 상세 조회")
	@Parameter(description = "경로값을 받아와서 조회")
	@GetMapping("/{id}")
	public ResponseEntity<CommonResponse<TodoResponseDto>> getTodoById(
		@PathVariable("id") long id) {

		return ResponseEntity.ok().body(CommonResponse.<TodoResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("일정 조회")
			.data(todoService.getTodo(id)).build());
	}

	@Operation(description = "일정 전체 생성일 기준 내림차순으로 조회")
	@GetMapping
	public ResponseEntity<CommonResponse<TodoListResponseDto>> getAllTodos() {

		return ResponseEntity.ok().body(CommonResponse.<TodoListResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("일정 전체 조회")
			.data(todoService.getTodoList()).build());
	}

	@Operation(description = "일정 수정 - 작성자만 수정 가능")
	@Parameter(description = "수정할 일정을 경로로 받고 수정 dto 와 검증을 위한 비밀번호를 받아온다.")
	@PutMapping("/{id}")
	public ResponseEntity<CommonResponse<TodoResponseDto>> updateTodo(
		@Valid @RequestPart TodoAddRequestDto requestDto, @PathVariable("id") long id,
		@RequestPart(value = "image", required = false) MultipartFile file,
		@RequestHeader(JwtUtil.AUTHORIZATION_HEADER) String token) throws IOException {
		String username = jwtService.tokenUsername(token);

		TodoResponseDto responseDto = todoService.updateTodo(requestDto, id, username, file);
		return ResponseEntity.ok().body(CommonResponse.<TodoResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("일정 저장")
			.data(responseDto).build());
	}

	@Operation(description = "일정 삭제, 작성자만")
	@Parameter(description = "경로로 아아디를 받고 토큰으로 작성자 확인 후 삭제")
	@DeleteMapping("/{id}")
	public ResponseEntity<CommonResponse> deleteTodo(
		@PathVariable Long id, @RequestHeader(JwtUtil.AUTHORIZATION_HEADER) String token) {
		String username = jwtService.tokenUsername(token);
		todoService.deleteTodo(username, id);
		return ResponseEntity.ok().body(CommonResponse.<TodoResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("일정 삭제")
			.build());
	}
}
