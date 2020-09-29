package com.uangel.svc.demo.web.exception;

import java.util.Enumeration;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorViewResolver;
import org.springframework.boot.web.servlet.error.ErrorAttributes;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

// Controller 에서 에러처리하는 handler 가 없으면 /error 로 온다.
// ErrorAttributes 만 수정할 경우 
// CustomErrorController 는 굳이 없어도 된다. CustomErrorAttributes 만 정의해도 된다.
@Slf4j
@Controller
@RequestMapping("${server.error.path:${error.path:/error}}")
public class CustomErrorController extends BasicErrorController {

	public CustomErrorController(ErrorAttributes errorAttributes, ServerProperties serverProperties, List<ErrorViewResolver> errorViewResolvers) {
		super(errorAttributes, serverProperties.getError(), errorViewResolvers);
	}
	
	// HTML로 응답을 주는 경우 errorHtml에서 응답을 처리한다. 
	@RequestMapping(produces = MediaType.TEXT_HTML_VALUE)
    public ModelAndView errorHtml(HttpServletRequest request, HttpServletResponse response) {
		log.error("==================== CustomErrorController error HTML. status = {}", response.getStatus());
        return super.errorHtml(request, response);
    }

	// HTML 외의 응답이 필요한 경우 error에서 처리한다.
    @Override
    public ResponseEntity<Map<String, Object>> error(HttpServletRequest request) {
    	
    	Enumeration<String> es = request.getAttributeNames();
    	while(es.hasMoreElements()) {
    		String key = es.nextElement();
    		log.info("request.getAttribute. {} -> {}", key, request.getAttribute(key));
    	}
    	
    	log.info("getErrorPath = {}", getErrorPath());
    	
    	log.info("" + this.getErrorAttributes(request, getErrorAttributeOptions(request, MediaType.ALL)));
    	log.error("==================== CustomErrorController error none-HTML. status = {}", getStatus(request));
        return super.error(request);
    	
    	//request.setAttribute(RequestDispatcher.ERROR_STATUS_CODE, 500);
    	
    	//Map<String, Object> m = new HashMap<String, Object>();
    	
    	//return ResponseEntity.status(500).body(m);
    	
    	//return new ResponseEntity<>(m, HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
