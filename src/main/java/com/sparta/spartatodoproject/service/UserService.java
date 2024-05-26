package com.sparta.spartatodoproject.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.sparta.spartatodoproject.dto.UserRequestDto;
import com.sparta.spartatodoproject.dto.UserResponseDto;
import com.sparta.spartatodoproject.entity.User;
import com.sparta.spartatodoproject.entity.UserRoleEnum;
import com.sparta.spartatodoproject.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UserService {
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final String ADMIN_TOKEN = "AAABnvxRVklrnYxKZ0aHgTBcXukeZygoC";

	public UserResponseDto addUser(UserRequestDto requestDto) {
		UserRoleEnum role = UserRoleEnum.USER;
		if (requestDto.getRole().equals(ADMIN_TOKEN)) {
			role = UserRoleEnum.ADMIN;
		}
		String password = passwordEncoder.encode(requestDto.getPassword());

		User user = userRepository.save(new User(requestDto, role, password));
		return new UserResponseDto(user);
	}
}
