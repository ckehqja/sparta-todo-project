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
import com.sparta.spartatodoproject.service.RefreshTokenService;
import com.sparta.spartatodoproject.jwt.JwtUtil;
import com.sparta.spartatodoproject.service.CommentService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Tag(name = "CommentController", description = "댓글 관리 api")
@RequiredArgsConstructor
@RequestMapping("/comment")
@RestController
public class CommentController {
	private final CommentService commentService;
	private final RefreshTokenService refreshTokenService;

	@Operation(description = "댓글 등록")
	@Parameter(description = "@RequestBody 댓글 dto, @RequestHeader 토큰")
	@PostMapping
	public ResponseEntity<CommonResponse<CommentResponseDto>> addComment(
		@Valid @RequestBody CommentRequestDto requestDto,
		@RequestHeader(JwtUtil.AUTHORIZATION_HEADER) String token) {
		String username = refreshTokenService.tokenUsername(token);
		CommentResponseDto responseDto = commentService.addComment(requestDto, username);
		return ResponseEntity.ok().body(CommonResponse.<CommentResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("댓글 등록")
			.data(responseDto).build());
	}

	@Operation(description = "댓글 수정")
	@Parameter(description = "@PathVariable 댓글 아이디, @RequestBody 변경 내용, @RequestHeader 토큰")
	@PutMapping("/{id}")
	public ResponseEntity<CommonResponse<CommentResponseDto>> updateComment(
		@PathVariable long id, @Valid @RequestBody CommentRequestDto requestDto,
		@RequestHeader(JwtUtil.AUTHORIZATION_HEADER) String token) {
		String username = refreshTokenService.tokenUsername(token);
		CommentResponseDto responseDto = commentService.updateComment(id, requestDto, username);
		return ResponseEntity.ok().body(CommonResponse.<CommentResponseDto>builder()
			.statusCode(HttpStatus.OK.value())
			.message("댓글 수정")
			.data(responseDto).build());
	}

	@Operation(description = "댓글 삭제")
	@Parameter(description = "@PathVariable 댓글 아이디, @RequestParam todoId, @RequestHeader 토큰")
	@DeleteMapping("/{id}")
	public ResponseEntity<CommonResponse> deleteComment(@PathVariable Long id,
		Long todoId, @RequestHeader(JwtUtil.AUTHORIZATION_HEADER) String token) {

		String username = refreshTokenService.tokenUsername(token);
		commentService.deleteComment(id, todoId, username);
		return ResponseEntity.ok().body(CommonResponse.builder()
			.statusCode(HttpStatus.OK.value())
			.message("댓글 삭제").build());
	}
}

