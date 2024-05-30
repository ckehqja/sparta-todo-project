package com.sparta.spartatodoproject.entity;

import com.sparta.spartatodoproject.dto.UserRequestDto;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(name = "users")
@NoArgsConstructor
public class User extends Timestamped{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String nickname;

	@Column(unique = true)
	private String username;

	private String password;

	@Enumerated(EnumType.STRING)
	private UserRoleEnum role;

	// @OneToMany(mappedBy = "user", cascade = CascadeType.PERSIST)
	// private List<User> users = new ArrayList<>();

	public User(UserRequestDto requestDto, UserRoleEnum role, String password) {
		this.nickname = requestDto.getNickname();
		this.username = requestDto.getUsername();
		this.password = password;
		this.role = role;
	}

	public User(String nickname, String username, String password, UserRoleEnum role) {
		this.nickname = nickname;
		this.username = username;
		this.password = password;
		this.role = role;
	}
}
