package com.sparta.spartatodoproject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartatodoproject.entity.LoginHistory;

public interface LoginHistoryRepository extends JpaRepository<LoginHistory, Long> {
}
