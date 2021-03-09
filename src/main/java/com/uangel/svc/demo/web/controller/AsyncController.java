package com.uangel.svc.demo.web.controller;

import java.util.List;
import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicInteger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.async.DeferredResult;
import org.springframework.web.context.request.async.WebAsyncTask;

import com.uangel.svc.demo.web.model.User;
import com.uangel.svc.demo.web.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/async")
public class AsyncController {
	
	private final Queue<DeferredResult<List<User>>> userQueue = new ConcurrentLinkedQueue<>();
	private static AtomicInteger count = new AtomicInteger(0);
	
	@Autowired
	UserService userService;
	
	//@Autowired
	//AsyncRestTemplate asyncRestTemplate;
	
	// 타임아웃이라던지 어떤 스레드풀을 사용할지 결정할 수 없다. java 1.5+
	@GetMapping("/callable")
	public Callable<List<User>> callable() {
		log.info("/callable, REQ");
		return () -> {
			log.info("/callable, RES");
			checkCount();
			return userService.getAll();
		};
	}
	
	// Executor를 추가로 파라미터로 받고 있다. 그래서 좀 더 나은 설정을 할 수 있다. java 1.8+
	@GetMapping("/completable")
	public CompletableFuture<List<User>> completable() {
		log.info("/completable, REQ");
		return CompletableFuture.supplyAsync(() -> {
			log.info("/completable, RES");
			checkCount();
			return userService.getAll();
		});
	}
	
	// spring 4.0+
	// AsyncRestTemplate의 리턴타입은 ListenableFuture로 정의되어있다.
	// 비동기 적으로 특정한 API를 호출할 때 유용한 AsyncRestTemplate은 리턴 타입 그대로 spring mvc에게 넘겨줘도 된다.
	@GetMapping("/listenable")
	public ListenableFuture<ResponseEntity<List<User>>> listenable() {
		return null;
//	  return asyncRestTemplate.exchange("http://localhost:8081/users",
//	      HttpMethod.GET,
//	      null,
//	      new ParameterizedTypeReference<List<User>>() {});
	}
	
	// spring 3.2+
	// WebAsyncTask 클래스 안에는 Callable을 사용하고 있으며 타임아웃 설정, executor 등을 설정 할 수 있다.
	@GetMapping("/webAsyncTask")
	public WebAsyncTask<List<User>> webAsyncTask() {
		log.info("/webAsyncTask, REQ");
		return new WebAsyncTask<>(() -> {
			log.info("/webAsyncTask, RES");
			checkCount();
			return userService.getAll();
		});
	}
	
	@GetMapping("/webAsyncTaskTimeout")
	public WebAsyncTask<List<User>> webAsyncTaskTimeout() {
		log.info("/webAsyncTask, REQ");
		return new WebAsyncTask<>(2000L, new ThreadPoolTaskExecutor(), () -> {
			log.info("/webAsyncTask, RES");
			checkCount();
			return userService.getAll();
		});
	}
	
	
	// spring 3.2+
	// DeferredResult를 생성해서 큐에 담고 바로 리턴
	@GetMapping("/deferred")
	public DeferredResult<List<User>> deferred() {
		log.info("/deferred, REQ");
		DeferredResult<List<User>> result = new DeferredResult<>();
		userQueue.add(result);
		return result;
	}

	// 다른스레드에서 setResult를 호출하면 그때 뷰에 전달
	@Scheduled(fixedRate = 2000)
	public void processQueues() {
		for (DeferredResult<List<User>> result : this.userQueue) {
			log.info("/deferred, RES");
			checkCount();
			result.setResult(userService.getAll());
			this.userQueue.remove(result);
		}
	}
	
	private void checkCount() {
		int cnt = count.incrementAndGet();
		log.info("checkCount = {}", cnt);
		if(cnt % 2 != 0) {
			try {
				Thread.sleep(2000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
}
