package com.uangel.svc.demo.web.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.uangel.svc.demo.web.interceptor.GlobalInterceptor;
import com.uangel.svc.demo.web.interceptor.UserInterceptor;

@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
	
	static final String className = WebMvcConfig.class.getSimpleName();

	@Autowired
	private UserInterceptor userInterceptor;
	
	@Autowired
	private GlobalInterceptor globalInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(globalInterceptor)
			.addPathPatterns("/**")
			.excludePathPatterns("/css/**", "/plugins/**", "/scripts/**");
		
		registry.addInterceptor(userInterceptor).addPathPatterns("/user");
	}
	
}
