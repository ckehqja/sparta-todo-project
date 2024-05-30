package com.sparta.spartatodoproject.jwt;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.spartatodoproject.entity.RefreshToken;
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
	public void saveRefreshToken(String refreshToken, User user) {
		// if (jwtRepository.findByUser(user).isPresent()) {
		// 	// RefreshToken oldToken = jwtRepository.findByUser(user).get();
		// 	// oldToken.updateToken(refreshToken);
		// } else {
		// 	jwtRepository.save(new RefreshToken(refreshToken, user));
		// }
	}

	// public boolean check(String refreshToken, String username) {
	// 	RefreshToken findToken = jwtRepository.findRefreshTokenByToken(refreshToken)
	// 		.orElseThrow();
	// 	if (refreshToken.equals(findToken.getToken())) {
	// 		User user = findToken.getUser();
	// 		return true;
	// 	}
	// 	return false;
	// }
	//
	// public void delete(String oldRefreshToken) {
	// 	jwtRepository.deleteByToken(oldRefreshToken);
	// }

	public User tokenUser(String token) {
		token = jwtUtil.substringToken(token);
		jwtUtil.validateToken(token);
		Claims info = jwtUtil.getUserInfoFromToken(token);
		String username = info.getSubject();
		// return userRepository.findByUsername(username).orElseThrow(
		// 	() -> new NotFoundException(UserErrorCode.USER_NOT_FOUND));
		return null;
	}
}
