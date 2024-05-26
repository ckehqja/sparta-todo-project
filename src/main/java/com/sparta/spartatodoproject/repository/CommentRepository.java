package com.sparta.spartatodoproject.repository;

import org.springframework.data.repository.CrudRepository;

import com.sparta.spartatodoproject.entity.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
