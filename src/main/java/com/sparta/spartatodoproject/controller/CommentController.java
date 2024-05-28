package com.sparta.spartatodoproject.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartatodoproject.CommonResponse;
import com.sparta.spartatodoproject.dto.CommentRequestDto;
import com.sparta.spartatodoproject.dto.CommentResponseDto;
import com.sparta.spartatodoproject.jwt.JwtUtil;
import com.sparta.spartatodoproject.service.CommentService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/comment")
@RestController
public class CommentController {
	private final CommentService commentService;

	@PostMapping
	public ResponseEntity<CommonResponse<CommentResponseDto>> addComment(
		@Valid @RequestBody CommentRequestDto requestDto,
		@RequestHeader(JwtUtil.AUTHORIZATION_HEADER) String token) {

		CommentResponseDto responseDto = commentService.addComment(requestDto, token);
		return ResponseEntity.ok().body(CommonResponse.<CommentResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("댓글 저장")
			.data(responseDto).build());
	}

	@PutMapping("/{id}")
	public ResponseEntity<CommonResponse<CommentResponseDto>> updateComment(
		@PathVariable long id, @Valid @RequestBody CommentRequestDto requestDto,
		@RequestHeader(JwtUtil.AUTHORIZATION_HEADER) String token) {

		CommentResponseDto responseDto = commentService.updateComment(id, requestDto, token);
		return ResponseEntity.ok().body(CommonResponse.<CommentResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("댓글 수정")
			.data(responseDto).build());
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<CommonResponse> deleteComment(@PathVariable Long id,
		Long todoId, @RequestHeader(JwtUtil.AUTHORIZATION_HEADER) String token) {

		commentService.deleteComment(id, todoId, token);
		return ResponseEntity.ok().body(CommonResponse.builder()
			.statusCode(HttpStatus.OK.value())
			.message("댓글 삭제").build());
	}
}

