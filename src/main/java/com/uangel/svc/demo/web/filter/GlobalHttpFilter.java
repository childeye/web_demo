package com.uangel.svc.demo.web.filter;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.uangel.svc.demo.web.test.TestCode;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class GlobalHttpFilter extends HttpFilter {

	private static final long serialVersionUID = -9132577622945552095L;
	
	static final String className = GlobalHttpFilter.class.getSimpleName();
	
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		// 웹 컨테이너(톰캣)이 시작될 때 필터 최초 한 번 인스턴스 생성
		super.init(filterConfig);
		log.info("[{}] HttpFilter.init()", className);
	}
	
	@Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
		log.info("[{}] HttpFilter.doFilter()", className);

		Map<String, String> requestMap = this.getTypesafeRequestMap(request);
		BufferedRequestWrapper bufferedRequest = new BufferedRequestWrapper(request);
		BufferedResponseWrapper bufferedResponse = new BufferedResponseWrapper(response);
		
		StringBuilder logMessage = new StringBuilder(
                "REST Request - ").append("[HTTP METHOD:")
                .append(request.getMethod())
                .append("] [PATH INFO:")
                .append(request.getServletPath())
                .append("] [REQUEST PARAMETERS:").append(requestMap)
                .append("] [REQUEST BODY:")
                .append(bufferedRequest.getRequestBody())
                .append("] [REMOTE ADDRESS:")
                .append(request.getRemoteAddr()).append("]");
		
		log.info(logMessage.toString());
		
		try {
			log.info("HttpFilter before doFilter.");
            chain.doFilter(bufferedRequest, bufferedResponse);
//            chain.doFilter(request, response);
            log.info("HttpFilter after doFilter.");

            StringBuilder logMessage2 = new StringBuilder(" [RESPONSE:").append(bufferedResponse.getContent()).append("]");
            log.info(logMessage2.toString());
            
            TestCode.makeNullPointException();
        } catch (Throwable t) {
            throw t;
        }
    }
	
	private Map<String, String> getTypesafeRequestMap(HttpServletRequest request) {
		Map<String, String> typesafeRequestMap = new HashMap<String, String>();
		Enumeration<?> requestParamNames = request.getParameterNames();
		while (requestParamNames.hasMoreElements()) {
			String requestParamName = (String) requestParamNames.nextElement();
			String requestParamValue;
			if (requestParamName.equalsIgnoreCase("password")) {
				requestParamValue = "********";
			} else {
				requestParamValue = request.getParameter(requestParamName);
			}
			typesafeRequestMap.put(requestParamName, requestParamValue);
		}
		return typesafeRequestMap;
	}
	
}
