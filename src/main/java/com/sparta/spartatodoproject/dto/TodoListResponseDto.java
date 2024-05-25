package com.sparta.spartatodoproject.dto;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

@Getter
public class TodoListResponseDto {
	List<TodoResponseDto> todoResponseDtoList = new ArrayList<>();

}
