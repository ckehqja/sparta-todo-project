package com.sparta.spartatodoproject;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class SpartaTodoProjectApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpartaTodoProjectApplication.class, args);
	}

}
