package com.sparta.spartatodoproject.jwt;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.spartatodoproject.entity.User;
import com.sparta.spartatodoproject.exception.NotFoundException;
import com.sparta.spartatodoproject.exception.UserErrorCode;
import com.sparta.spartatodoproject.repository.UserRepository;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class JwtService {
	private final JwtUtil jwtUtil;
	private final JwtRepository jwtRepository;
	private final UserRepository userRepository;

	@Transactional
	public void saveRefreshToken(String refreshToken, long id) {
		if (jwtRepository.findById(id).isPresent()) {
			RefreshToken oldToken = jwtRepository.findById(id).get();
			oldToken.updateToken(refreshToken);
		} else {
			jwtRepository.save(new RefreshToken(refreshToken, id));
		}
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


	public User tokenUser(String token) {
		token = jwtUtil.substringToken(token);
		jwtUtil.validateToken(token);
		Claims info = jwtUtil.getUserInfoFromToken(token);
		String username = info.getSubject();
		return userRepository.findByUsername(username).orElseThrow(
			() -> new NotFoundException(UserErrorCode.USER_NOT_FOUND));
	}
}
