package com.uangel.svc.demo.web.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.http.codec.LoggingCodecSupport;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.ExchangeStrategies;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;
import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;
import reactor.netty.http.client.HttpClient;

@Configuration
@Slf4j
public class WebClientConfig {

    @Bean
    public WebClient webClient() {

    	// Spring WebFlux 에서는 어플리케이션 메모리 문제를 피하기 위해 codec 처리를 위한 in-memory buffer 값이 256KB로 기본설정 되어 있습니다.
    	// 이 제약 때문에 256KB보다 큰 HTTP 메시지를 처리하려고 하면 DataBufferLimitException 에러가 발생하게 됩니다.
    	// 이 값을 늘려주기 위해서는 ExchageStrategies.builder() 를 통해 값을 늘려줘야 합니다.
        ExchangeStrategies exchangeStrategies = ExchangeStrategies.builder()
                                                                  .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024*1024*50))
                                                                  .build();
        
        // Debug 레벨 일 때 form Data 와 Trace 레벨 일 때 header 정보는 민감한 정보를 포함하고 있기 때문에, 기본 WebClient 설정에서는 위 정보를 로그에서 확인할 수 가 없습니다. 
        // 개발 진행 시 Request/Response 정보를 상세히 확인하기 위해서는 ExchageStrateges 와 logging level 설정을 통해 로그 확인이 가능하도록 해 주는 것이 좋습니다.
        // logging.level: org.springframework.web.reactive.function.client.ExchangeFunctions: DEBUG
        exchangeStrategies
            .messageWriters().stream()
            .filter(LoggingCodecSupport.class::isInstance)
            .forEach(writer -> ((LoggingCodecSupport)writer).setEnableLoggingRequestDetails(true));

        return WebClient.builder()
        		// HttpClient 를 변경하거나 ConnectionTimeOut 과 같은 설정값을 변경하려면 WebClient.builder().clientConnector() 를 통해 Reactor Netty의 HttpClient 를 직접 설정해 줘야 합니다.
        		// HTTPS 인증서를 검증하지 않고 바로 접속하는 설정과, TCP 연결 시 ConnectionTimeOut , ReadTimeOut , WriteTimeOut 을 적용하는 설정을 추가하였습니다.
                .clientConnector(
                    new ReactorClientHttpConnector(
                        HttpClient
                            .create()
//                            .secure(
//                                ThrowingConsumer.unchecked(
//                                    sslContextSpec -> sslContextSpec.sslContext(
//                                        SslContextBuilder.forClient().trustManager(InsecureTrustManagerFactory.INSTANCE).build()
//                                    )
//                                )
//                            )
                            .tcpConfiguration(
                                client -> client.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 120_000)
                                                .doOnConnected(conn -> conn.addHandlerLast(new ReadTimeoutHandler(180))
                                                                           .addHandlerLast(new WriteTimeoutHandler(180))
                                                )
                            )
                    )
                )
                .exchangeStrategies(exchangeStrategies)
                
                // Request 또는 Response 데이터에 대해 조작을 하거나 추가 작업을 하기 위해서는 WebClient.builder().filter() 메소드를 이용해야 합니다.
                // ExchangeFilterFunction.ofRequestProcessor() 와 ExchangeFilterFunction.ofResponseProcessor() 를 통해 clientRequest 와 clientResponse 를 변경하거나 출력할 수 있습니다.
                // Request / Response header를 출력하는 예제
                .filter(ExchangeFilterFunction.ofRequestProcessor(
                    clientRequest -> {
                        log.debug("Request: {} {}", clientRequest.method(), clientRequest.url());
                        clientRequest.headers().forEach((name, values) -> values.forEach(value -> log.debug("{} : {}", name, value)));
                        return Mono.just(clientRequest);
                    }
                ))
                .filter(ExchangeFilterFunction.ofResponseProcessor(
                    clientResponse -> {
                        clientResponse.headers().asHttpHeaders().forEach((name, values) -> values.forEach(value -> log.debug("{} : {}", name, value)));
                        return Mono.just(clientResponse);
                    }
                ))
                .defaultHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/78.0.3904.87 Safari/537.3")
                .build();
    }
}
