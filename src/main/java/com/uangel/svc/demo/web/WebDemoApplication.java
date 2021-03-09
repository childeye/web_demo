package com.uangel.svc.demo.web;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.context.ApplicationContext;

import com.uangel.svc.demo.web.test.WebClientTest;

@ServletComponentScan // @WebFilter, @WebListener, @WebServlet 지원.
@SpringBootApplication
public class WebDemoApplication {

	private static ApplicationContext context;
	
	// Spring MVC
	// https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FJMiVh%2FbtqxPToLQPr%2FEftYdWW8JX0rpxG7xBBov0%2Fimg.png
	
	// DispatcherSeervlet과 WebApplicationContext
	// https://img1.daumcdn.net/thumb/R1280x0/?scode=mtistory2&fname=https%3A%2F%2Fblog.kakaocdn.net%2Fdn%2FEuPS4%2FbtqxQoWpNw0%2FxSkkdVaN5YWk9pl2sKFe6K%2Fimg.png
	
	public static void main(String[] args) {
		context = SpringApplication.run(WebDemoApplication.class, args);
		WebClientTest webClientTest = context.getBean(WebClientTest.class);
		//webClientTest.test();
		
		ExecutorService ex = Executors.newCachedThreadPool();
		
		ex.execute(() -> {webClientTest.testAsyncUser("/async/callable");});
		ex.execute(() -> {webClientTest.testAsyncUser("/async/callable");});
		
		ex.execute(() -> {webClientTest.testAsyncUser("/async/completable");});
		ex.execute(() -> {webClientTest.testAsyncUser("/async/completable");});
		
		ex.execute(() -> {webClientTest.testAsyncUser("/async/webAsyncTask");});
		ex.execute(() -> {webClientTest.testAsyncUser("/async/webAsyncTask");});
		
		ex.execute(() -> {webClientTest.testAsyncUser("/async/webAsyncTaskTimeout");});
		ex.execute(() -> {webClientTest.testAsyncUser("/async/webAsyncTaskTimeout");});
		
		ex.execute(() -> {webClientTest.testAsyncUser("/async/deferred");});
		ex.execute(() -> {webClientTest.testAsyncUser("/async/deferred");});
	}
	
}
