package com.sparta.spartatodoproject.jwt;

import org.springframework.stereotype.Service;

import com.sparta.spartatodoproject.entity.User;
import com.sparta.spartatodoproject.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JwtService {
	private final JwtRepository jwtRepository;
	private final UserRepository userRepository;

	public void saveRefreshToken(String refreshToken, long id) {
		jwtRepository.save(new RefreshToken(refreshToken, id));
	}

	public boolean check(String refreshToken, String username) {
		RefreshToken findToken = jwtRepository.findRefreshTokenByToken(refreshToken)
			.orElseThrow();
		if (refreshToken.equals(findToken.getToken())) {
			User user = userRepository.findById(findToken.getUserId())
				.orElseThrow();
			if (findToken.getUserId() == user.getId())
				return true;
		}
		return false;
	}

	public void delete(String oldRefreshToken) {
		jwtRepository.deleteByToken(oldRefreshToken);
	}
}
