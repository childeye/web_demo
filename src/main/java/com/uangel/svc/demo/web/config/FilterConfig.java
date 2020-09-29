package com.uangel.svc.demo.web.config;

import javax.servlet.Filter;

import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.CommonsRequestLoggingFilter;

import com.uangel.svc.demo.web.filter.GlobalHttpFilter;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class FilterConfig {

	static final String className = FilterConfig.class.getSimpleName();
	
	// spring 에서 기본 제공하는 filter.
	// request 로그만 출력.
	// Exception 처리 하지 못함.
	@Bean
	public CommonsRequestLoggingFilter logFilter() {
		log.info("[{}] logFilter()", className);
		
		CommonsRequestLoggingFilter requestLoggingFilter = new CommonsRequestLoggingFilter();
		requestLoggingFilter.setIncludeQueryString(true);
		requestLoggingFilter.setIncludePayload(true);
		requestLoggingFilter.setMaxPayloadLength(10000);
		requestLoggingFilter.setIncludeHeaders(false);
		requestLoggingFilter.setAfterMessagePrefix("[CommonsRequestLoggingFilter] REQUEST DATA : ");
		
		return requestLoggingFilter;
	}
	
    @Bean
	public FilterRegistrationBean<Filter> setFilterRegistration() {
    	log.info("[{}] setFilterRegistration()", className);
    	
		FilterRegistrationBean<Filter> filterRegistrationBean = new FilterRegistrationBean<Filter>(new GlobalHttpFilter());
		//filterRegistrationBean.addUrlPatterns("/user/*"); // string 여러개를 가변인자로 받는 메소드
		filterRegistrationBean.setName("HTTPLOGGING");
		//filterRegistrationBean.setOrder(0);
		return filterRegistrationBean;
    }
}
