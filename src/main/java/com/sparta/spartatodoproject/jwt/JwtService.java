package com.sparta.spartatodoproject.jwt;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sparta.spartatodoproject.entity.RefreshToken;
import com.sparta.spartatodoproject.entity.User;
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
		//기존 리플래시 토큰을 가진 유저이면 로그인 시 새로운 리플래시 토큰으로 변경해준다.
		if (jwtRepository.findByUser(user).isPresent())
			jwtRepository.findByUser(user).get().updateToken(refreshToken);
		//db 에 저장된 리플래시 토큰이 없다면 새로 생성하여 추가한다.
		else
			jwtRepository.save(new RefreshToken(refreshToken, user));
	}

	//토큰에 든 유저이름으로 유저를 찾아 반환한다.
	public String tokenUsername(String token) {
		jwtUtil.validateToken(jwtUtil.substringToken(token));
		Claims info = jwtUtil.getUserInfoFromToken(jwtUtil.substringToken(token));
		return info.getSubject();
	}
}
