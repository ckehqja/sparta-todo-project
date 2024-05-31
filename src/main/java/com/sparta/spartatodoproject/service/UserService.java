package com.sparta.spartatodoproject.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.sparta.spartatodoproject.dto.LoginRequestDto;
import com.sparta.spartatodoproject.dto.UserRequestDto;
import com.sparta.spartatodoproject.dto.UserResponseDto;
import com.sparta.spartatodoproject.entity.User;
import com.sparta.spartatodoproject.entity.UserRoleEnum;
import com.sparta.spartatodoproject.exception.MismatchException;
import com.sparta.spartatodoproject.exception.NotFoundException;
import com.sparta.spartatodoproject.exception.UserErrorCode;
import com.sparta.spartatodoproject.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {

	private final UserRepository userRepository;

	@Value("${user.admin.token}")
	private String ADMIN_TOKEN;

	public UserResponseDto signup(UserRequestDto requestDto) {
		UserRoleEnum role = UserRoleEnum.USER;
		if (requestDto.getRole().equals(ADMIN_TOKEN)) {
			role = UserRoleEnum.ADMIN;
		}
		String password = requestDto.getPassword();

		User user = userRepository.save(new User(requestDto, role, password));
		return new UserResponseDto(user);
	}

	public User login(LoginRequestDto requestDto) {
		User user = userRepository.findByUsername(
			requestDto.getUsername()).orElseThrow(
			() -> new NotFoundException(UserErrorCode.USER_NOT_FOUND)
		);

		if (!user.getPassword().equals(requestDto.getPassword()))
			throw new MismatchException(UserErrorCode.PW_MISMATCH);
		return user;
	}

	public void delete(long id, String username) {
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new NotFoundException(UserErrorCode.USER_NOT_FOUND)
		);
		if (user.getId() != id)
			throw new MismatchException(UserErrorCode.USER_NOT_FOUND);
		userRepository.deleteById(id);
	}
}
