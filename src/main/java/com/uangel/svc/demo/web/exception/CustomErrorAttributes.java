package com.uangel.svc.demo.web.exception;

import java.util.Arrays;
import java.util.Map;

import org.springframework.boot.web.error.ErrorAttributeOptions;
import org.springframework.boot.web.servlet.error.DefaultErrorAttributes;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.WebRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomErrorAttributes extends DefaultErrorAttributes {

	// BasicErrorController 에서 사용하는 ErrorAttributes 정의.
	// HTML 형태의 response 가 아닌 Error.
	@Override
    public Map<String, Object> getErrorAttributes(WebRequest webRequest, ErrorAttributeOptions options) {
		log.error("==================== CustomErrorAttributes error");
		
		String[] s = webRequest.getAttributeNames(LOWEST_PRECEDENCE);
		Arrays.asList(s).stream().forEach(a -> {
			log.info("CustomErrorAttributes. {} -> {}", a, webRequest.getAttribute(a, LOWEST_PRECEDENCE));
		});
		
        Map<String, Object> result = super.getErrorAttributes(webRequest, options);
        result.put("greeting", "Hello");
        return result;
    }
	
}
