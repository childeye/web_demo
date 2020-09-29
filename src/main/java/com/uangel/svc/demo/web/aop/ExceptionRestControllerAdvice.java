package com.uangel.svc.demo.web.aop;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.annotation.Order;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springframework.web.util.WebUtils;

import com.uangel.svc.demo.web.exception.UserException;
import com.uangel.svc.demo.web.exception.UserRuntimeException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Order(ExceptionRestControllerAdvice.ORDER)
@RestControllerAdvice(annotations = RestController.class) // 예외처리를 body로 전달하고 싶다면 @RestControllerAdvice 이용
public class ExceptionRestControllerAdvice extends ResponseEntityExceptionHandler {

	
	/*
	
	https://supawer0728.github.io/2019/04/04/spring-error-handling/
	
	Spring MVC 내에서는 @HandlerException을 통해 각 @Controller별로 예외 처리를 할 수 있었으며, @HandlerException을 @ControllerAdvice에 등록하여 전역적으로 예외를 처리할 수도 있다.
	브라우저 요청(GlobalHtmlControllerAdvice)과 REST API의 요청(@RestControllerAdvice)을 나누어서 @ControllerAdvice에서 처리할 수 있다.
	
	이러한 기본 동작들은 HandlerExceptionResolver에 의해 이루어진다.
	 - ExceptionHandlerExceptionResolver : @ExceptionHandler가 붙은 메서드를 통해 예외 처리를 할 수 있도록 설정하는 클래스
	 - SimpleMappingExceptionResolver : 예외 이름과 view 이름을 매핑해주며, browser 요청을 view로 렌더링할 때 유용하게 쓸 수 있다
	 - ResponseStatusExceptionResolver : Exception 클래스에 @ResponseStatus를 달아 해당 응답 코드로 응답을 보낼 수 있도록 설정하는 클래스
	 - DefaultHandlerExceptionResolver : Spring MVC Exception에 대해 기본적인 처리를 해주는 클래스
	
	Spring MVC 내에서 처리하지 못한 예외들은 ServletException으로 포장되어 서블릿 컨테이너까지 전파되며, 서블릿 컨테이너는 예외를 처리하기 위해나 경로로 예외 처리를 위임하게 된다.
	이때 Spring boot를 기본설정으로 사용하는 경우, BasicErrorController가 이를 담당하게 된다.
	 - 400 BAD REQUEST 같은 경우.
	  - 각종 Filter 와 DispatcherServlet, Interceptor 까지 처리 후 Controller 에 진입하다가 에러가 발생, AOP 수행하지 않음.
	  - @RestControllerAdvice 에서 처리. handleExceptionInternal 호출 됨.
	  - Filter before -> DispatcherServlet before -> Interceptor before -> 메시지 변환 에러(400 BAD REQUEST) 
	  - RestControllerAdvice 에서 에러 처리(Response 생성). AOP(X), Controller(X) ->  Interceptor after -> DispatcherServlet after -> Filter after
	  
	- 404 NOT FOUND 같은 경우.
	  - 각종 Filter 와 DispatcherServlet, Interceptor 까지 처리 후 handler 매핑 실패. BasicErrorController 처리.(/error DispatcherServlet, Interceptor 호출 됨)
	  - Filter before -> DispatcherServlet before -> Interceptor before -> handler 매핑 실패. -> response.sendError() 처리. -> Interceptor after -> DispatcherServlet after -> Filter after
	  - /error 처리. DispatcherServlet before -> Interceptor before -> BasicErrorController 에서 에러 처리(DefaultErrorAttributes 생성). ->  Interceptor after -> DispatcherServlet after
	  
	- 500 INTERNAL SERVER ERROR 같은 경우.
	  - 1. DispatcherServlet 에서 HttpRequestMethodNotSupportedException 발생(정의되어 있지 않은 Method.)
	  - 각종 Filter 처리 후 DispatcherServlet 에서 handler 매핑 실패. ExceptionControllerAdvice 에서 처리.
	  - Filter before -> DispatcherServlet before. handler 매핑 실패. HttpRequestMethodNotSupportedException 발생. -> Filter after 처리 안됨.
	  - /error 처리. DispatcherServlet before -> Interceptor before -> BasicErrorController 에서 에러 처리(DefaultErrorAttributes 생성). ->  Interceptor after -> DispatcherServlet after
	
	- Filter 에서 Exception 발생.
	  - 각종 Filter 처리 중 Exception 발생. BasicErrorController 처리.(/error DispatcherServlet, Interceptor 호출 됨)
	  - Filter before -> Exception 발생. -> Filter after 처리 안됨.
	  - /error 처리. DispatcherServlet before -> Interceptor before -> BasicErrorController 에서 에러 처리(DefaultErrorAttributes 생성). ->  Interceptor after -> DispatcherServlet after
	  
	- DispatcherServlet 에서 Exception 발생.
	  - 각종 Filter 처리 후 DispatcherServlet 에서 Exception 발생. BasicErrorController 처리.(/error DispatcherServlet, Interceptor 호출 됨)
	  - Filter before -> DispatcherServlet before -> Exception 발생. -> Filter after 처리 안됨.
	  - /error 처리. DispatcherServlet before -> Interceptor before -> BasicErrorController 에서 에러 처리(DefaultErrorAttributes 생성). ->  Interceptor after -> DispatcherServlet after
	
	- Interceptor 에서 Exception 발생.
	  - 각종 Filter, DispatcherServlet 처리 후 Interceptor 에서 Exception 발생. ControllerAdvice 에서 처리.
	  - Filter before -> DispatcherServlet before -> Interceptor before -> Exception 발생.
	  - RestControllerAdvice 에서 에러 처리(Response 생성). AOP(X), Controller(X) ->  Interceptor after -> DispatcherServlet after -> Filter after
	  
	- AOP 에서 Exception 발생.
	  - 각종 Filter, DispatcherServlet, Interceptor 처리 후 AOP 에서 Exception 발생. ControllerAdvice 에서 처리.
	  - Filter before -> DispatcherServlet before -> Interceptor before -> AOP -> Exception 발생.
	  - RestControllerAdvice 에서 에러 처리(Response 생성). Controller(X) ->  Interceptor after -> DispatcherServlet after -> Filter after
	  
	- Controller 에서 Exception 발생.
	  - 각종 Filter, DispatcherServlet, Interceptor, AOP 처리 후 Controller 에서 Exception 발생. ControllerAdvice 에서 처리.
	  - Filter before -> DispatcherServlet before -> Interceptor before -> AOP before -> Controller -> Exception 발생.
	  - RestControllerAdvice 에서 에러 처리(Response 생성). ->  AOP Exception 처리(throw) -> Interceptor after -> DispatcherServlet after -> Filter after
	  
	 
	- Interceptor after 에서 Exception 발생.(request, response 객체 사용하다가 Exception 발생한 것이 아님.)
	  - 로그만 출력 됨. 정상 처리.
	  
	  
	- Filter after 에서 Exception 발생.(request, response 객체 사용하다가 Exception 발생한 것이 아님.)
	  - 로그 출력. 이후 Filter 수행 안됨. 결과는 정상적으로 출력.
	  - Filter(1,2,3) before -> DispatcherServlet before -> Interceptor before -> AOP before -> Controller -> Filter3 after -> Filter2 after -> Exception 발생.
	  - 동일한 url 로 다시 DispatcherServlet, Global Interceptor before -> BasicErrorController 에서 에러 처리(DefaultErrorAttributes 생성). ->  Interceptor after -> DispatcherServlet after
	  - DispatcherServlet 에서 Exception 을 catch 해서 처리하는 것 같음. 그런데 결과가 바뀌지는 않음.
	  
	  
	
	Interceptor는 DispatcherServlet 내부에서 발생하기 때문에 ControllerAdvice를 적용할 수 있다.
	Filter는 DispatcherServlet 외부에서 발생해서 ErrorController에서 처리해야 한다.
	
	*/
	
	
	// HTML view를 사용할 경우와 json view를 사용할 경우를 나누어 ControllerAdivce를 등록하고, @Order를 사용하여 우선 순위를 부여하면 분기처리 없이 나누어 오류 처리를 할 수 있다.
	public static final int ORDER = 0;
	
	// UserController 에서 handler 가 정의 되어 있어서 호출되지 않음.
	@ResponseStatus(HttpStatus.SERVICE_UNAVAILABLE)
	@ExceptionHandler(UserException.class)
	public Map<String, Object> userExceptionHandler(UserException e, HttpServletRequest request) {
		log.error("==================== ExceptionRestControllerAdvice @ExceptionHandler UserException");
		
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("code", e.getCode());
		errorMap.put("message", e.getMessage());
		
		return errorMap;
	}
	
	
	// @ResponseStatus 이 정의된 Exception. class 에서 미리 @ResponseStatus 을 정의 할 수 있음. 이거는 안 먹힘. --??
	@ResponseStatus(HttpStatus.BAD_GATEWAY)
	@ExceptionHandler(UserRuntimeException.class)
	public Map<String, Object> userRuntimeExceptionHandler(UserRuntimeException e, HttpServletRequest request) {
		log.error("==================== ExceptionRestControllerAdvice @ExceptionHandler UserRuntimeException");
		
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("code", e.getCode());
		errorMap.put("message", e.getMessage());
		
		return errorMap;
	}
	
	// 그 외 모든 Exception
	@ExceptionHandler(Exception.class)
	public ResponseEntity<?> exceptionHandler(Exception e, HttpServletRequest request) {
		log.error("==================== ExceptionRestControllerAdvice @ExceptionHandler Exception");
		e.printStackTrace();
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
	}
	
	
	
	
	
	// ResponseEntityExceptionHandler 을 상속해서 사용할 경우 미리 정의되어 있는 특정 Exception 을 override 해서 사용할 수 있다.
	@Override
	protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
		return super.handleHttpMessageNotReadable(ex, headers, status, request);
	}
	
	// ResponseEntityExceptionHandler 에 정의된 모든 Exception 처리.
	@Override
	protected ResponseEntity<Object> handleExceptionInternal(Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {
		log.error("==================== ExceptionRestControllerAdvice handleExceptionInternal. body = {}, message = {}", body, ex.getMessage());
		
		if (HttpStatus.INTERNAL_SERVER_ERROR.equals(status)) {
			request.setAttribute(WebUtils.ERROR_EXCEPTION_ATTRIBUTE, ex, WebRequest.SCOPE_REQUEST);
		}
		
		String strBody = null;
		//if(body == null) strBody = ex.getMessage();
		if(body == null) strBody = status.name();
		
		return new ResponseEntity<>(strBody, headers, status);
	}

}
