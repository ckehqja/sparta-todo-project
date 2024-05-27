package com.sparta.spartatodoproject.jwt;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

public interface JwtRepository extends JpaRepository<RefreshToken, Long> {
	Optional<RefreshToken> findRefreshTokenByToken(String token);

	@Transactional
	void deleteByToken(String oldRefreshToken);
}
