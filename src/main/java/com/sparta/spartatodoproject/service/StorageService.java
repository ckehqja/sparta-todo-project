package com.sparta.spartatodoproject.service;

import java.io.IOException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.spartatodoproject.ImageUtils;
import com.sparta.spartatodoproject.entity.ImageData;
import com.sparta.spartatodoproject.entity.Todo;
import com.sparta.spartatodoproject.exception.NotFoundException;
import com.sparta.spartatodoproject.exception.StorageErrorCode;
import com.sparta.spartatodoproject.repository.StorageRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StorageService {

	private final StorageRepository storageRepository;

	public String uploadImage(MultipartFile file, Todo todo) throws IOException {
		log.info("Uploading image {} {}", file.getContentType(), "image/jpeg".equals(file.getContentType()));
		if ("image/jpeg".equals(file.getContentType()) ||
			"image/png".equals(file.getContentType())) {
			log.info("upload file: {}", file);
			ImageData imageData = storageRepository.save(
				ImageData.builder()
					.name(file.getOriginalFilename())
					.type(file.getContentType())
					.size(file.getSize())
					.imageData(ImageUtils.compressImage(file.getBytes()))
					.todo(todo)
					.build());
			if (imageData != null) {
				log.info("imageData: {}", imageData);
				return "file uploaded successfully : " + file.getOriginalFilename();
			}
		}

		throw new NotFoundException(StorageErrorCode.FILE_TYPE_MISMATCH);
	}

	@Transactional
	public void update(MultipartFile file, Todo todo) throws IOException {

		ImageData imageData = null;
		try {
			imageData = storageRepository.findByTodo(todo).get();
		} catch (Exception e) {
			imageData = new ImageData(file.getName(), file.getContentType(),
				file.getSize(), file.getBytes(), todo);
			storageRepository.save(imageData);
		}

		imageData.updateFile(ImageUtils.compressImage(file.getBytes()));
	}
}
