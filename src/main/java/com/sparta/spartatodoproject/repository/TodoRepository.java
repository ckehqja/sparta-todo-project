package com.sparta.spartatodoproject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartatodoproject.entity.Todo;

public interface TodoRepository extends JpaRepository<Todo, Long> {
	List<Todo> findAllByOrderByCreatedAtDesc();
}
