package com.uangel.svc.demo.web.filter;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.WebUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class GlobalOncePerRequestFilter extends OncePerRequestFilter {

	static final String className = GlobalOncePerRequestFilter.class.getSimpleName();
	
    public static final String DEFAULT_AFTER_MESSAGE_PREFIX = "After request [";

    public static final String DEFAULT_AFTER_MESSAGE_SUFFIX = "]";

    private final boolean includeQueryString = true;
    private final boolean includeClientInfo = true;
    private final boolean includeHeaders = true;
    private final boolean includePayload = true;

    private final int maxPayloadLength = (int) (2 * FileUtils.ONE_MB);

    private final String afterMessagePrefix = DEFAULT_AFTER_MESSAGE_PREFIX;

    private final String afterMessageSuffix = DEFAULT_AFTER_MESSAGE_SUFFIX;

    
    @Override
    protected void initFilterBean() throws ServletException {
    	log.info("[{}]2 OncePerRequestFilter.initFilterBean()", className);
    }
    
    
    /**
     * The default value is "false" so that the filter may log a "before" message
     * at the start of request processing and an "after" message at the end from
     * when the last asynchronously dispatched thread is exiting.
     */
    @Override
    protected boolean shouldNotFilterAsyncDispatch() {
        return false;
    }

    
    // OncePerRequestFilter 에서 ContentCachingRequestWrapper, ContentCachingResponseWrapper 로 Wrapping 됨.
    @Override
    protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response, final FilterChain filterChain)
            throws ServletException, IOException {
    	
    	log.info("[{}]2 OncePerRequestFilter.doFilterInternal()", className);

//        final boolean isFirstRequest = !isAsyncDispatch(request);
//        HttpServletRequest requestToUse = request;
//
//        if (includePayload && isFirstRequest && !(request instanceof ContentCachingRequestWrapper)) {
//            requestToUse = new ContentCachingRequestWrapper(request, maxPayloadLength);
//        }
//
//        final boolean shouldLog = shouldLog(requestToUse);
//
//        try {
//        	log.info("GlobalOncePerRequestFilter before doFilter.");
//            filterChain.doFilter(requestToUse, response);
//            log.info("GlobalOncePerRequestFilter after doFilter.");
//        } finally {
//            if (shouldLog && !isAsyncStarted(requestToUse)) {
//                afterRequest(requestToUse, response, getAfterMessage(requestToUse));
//            }
//        }
    	
    	log.info("GlobalOncePerRequestFilter before doFilter.");
        filterChain.doFilter(request, response);
        log.info("GlobalOncePerRequestFilter after doFilter.");
    }

    private String getAfterMessage(final HttpServletRequest request) {
        return createMessage(request, this.afterMessagePrefix, this.afterMessageSuffix);
    }

    private String createMessage(final HttpServletRequest request, final String prefix, final String suffix) {
        final StringBuilder msg = new StringBuilder();
        msg.append(prefix);
        msg.append("uri=").append(request.getRequestURI());

        if (includeQueryString) {
            final String queryString = request.getQueryString();
            if (queryString != null) {
                msg.append('?').append(queryString);
            }
        }

        if (includeClientInfo) {
            final String client = request.getRemoteAddr();
            if (StringUtils.hasLength(client)) {
                msg.append(";client=").append(client);
            }
            final HttpSession session = request.getSession(false);
            if (session != null) {
                msg.append(";session=").append(session.getId());
            }
            final String user = request.getRemoteUser();
            if (user != null) {
                msg.append(";user=").append(user);
            }
        }

        if (includeHeaders) {
            msg.append(";headers=").append(new ServletServerHttpRequest(request).getHeaders());
        }

        if (includeHeaders) {
            final ContentCachingRequestWrapper wrapper = WebUtils.getNativeRequest(request, ContentCachingRequestWrapper.class);
            if (wrapper != null) {
                final byte[] buf = wrapper.getContentAsByteArray();
                if (buf.length > 0) {
                    final int length = Math.min(buf.length, maxPayloadLength);
                    String payload;
                    try {
                        payload = new String(buf, 0, length, wrapper.getCharacterEncoding());
                    } catch (final UnsupportedEncodingException ex) {
                        payload = "[unknown]";
                    }
                    msg.append(";payload=").append(payload);
                }
            }
        }
        msg.append(suffix);
        return msg.toString();
    }

    private boolean shouldLog(final HttpServletRequest request) {
        return true;
    }

    private void afterRequest(final HttpServletRequest request, final HttpServletResponse response, final String message) {
        if (response.getStatus() == HttpStatus.BAD_REQUEST.value()) {
            log.warn(message);
        }
    }

}
