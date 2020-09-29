# Web Demo

### Spring Boot Web Project
Request/Response 로깅 테스트

* Filter
* Interceptor
* AOP


### Filter 사용 방법

* 웹 어플리케이션의 Context의 기능
* Java의 Servlet 스펙에서 정의된 개념. Java Servlet의 스펙이므로 스프링 프레임워크가 없이도 사용 가능.
* 스프링 기능을 활용하기에 어려움
* 일반적으로 인코딩, CORS, XSS, LOG, 인증, 권한 등 을 구현
* Filter는 다음 Filter로 체이닝을 할때 새로운 HttpServletRequest, HttpServletResponse를 넘길 수 있다.
* ServletRequest의 Body는 한 번 밖에 읽을 수 없다. 여러번 읽을 수 있게 원본 Request에서 읽은 내용을 byte[] 버퍼에 저장해둔 새로운 ServeletRequest를 정의하여 넘길 수 있다.


