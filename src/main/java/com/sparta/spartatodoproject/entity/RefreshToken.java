package com.sparta.spartatodoproject.entity;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@Entity
public class RefreshToken {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	private String token;

	@OnDelete(action = OnDeleteAction.CASCADE)
	@OneToOne
	@JoinColumn(name = "users_id")
	private User user;

	public RefreshToken(String refreshToken, User user) {
		token = refreshToken;
		this.user = user;
	}

	public void updateToken(String refreshToken) {
		token = refreshToken;
	}
}
