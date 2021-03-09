package com.uangel.svc.demo.web.test;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.uangel.svc.demo.web.model.User;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebClientTest {
	
	// https://medium.com/@odysseymoon/spring-webclient-%EC%82%AC%EC%9A%A9%EB%B2%95-5f92d295edc0
	private final WebClient webClient;
	
	//@PostConstruct
	public void test() {
		log.info("Test WebClient.");
		testResponse();
		
//		testPost();
//		testGet();
//		testGetAsync();
//		testSynchronous();
//		testError();
	}
	
	// WebClient 는 기존 설정값을 상속해서 사용할 수 있는 mutate() 함수를 제공하고 있습니다. 
	// mutate() 를 통해 builder() 를 다시 생성하여 추가적인 옵션을 설정하여 재사용이 가능하기 때문에 @Bean 으로 등록한 WebClient는 각 Component 에서 의존주입하여 mutate()를 통해 사용 하는 것이 좋습니다.
	void testBuilder() {
		WebClient a = WebClient.builder().baseUrl("https://some.com").build();
		WebClient b = a.mutate().defaultHeader("user-agent", "WebClient").build();
		WebClient c = b.mutate().defaultHeader(HttpHeaders.AUTHORIZATION, "token").build();
	}
	
	void testResponse() {
		// HTTP 호출 결과를 가져오는 두 가지 방법으로 retrieve() 와 exchange() 가 존재합니다. 
		// retrieve 를 이용하면 바로 ResponseBody를 처리 할 수 있고, exchange 를 이용하면 세세한 컨트롤이 가능합니다. 
		// 하지만 Spring에서는 exchange 를 이용하게 되면 Response 컨텐츠에 대한 모든 처리를 직접 하면서 발생할 수 있는 memory leak 가능성 때문에 가급적 retrieve 를 사용하기를 권고하고 있습니다.
		
		
		// retrieve()
		// - Perform the HTTP request and retrieve the response body.
		// - This method is a shortcut to using exchange() and decoding the response body through ClientResponse.
		
		// exchange()
		//  - Perform the HTTP request and return a ClientResponse with the response status and headers. You can then use methods of the response to consume the body
		/*
		Mono<User> resultr = webClient.get()
                .uri("/user/{id}", "1")
                .accept(MediaType.APPLICATION_JSON) 
                .retrieve() 
                .bodyToMono(User.class);
		
		resultr.subscribe(result -> {
		      log.info("{}", result);
		    }, e -> {
		    	log.error("{}", e.getMessage());
		    });
		    */
		
		Mono<User> resulte = webClient.get()
                .uri("http://localhost:8080/4040")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .doOnError(e -> {
                	// connection error, read timeout error 등.
                	log.error("doOnError {}", e.getMessage());
                })
                .flatMap(response -> {
                	log.info("flatMap. status = {}", response.statusCode());
                	return response.bodyToMono(User.class);
                });
		
		resulte.subscribe(result -> {
		      log.info("testResponse {}");
		    }, e -> {
		    	log.error("subscribe {}", e.getMessage());
		    });
	}
	
	// HTTP 응답 코드가 4xx 또는 5xx로 내려올 경우 WebClient 에서는 WebClientResponseException이 발생하게 됩니다. 
	// 이 때 각 상태코드에 따라 임의의 처리를 하거나 Exception 을 랩핑하고 싶을 때는 onStatus() 함수를 사용하여 해결 할 수 있습니다.
	void testError() {
		try {
			webClient.mutate()
	        .baseUrl("http://localhost:8080")
	        .build()
	        .get()
	        .uri("/notfound")
	        .accept(MediaType.APPLICATION_JSON)
	        .retrieve()
	        .onStatus(status -> status.is4xxClientError() 
	                         || status.is5xxServerError()
	            , clientResponse ->
	                          clientResponse.bodyToMono(String.class)
	                          .map(body -> new RuntimeException(body)))
	        .bodyToMono(User.class)
	        .block();
		} catch(Exception e) {
			log.warn("Exception testError.", e);
		}
	}
	
	// GET 호출은 앞서 보여진 예시에서 처럼 get() 함수를 통해 사용되며 uri() 를 통해 호출 리소스 정보를 전달해 줘야 합니다. 만약 Query 파라미터가 존재한다면 다음과 같이 변수를 추가 줄 수 있습니다.
	// uri() 함수를 제공하는 UriSpec 인터페이스는 아래와 같으며, Map 을 이용하거나 직접 UriBuilder 등을 통해 세세한 컨트롤도 가능합니다.
	void testGet() {
		try {
			Mono<User> user = webClient.mutate()
	        .baseUrl("http://localhost:8080")
	        .build()
	        .get()
	        .uri("/user/{ID}", "1")
	        .accept(MediaType.APPLICATION_JSON)
	//        .header(HttpHeaders.AUTHORIZATION, "Bearer accessToken")
	        .retrieve()
	        .bodyToMono(User.class);
			
			log.info("testGet = {}", user.flux().toStream().findFirst());
		} catch(Exception e) {
			log.warn("Exception testGet.", e);
		}
	}
	
	void testGetAsync() {
		try {
			// Request 준비.
			Mono<User> user = webClient.mutate()
	        .baseUrl("http://localhost:8080")
	        .build()
	        .get()
	        .uri("/user/{ID}", "1")
	        .accept(MediaType.APPLICATION_JSON)
	        .retrieve()
	        .bodyToMono(User.class);
			
			// Request 실행 & Async 로 결과처리.
			user.subscribe(u -> {
				log.info("testGetAsync = {}", u);
			});
			
			user.subscribe(u -> {
				log.info("testGetAsync = {}", u);
			}, e -> {
				log.error("Exception subscribe.", e);
			});
			
		} catch(Exception e) {
			log.warn("Exception testGet.", e);
		}
	}
	
	// Body Contents를 전송할 수 있는 POST 호출 은 post() 함수를 통해 제공되며, RequestBody를 설정하기 위한 RequestBodySpec 인터페이스는 다음과 같습니다.
	// form 데이터를 생성하기 위해서는 BodyInserters.fromFormData() 를 이용할 수 있으며, bodyValue(MultiValueMap<String, String>) 을 통해서도 데이터를 전송 할 수 있습니다.
	// 객체 자체를 RequestBody로 전달하기 위해서는 bodyValue(Object body) 를 사용하거나 body(Object producer, Class<?> elementClass) 를 통해서 사용할 수 있습니다.
	void testPost() {
		/*
		webClient.mutate()
        .baseUrl("http://localhost:8080")
        .build()
        .post()
        .uri("/user")
        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
        .accept(MediaType.APPLICATION_JSON)
        .body(BodyInserters.fromFormData("id", "1")
                           .with("pwd", "pwd")
        )
        .retrieve()
        .bodyToMono(User.class);
		*/
		
		User newUser = User.builder().id(1).name("홍길동").age(20).build();
		Mono<User> user = webClient.mutate()
	        .baseUrl("http://localhost:8080")
	        .build()
	        .post()
	        .uri("/user")
	        .contentType(MediaType.APPLICATION_JSON)
	        .accept(MediaType.APPLICATION_JSON)
	        .bodyValue(newUser)
	        .retrieve()
	        .bodyToMono(User.class);
		
		user.flux().toStream().forEach(u -> {
			log.info("testPost = {}", u);
		});
		//log.info("testPost = {}", user.flux().toStream().findFirst());
	}
	
	// PUT 호출은 POST 호출과 유사하며 다만 put() 함수를 통해 시작되는 것 만 다릅니다.
	void testPut() {
		User newUser = User.builder().id(1).name("홍길동").age(20).build();
		
		webClient.mutate()
		.baseUrl("http://localhost:8080")
        .build()
        .put()
        .uri("/user/{ID}", "1")
        .contentType(MediaType.APPLICATION_JSON)
        .accept(MediaType.APPLICATION_JSON)
        .bodyValue(newUser)
        .retrieve()
        .bodyToMono(User.class);
	}
	
	// DELETE 호출은 GET 과 유사하며 delete() 함수를 통해 시작되고, delete() 함수 의 특성상 response는 Void.class 로 처리됩니다.
	void testDelete() {
		webClient.mutate()
		.baseUrl("http://localhost:8080")
        .build()
        .delete()
        .uri("/user/{ID}", "1")
        .retrieve()
        .bodyToMono(Void.class);
	}
	
	void testSynchronous() {
		// WebClient 는 Reactive Stream 기반이므로 리턴값을 Mono 또는 Flux 로 전달받게 됩니다. 
		// Spring WebFlux를 이미 사용하고 있다면 문제가 없지만 Spring MVC를 사용하는 상황에서 WebClient 를 활용하고자 한다면 Mono 나 Flux 를 객체로 변환하거나 Java Stream 으로 변환해야 할 필요가 있습니다.
		// 이럴 경우를 대비해서 Mono.block() 이나 Flux.blockFirst() 와 같은 blocking 함수가 존재하지만,
		// block() 을 이용해서 객체로 변환하면 Reactive Pipeline 을 사용하는 장점이 없어지고,
		// 모든 호출이 main 쓰레드에서 호출되기 때문에 Spring 측에서는 block() 은 테스트 용도 외에는 가급적 사용하지 말라고 권고하고 있습니다.
		// 대신 완벽한 Reactive 호출은 아니지만 Lazy Subscribe 를 통한 Stream 또는 Iterable 로 변환 시킬 수 있는 Flux.toStream(), Flux.toIterable() 함수를 제공하고 있습니다.
		// Flux.toStream() 을 통해 데이터를 추가 처리하거나 List로 변환하여 사용할 수 있습니다.
		List<User> results = webClient.mutate()
				.baseUrl("http://localhost:8080")
				.build()
				.get()
				.uri("/user")
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToFlux(User.class)
				.toStream()
				.collect(Collectors.toList());
		
		log.info("testSynchronous = {}", results);
	}
	
	public void testAsyncUser(String path) {
		List<User> results = webClient.mutate()
				.baseUrl("http://localhost:8080")
				.build()
				.get()
				.uri(path)
				.accept(MediaType.APPLICATION_JSON)
				.retrieve()
				.bodyToFlux(User.class)
				.toStream()
				.collect(Collectors.toList());
		
		log.info("{}, response = {}", path, results);
	}

}
