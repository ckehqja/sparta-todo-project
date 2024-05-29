package com.sparta.spartatodoproject.controller;

import java.io.IOException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.spartatodoproject.service.StorageService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
public class StorageController {

	final private StorageService storageService;

	// 업로드
	// @PostMapping
	// public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile file) throws IOException {
	// 	String uploadImage = storageService.uploadImage(file, todo);
	// 	log.info(file.getContentType());
	// 	return ResponseEntity.status(HttpStatus.OK)
	// 		.body(uploadImage);
	// }

	// 다운로드
	@GetMapping("/{id}")
	public ResponseEntity<?> downloadImage(@PathVariable("id") Long id) {
		byte[] downloadImage = storageService.downloadImage(id);
		return ResponseEntity.status(HttpStatus.OK)
			.contentType(MediaType.valueOf("image/png"))
			.body(downloadImage);
	}

}