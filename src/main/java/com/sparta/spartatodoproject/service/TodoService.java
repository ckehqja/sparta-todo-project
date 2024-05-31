package com.sparta.spartatodoproject.service;

import java.io.IOException;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import com.sparta.spartatodoproject.dto.TodoAddRequestDto;
import com.sparta.spartatodoproject.dto.TodoListResponseDto;
import com.sparta.spartatodoproject.dto.TodoResponseDto;
import com.sparta.spartatodoproject.entity.Todo;
import com.sparta.spartatodoproject.entity.User;
import com.sparta.spartatodoproject.exception.MismatchException;
import com.sparta.spartatodoproject.exception.TodoErrorCode;
import com.sparta.spartatodoproject.exception.NotFoundException;
import com.sparta.spartatodoproject.exception.UserErrorCode;
import com.sparta.spartatodoproject.jwt.JwtService;
import com.sparta.spartatodoproject.repository.TodoRepository;
import com.sparta.spartatodoproject.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class TodoService {

	private final TodoRepository todoRepository;
	private final UserRepository userRepository;
	final private StorageService storageService;

	//할일 생성
	public TodoResponseDto addTodo(TodoAddRequestDto requestDto, String username,
		MultipartFile file) throws IOException {
		//유저를 찾아와 일정과 같이 저장
		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new NotFoundException(UserErrorCode.USER_NOT_FOUND)
		);
		Todo todo = todoRepository.save(new Todo(requestDto, user));


		if (file != null) //파일이 있다면 저장
			storageService.uploadImage(file, todo);

		return new TodoResponseDto(todo);
	}

	//일정 아이디로 일정을 찾아 -> responseDto 반환
	public TodoResponseDto getTodo(long id) {
		Todo todo = todoRepository.findById(id).orElseThrow(
			() -> new NotFoundException(TodoErrorCode.TODO_NOT_FOUND));

		return new TodoResponseDto(todo);
	}

	//모든 일정을 -> responseDto 리스트로 반환
	public TodoListResponseDto getTodoList() {
		List<Todo> todoList = todoRepository.findAllByOrderByCreatedAtDesc();
		TodoListResponseDto todoListResponseDto = new TodoListResponseDto();
		for (Todo todo : todoList) {
			todoListResponseDto.getTodoResponseDtoList()
				.add(new TodoResponseDto(todo));
		}
		return todoListResponseDto;
	}

	@Transactional //변경 감지
	public TodoResponseDto updateTodo(TodoAddRequestDto requestDto, long id, String username, MultipartFile file) throws IOException {

		Todo todo = todoRepository.findById(id).orElseThrow(
			() -> new NotFoundException(TodoErrorCode.TODO_NOT_FOUND));

		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new NotFoundException(UserErrorCode.USER_NOT_FOUND)
		);

		if (!user.equals(todo.getUser())) //작성자만 수정 가능
			throw new MismatchException(TodoErrorCode.USER_MISMATCH);

		todo.update(requestDto);

		if(!file.isEmpty())
			storageService.update(file, todo);

		return new TodoResponseDto(todo);
	}

	public void deleteTodo(String username, Long id) {
		Todo todo = todoRepository.findById(id).orElseThrow(
			() -> new NotFoundException(TodoErrorCode.TODO_NOT_FOUND));

		User user = userRepository.findByUsername(username).orElseThrow(
			() -> new NotFoundException(UserErrorCode.USER_NOT_FOUND)
		);

		if(!user.equals(todo.getUser()))
			throw new MismatchException(TodoErrorCode.USER_MISMATCH);

		todoRepository.delete(todo);
	}
}
