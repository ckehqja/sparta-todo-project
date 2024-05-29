package com.sparta.spartatodoproject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartatodoproject.entity.ImageData;
import com.sparta.spartatodoproject.entity.Todo;

public interface StorageRepository extends JpaRepository<ImageData, Long> {

	Optional<ImageData> findByName(String fileName);

	Optional<ImageData> findByTodo(Todo todo);
}