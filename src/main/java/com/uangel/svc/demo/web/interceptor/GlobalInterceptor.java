package com.uangel.svc.demo.web.interceptor;

import java.util.Enumeration;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class GlobalInterceptor extends HandlerInterceptorAdapter {

	// 요청이 들어오면 DispatcherServlet에서 요청을 처리.
	// DispatcherServlet의 doDispatch() 메소드에서 getHandler() 메소드로 HandlerExecutionChain를 호출.(RequestMappingHandlerAdapter의 HandlerExecutionChain)
	// getHandler() 메소드 내부에는 getHandlerInternal() 메소드로 handler를 가져오는 부분이 있다. 이 부분이 바로 요청 URL과 매칭하는 Controller 메소드를 찾아내는 부분
	// getHandlerExecutionChain() 메소드에서 요청 메소드의 URL에 대해 이미 등록 된 interceptor 들의 url-pattern들과 매칭 되는 interceptor 리스트를 추출
	// 추출 된 interceptor들에 대해 preHandle() 메소드를 실행(preHandle() 메소드의 리턴 타입은 boolean인데 false가 리턴 되는 경우에는 Controller 메소드를 실행 하지 않는다)
	// 요청 URL에 맞는 Controller 메소드를 실행
	// 메소드 작업이 끝난 뒤 추출 된 interceptor들에 대해 postHandle() 메소드를 실행
	
	
	static final String LINE_FEED = System.lineSeparator();
	static final String className = GlobalInterceptor.class.getSimpleName();
	
	// handler = @RequestMapping 선언으로 요청에 대한 HandlerMethod(@Controller의 메서드)
	// Interceptor의 실행 시점은 Spring의 ServletDispatcher 내에 있다.
	// @ControllerAdvice에서 @ExceptionHandler를 사용해서 예외를 처리를 할 수 있다. 
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		// 컨트롤러의 메서드에 매핑된 특정 URI를 호출했을 때 컨트롤러에 접근하기 전에 실행.
		log.info("[{}] ==================== preHandle ====================", className);
		// ContentCachingRequestWrapper requestCacheWrapperObject = new ContentCachingRequestWrapper(request);
		
		//TestCode.makeNullPointException();
		
		request(request);
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
		// 컨트롤러를 경유한 다음, 화면(View)으로 결과를 전달하기 전에 실행되는 메서드
		// 핸들러 메서드에서 예외가 발생하면 호출 안됨
		log.info("[{}] ==================== postHandle ======================", className);
		super.postHandle(request, response, handler, modelAndView);
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
		// 뷰까지 끝고난 후 실행
		// 예외가 발생해도 호출됨.
		log.info("[{}] ==================== afterCompletion ======================", className);
		super.afterCompletion(request, response, handler, ex);
	}

	@Override
	public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		log.info("[{}] ==================== afterConcurrentHandlingStarted ======================", className);
		super.afterConcurrentHandlingStarted(request, response, handler);
	}
	
	private void request(final HttpServletRequest request) throws Exception {
		StringBuilder sb = new StringBuilder();

		//String current = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss").format(new Date());
		sb.append(LINE_FEED).append(String.format("%s %s %s", request.getMethod(), request.getRequestURI(), request.getProtocol()));
		sb.append(LINE_FEED).append(String.format("host: %s", request.getHeader("host")));
		Enumeration<String> headerNames = request.getHeaderNames();
		while(headerNames.hasMoreElements()) {
			String h = headerNames.nextElement();
			if(!h.equals("host")) {
				sb.append(LINE_FEED).append(String.format("%s: %s", h, request.getHeader(h)));
			}
		}
		sb.append(LINE_FEED);
		
		log.info("[{}] {}", className, sb.toString());
	}

}
