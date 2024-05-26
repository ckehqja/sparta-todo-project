package com.sparta.spartatodoproject.controller;

import java.net.http.HttpResponse;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.sparta.spartatodoproject.CommonResponse;
import com.sparta.spartatodoproject.dto.CommentRequestDto;
import com.sparta.spartatodoproject.dto.CommentResponseDto;
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
		@Valid @RequestBody CommentRequestDto requestDto) {
		CommentResponseDto responseDto = commentService.addComment(requestDto);
		return ResponseEntity.ok().body(CommonResponse.<CommentResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("댓글 저장")
			.data(responseDto).build());
	}
}

