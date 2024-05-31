package com.sparta.spartatodoproject.jwt;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sparta.spartatodoproject.entity.RefreshToken;
import com.sparta.spartatodoproject.entity.User;

public interface JwtRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByUser(User user);
}
