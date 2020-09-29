# Web Demo

### Spring Boot Web Project
Request/Response 로깅 테스트

* Filter
* Interceptor
* AOP


### Filter

* 웹 어플리케이션의 Context의 기능
* Java의 Servlet 스펙에서 정의된 개념. Java Servlet의 스펙이므로 스프링 프레임워크가 없이도 사용 가능.
* 스프링 기능을 활용하기에 어려움
* 일반적으로 인코딩, CORS, XSS, LOG, 인증, 권한 등 을 구현
* Filter는 다음 Filter로 체이닝을 할때 새로운 HttpServletRequest, HttpServletResponse를 넘길 수 있다.
* ServletRequest의 Body는 한 번 밖에 읽을 수 없다. 여러번 읽을 수 있게 원본 Request에서 읽은 내용을 byte[] 버퍼에 저장해둔 새로운 ServeletRequest를 정의하여 넘길 수 있다.

* 구현 방법
** 1. FilterRegistrationBean을 이용해서 Filter로 등록한 후에 WebMvcConfigurer을 상속 받아 설정 후 빈으로 등록하는 방식.
** 2. Spring boot 에서 지원하는 @WebServlet, @WebFilter 및 @WebListener 로 선언 후 @ServletComponentScan로 이용하는 방식.
** 모든 URL 에 필터를 적용할 경우 @Component 사용.
** 특정 URL 에 필터를 적용할 경우 FilterRegistrationBean 이나 @WebFilter + @ServletComponentScan 조합을 사용.

* Spring 에서 제공하는 Filter
** HttpFilter extends GenericFilter implements Filter,...
** OncePerRequestFilter extends GenericFilterBean implements Filter,... --> spring config 설정 정보를 쉽게 처리. getFilterConfig()나 getEnvironment()를 제공.
** - GenericFilterBean : 서블릿 필터의 초기화 파라미터를 서블릿 필터 클래스의 프로퍼티에 바인드하는 기반 클래스
** - OncePerRequestFilter : Filter를 중첩 호출한 경우(의도치 않은 경우) 매번 Filter의 내용이 수행되는 것을 방지. 스프링제공 서블릿 필터는 이 클래스를 상속받음


### Interceptor

* Spring의 스펙으로, Filter와 목적은 동일하나 좀 더 세분화된 컨트롤을 제공
* Spring Context의 기능이며 일종의 빈
* Spring Container이기에 다른 빈을 주입하여 인증, 권한 등을 구현
* Filter는 전, 후 처리만 가능하지만 Interceptor는 preHandle, postHandler, afterCompletion로 세분화
* WebContentInterceptor, LocaleChangeInterceptor 등의 상황에 맞게 활용할 수 있는 다양한 인터페이스를 제공
* DispatcherServlet 사이클 안에서 사용되므로 ModelAndView를 접근 가능.
* DispatcherServlet 으로 넘어온 상태이기 때문에 HttpServletRequest, HttpServletResponse를 다른 인스턴스로 교체할 수 있는 방법이 없다.

