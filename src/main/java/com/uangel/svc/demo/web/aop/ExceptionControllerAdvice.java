package com.uangel.svc.demo.web.aop;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(ExceptionRestControllerAdvice.ORDER + 1)
@ControllerAdvice
public class ExceptionControllerAdvice {

	// @ControllerAdvice 사용.
	// - 예외처리기 이외이 기능 사용
	
	// 1. @ModelAttribute 
	// @ModelAttribute를 모든 Controller에 사용한다면 @ControllerAdvice에 선언하면 된다.
	
	// 2. @InitBinder
	// Validator, Formatter, Converter, PropertyEditor 등, 뿐만 아니라 여러가지를 설정을 할 수 있는 어노테이션
	// Formatter, Converter 경우에는 WebMvcConfigurer 상속받아 설정하는 것이 더 편리해서 굳이 이 방법으로 글로벌하게 설정 할일은 드물 것 같다
	
	// 3. implements ResponseBodyAdvice, RequestBodyAdvice
	// ResponseBodyAdvice body에 쓰기전에 커스텀하게 변경가능
	// RequestBodyAdvice 경우에는 바디를 읽기전, 읽은 후 등에 커스텀하게 Body를 변경가능
	
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(Exception.class)
    public String handleException(Exception e, Model model, HttpServletRequest request) {
    	log.error("==================== ExceptionControllerAdvice @ExceptionHandler Exception");
    	
        log.error(e.getMessage(), e);
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("error", "BOARD_NOT_FOUND");
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("message", e.getMessage());
        return "/error/404";
    }
    
    @ExceptionHandler(Throwable.class)
    public String handleThrowable(Throwable e, Model model, HttpServletRequest request) {
    	log.error("==================== ExceptionControllerAdvice @ExceptionHandler Throwable");
    	
        log.error(e.getMessage(), e);
        model.addAttribute("timestamp", LocalDateTime.now());
        model.addAttribute("error", "BOARD_NOT_FOUND");
        model.addAttribute("path", request.getRequestURI());
        model.addAttribute("message", e.getMessage());
        return "/error/404";
    }
}