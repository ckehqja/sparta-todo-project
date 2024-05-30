package com.sparta.spartatodoproject.entity;

public enum LoginStatus {
	BEFORE("before"), AFTER_RETURNING("afterReturning"), AFTER_THROWING("afterThrowing");

	private String status;

	LoginStatus(String status) {
		this.status = status;
	}
}
