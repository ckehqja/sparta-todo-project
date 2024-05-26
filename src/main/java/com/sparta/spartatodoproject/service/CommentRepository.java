package com.sparta.spartatodoproject.service;

import org.springframework.data.repository.CrudRepository;

import com.sparta.spartatodoproject.entity.Comment;

public interface CommentRepository extends CrudRepository<Comment, Long> {
}
