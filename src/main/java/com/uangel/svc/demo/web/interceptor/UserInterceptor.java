package com.uangel.svc.demo.web.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class UserInterceptor implements HandlerInterceptor {

	// Interceptor의 실행 시점은 Spring의 ServletDispatcher 내에 있다.
	// @ControllerAdvice에서 @ExceptionHandler를 사용해서 예외를 처리를 할 수 있다. 
	
	static final String className = UserInterceptor.class.getSimpleName();
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 컨트롤러의 메서드에 매핑된 특정 URI를 호출했을 때 컨트롤러에 접근하기 전에 실행.
		log.info("[{}] ==================== BEGIN ====================", className);
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// 컨트롤러를 경유한 다음, 화면(View)으로 결과를 전달하기 전에 실행되는 메서드
		// 핸들러 메서드에서 예외가 발생하면 호출 안됨
		log.info("[{}] ==================== END ======================", className);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		log.info("[{}] ==================== afterCompletion ======================", className);
	}

}
