package com.uangel.svc.demo.web.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Aspect
@Component
public class LoggingAspect {
	
	static final String className = LoggingAspect.class.getSimpleName();
	
    // RestController,Controller public Method
    @Around("within(@org.springframework.web.bind.annotation.RestController *)) && execution(public * *(..))")
    public Object controllerMethodLogging(ProceedingJoinPoint joinPoint) throws Throwable {
    	
    	//TestCode.makeNullPointException();
    	
        try {
        	log.info("[{}] === BEGIN", className);
//        	HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        	ContentCachingRequestWrapper wreq = new ContentCachingRequestWrapper(request);
//        	log.info("[{}] === REQUEST\n{}\n=== end request\n", className, new String(wreq.getContentAsByteArray()));
    		
            Object result = joinPoint.proceed();
            
//            HttpServletResponse response = ((ServletRequestAttributes)RequestContextHolder.currentRequestAttributes()).getResponse();
//            ContentCachingResponseWrapper wres = new ContentCachingResponseWrapper(response);
//            log.info("[{}] === RESPONSE\n{}\n=== end response\n", className, new String(wres.getContentAsByteArray()));
            //log.info("[{}] === RESPONSE\n{}\n=== end response\n", className, result);
            
            return result;
        } catch(Throwable e) {
        	log.info("[{}] === Throwable", className);
			// 메소드에서 정의하고 처리 가능한 예외에 대한 메세지 로깅
            throw e; // throw 후 ControllerAdvice 에서 처리. 정의되지 않은 Exception 은 error log 발생
        }
    }
}
