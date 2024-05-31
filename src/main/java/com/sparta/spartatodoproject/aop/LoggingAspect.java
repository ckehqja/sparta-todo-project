package com.sparta.spartatodoproject.aop;

import java.lang.reflect.Method;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import com.sparta.spartatodoproject.dto.LoginRequestDto;
import com.sparta.spartatodoproject.entity.LoginHistory;
import com.sparta.spartatodoproject.entity.LoginStatus;
import com.sparta.spartatodoproject.repository.LoginHistoryRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Aspect
@Slf4j
@RequiredArgsConstructor
@Component
public class LoggingAspect {

	private final LoginHistoryRepository loginHistoryRepository;

	// login 메서드에 적용
	@Pointcut("execution(* com.sparta.spartatodoproject.service.UserService.login(..))")
	private void cut() {
	}

	// Pointcut에 의해 필터링된 경로로 들어오는 경우 메서드 호출 전에 적용
	@Before("cut()")
	public void beforeParameterLog(JoinPoint joinPoint) {
		// 메서드 정보 받아오기
		Method method = getMethod(joinPoint);
		log.info("======= beforeParameterLog method name = {} =======", method.getName());

		LoginRequestDto loginRequestDto = getLoginRequestDto(joinPoint);
		loginHistoryRepository.save(new LoginHistory(loginRequestDto.getUsername(), LoginStatus.BEFORE));
	}

	@AfterReturning("cut()")
	public void afterReturning(JoinPoint joinPoint) {
		Method method = getMethod(joinPoint);
		log.info("======= AfterReturning method name = {} =======", method.getName());

		LoginRequestDto loginRequestDto = getLoginRequestDto(joinPoint);
		loginHistoryRepository.save(new LoginHistory(loginRequestDto.getUsername(), LoginStatus.AFTER_RETURNING));
	}

	@AfterThrowing("cut()")
	public void afterThrowing(JoinPoint joinPoint) {
		Method method = getMethod(joinPoint);
		log.info("======= AfterThrowing method name = {} =======", method.getName());

		LoginRequestDto loginRequestDto = getLoginRequestDto(joinPoint);
		loginHistoryRepository.save(new LoginHistory(loginRequestDto.getUsername(), LoginStatus.AFTER_THROWING));

	}

	// JoinPoint로 메서드 정보 가져오기
	private Method getMethod(JoinPoint joinPoint) {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		return signature.getMethod();
	}

	private static LoginRequestDto getLoginRequestDto(JoinPoint joinPoint) {
		LoginRequestDto loginRequestDto = new LoginRequestDto();
		// 파라미터 받아오기
		Object[] args = joinPoint.getArgs();
		if (args.length <= 0)
			log.info("no parameter");
		for (Object arg : args) {
			loginRequestDto = (LoginRequestDto)arg;
			log.info("parameter type = {}", arg.getClass().getSimpleName());
			log.info("parameter value = {}", arg);
		}
		return loginRequestDto;
	}
}
