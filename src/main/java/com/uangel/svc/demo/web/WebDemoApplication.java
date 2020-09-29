package com.uangel.svc.demo.web;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;

@ServletComponentScan // @WebFilter, @WebListener, @WebServlet 지원.
@SpringBootApplication
public class WebDemoApplication {

	// Spring MVC
	// https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FJMiVh%2FbtqxPToLQPr%2FEftYdWW8JX0rpxG7xBBov0%2Fimg.png
	
	// DispatcherSeervlet과 WebApplicationContext
	// https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FEuPS4%2FbtqxQoWpNw0%2FxSkkdVaN5YWk9pl2sKFe6K%2Fimg.png
	
	public static void main(String[] args) {
		SpringApplication.run(WebDemoApplication.class, args);
	}

}
