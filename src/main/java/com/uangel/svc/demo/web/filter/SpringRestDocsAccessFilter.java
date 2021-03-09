package com.uangel.svc.demo.web.filter;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@WebFilter(urlPatterns = "/docs/index.html")
public class SpringRestDocsAccessFilter implements Filter {

	@Value("${spring.profiles.active:default}")
	private String phase;

	@Override
	public void init(FilterConfig filterConfig) {
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
		throws IOException, ServletException {

//		if ("prd".equals(phase)) { // spring profile 이 release 일 경우 일부러 FORBIDDEN 처리를 해준다.
//			HttpServletResponse response = (HttpServletResponse)servletResponse;
//			response.sendError(HttpServletResponse.SC_FORBIDDEN);
//			filterChain.doFilter(servletRequest, response);
//			return;
//		}

		filterChain.doFilter(servletRequest, servletResponse);
	}

	@Override
	public void destroy() {
	}
}
