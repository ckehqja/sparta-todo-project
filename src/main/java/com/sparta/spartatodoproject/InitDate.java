// package com.sparta.spartatodoproject;
//
// import java.util.ArrayList;
// import java.util.List;
//
// import org.springframework.stereotype.Component;
//
// import com.sparta.spartatodoproject.entity.Comment;
// import com.sparta.spartatodoproject.entity.Todo;
// import com.sparta.spartatodoproject.entity.User;
// import com.sparta.spartatodoproject.entity.UserRoleEnum;
// import com.sparta.spartatodoproject.repository.CommentRepository;
// import com.sparta.spartatodoproject.repository.TodoRepository;
// import com.sparta.spartatodoproject.repository.UserRepository;
//
// import jakarta.annotation.PostConstruct;
// import lombok.RequiredArgsConstructor;
//
// @RequiredArgsConstructor
// @Component
// public class InitDate {
// 	private final UserRepository userRepository;
// 	private final TodoRepository todoRepository;
// 	private final CommentRepository commentRepository;
//
// 	@PostConstruct
// 	public void init() {
// 		User usera = new User("usera", "usera", "usera", UserRoleEnum.USER);
// 		User userb = new User("userb", "userb", "userb", UserRoleEnum.USER);
// 		User userc = new User("userc", "userc", "userc", UserRoleEnum.USER);
// 		User user = new User("user", "user", "user", UserRoleEnum.ADMIN);
//
// 		userRepository.saveAll(new ArrayList<>(List.of(
// 			usera, userb, userc, user
// 		)));
//
// 		Todo todo1 = new Todo("jpa", "jpa 완강하기 ", usera);
// 		Todo todo2 = new Todo("til", "til 작성하기 ", usera);
// 		Todo todo3 = new Todo("sql", "sql 마스터하기 ", usera);
//
// 		Todo todo4 = new Todo("jpa", "jpa 완강하기 ", userb);
// 		Todo todo5 = new Todo("til", "til 작성하기 ", userb);
// 		Todo todo6 = new Todo("spring mvc", "spring mvc 완강하기 ", userb);
// 		Todo todo7 = new Todo("개인 과제 제출", "빨리 강의 다 듣고 개인과제 다하고 제출하기 ", userb);
//
// 		Todo todo8 = new Todo("sql", "sql 마스터하기 ", userc);
// 		Todo todo9 = new Todo("운동", "일찍 일어나서 운동가기", userc);
// 		Todo todo0 = new Todo("서울", "5시 ktx 타고 서울 놀러가기", userc);
// 		Todo todo11 = new Todo("숨", "숨 쉬기 운동 열심히 하기", userc);
//
// 		todoRepository.saveAll(new ArrayList<>(List.of(
// 			todo1, todo2, todo3, todo4, todo5, todo6, todo7, todo8, todo9,
// 			todo0, todo11
// 		)));
//
// 		commentRepository.saveAll(new ArrayList<>(List.of(
// 			new Comment("우와 부지런하다 ㄷㄷ", todo1, usera),
// 			new Comment("우와 1 ㄷㄷ", todo1, usera),
// 			new Comment("우와 2 ㄷㄷ", todo1, usera),
// 			new Comment("우와 3 ㄷㄷ", todo1, usera),
// 			new Comment("1 부지런하다 ㄷㄷ", todo1, usera),
// 			new Comment("2 부지런하다 ㄷㄷ", todo1, usera),
// 			new Comment("3 부지런하다 ㄷㄷ", todo1, usera),
// 			new Comment("4 부지런하다 ㄷㄷ", todo1, usera),
// 			new Comment("우와 5 ㄷㄷ", todo1, usera),
// 			new Comment("우와 부지런하다 1", todo1, usera),
//
// 			new Comment("ads 부지런하다 ㄷㄷ", todo11, userb),
// 			new Comment("우와 ads ㄷㄷ", todo4, userb),
// 			new Comment("zz", todo7, userb),
// 			new Comment("fuck xx", todo5, userb),
// 			new Comment("ㅇㅈ", todo9, userb),
// 			new Comment("ㅎㅎ", todo3, userb),
// 			new Comment("ㅌㅌ", todo2, userb),
// 			new Comment("우와 부지런하다 ㄷㄷ", todo5, userb),
// 			new Comment("ㅌㅌ ㄷㄷ", todo8, userb),
// 			new Comment("우와 ㅌㅌ ㄷㄷ", todo7, userb),
// 			new Comment("ㅌㅌ 부지런하다 ㄷㄷ", todo0, userb),
//
// 			new Comment(":)", todo1, userc),
// 			new Comment("우와 부지런하다 ㄷㄷ", todo0, userc),
// 			new Comment("sKkkkkrrrr", todo2, userc),
// 			new Comment("nnn0", todo3, userc),
// 			new Comment("우와  ㄷㄷ", todo4, userc),
// 			new Comment("a;dfj;alsdkfj;adlskfj;klsdj", todo5, userc),
// 			new Comment("우와  ㄷㄷ", todo6, userc),
// 			new Comment(":>", todo7, userc),
// 			new Comment(":p", todo8, userc),
// 			new Comment("굿", todo9, userc),
// 			new Comment(" 부지런하다 ㄷㄷ", todo7, userc),
// 			new Comment("우와  ㄷㄷ", todo8, userc)
// 		)));
//
// 	}
// }
