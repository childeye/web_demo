package com.uangel.svc.demo.web.filter;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Collection;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.WriteListener;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.output.TeeOutputStream;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class BufferedResponseWrapper implements HttpServletResponse {

	HttpServletResponse original;
    TeeServletOutputStream tee;
    ByteArrayOutputStream bos;

    public BufferedResponseWrapper(HttpServletResponse response) {
    	log.info("BufferedResponseWrapper constructor.");
        original = response;
    }

    public String getContent() {
    	log.info("BufferedResponseWrapper getContent().");
    	//if(bos == null) return null;
        return bos.toString();
    }

    public PrintWriter getWriter() throws IOException {
    	log.info("BufferedResponseWrapper getWriter().");
        return original.getWriter();
    }

    public ServletOutputStream getOutputStream() throws IOException {
    	log.info("BufferedResponseWrapper getOutputStream().");
        if (tee == null) {
            bos = new ByteArrayOutputStream();
            tee = new TeeServletOutputStream(original.getOutputStream(), bos);
        }
        return tee;

    }

    @Override
    public String getCharacterEncoding() {
    	log.info("BufferedResponseWrapper getCharacterEncoding().");
        return original.getCharacterEncoding();
    }

    @Override
    public String getContentType() {
    	log.info("BufferedResponseWrapper getContentType().");
        return original.getContentType();
    }

    @Override
    public void setCharacterEncoding(String charset) {
    	log.info("BufferedResponseWrapper setCharacterEncoding().");
        original.setCharacterEncoding(charset);
    }

    @Override
    public void setContentLength(int len) {
    	log.info("BufferedResponseWrapper setContentLength().");
        original.setContentLength(len);
    }

    @Override
    public void setContentLengthLong(long l) {
    	log.info("BufferedResponseWrapper setContentLengthLong().");
        original.setContentLengthLong(l);
    }

    @Override
    public void setContentType(String type) {
    	log.info("BufferedResponseWrapper setContentType().");
        original.setContentType(type);
    }

    @Override
    public void setBufferSize(int size) {
    	log.info("BufferedResponseWrapper setBufferSize().");
        original.setBufferSize(size);
    }

    @Override
    public int getBufferSize() {
    	log.info("BufferedResponseWrapper getBufferSize().");
        return original.getBufferSize();
    }

    @Override
    public void flushBuffer() throws IOException {
    	log.info("BufferedResponseWrapper flushBuffer().");
        tee.flush();
    }

    @Override
    public void resetBuffer() {
    	log.info("BufferedResponseWrapper resetBuffer().");
        original.resetBuffer();
    }

    @Override
    public boolean isCommitted() {
    	log.info("BufferedResponseWrapper isCommitted().");
        return original.isCommitted();
    }

    @Override
    public void reset() {
    	log.info("BufferedResponseWrapper reset().");
        original.reset();
    }

    @Override
    public void setLocale(Locale loc) {
    	log.info("BufferedResponseWrapper setLocale().");
        original.setLocale(loc);
    }

    @Override
    public Locale getLocale() {
    	log.info("BufferedResponseWrapper getLocale().");
        return original.getLocale();
    }

    @Override
    public void addCookie(Cookie cookie) {
    	log.info("BufferedResponseWrapper addCookie().");
        original.addCookie(cookie);
    }

    @Override
    public boolean containsHeader(String name) {
    	log.info("BufferedResponseWrapper containsHeader().");
        return original.containsHeader(name);
    }

    @Override
    public String encodeURL(String url) {
    	log.info("BufferedResponseWrapper encodeURL().");
        return original.encodeURL(url);
    }

    @Override
    public String encodeRedirectURL(String url) {
    	log.info("BufferedResponseWrapper encodeRedirectURL().");
        return original.encodeRedirectURL(url);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String encodeUrl(String url) {
    	log.info("BufferedResponseWrapper encodeUrl().");
        return original.encodeUrl(url);
    }

    @SuppressWarnings("deprecation")
    @Override
    public String encodeRedirectUrl(String url) {
    	log.info("BufferedResponseWrapper encodeRedirectUrl().");
        return original.encodeRedirectUrl(url);
    }

    @Override
    public void sendError(int sc, String msg) throws IOException {
    	log.info("BufferedResponseWrapper sendError().");
        original.sendError(sc, msg);
    }

    @Override
    public void sendError(int sc) throws IOException {
    	log.info("BufferedResponseWrapper sendError().");
        original.sendError(sc);
    }

    @Override
    public void sendRedirect(String location) throws IOException {
    	log.info("BufferedResponseWrapper sendRedirect().");
        original.sendRedirect(location);
    }

    @Override
    public void setDateHeader(String name, long date) {
    	log.info("BufferedResponseWrapper setDateHeader().");
        original.setDateHeader(name, date);
    }

    @Override
    public void addDateHeader(String name, long date) {
    	log.info("BufferedResponseWrapper addDateHeader().");
        original.addDateHeader(name, date);
    }

    @Override
    public void setHeader(String name, String value) {
    	log.info("BufferedResponseWrapper setHeader().");
        original.setHeader(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
    	log.info("BufferedResponseWrapper addHeader().");
        original.addHeader(name, value);
    }

    @Override
    public void setIntHeader(String name, int value) {
    	log.info("BufferedResponseWrapper setIntHeader().");
        original.setIntHeader(name, value);
    }

    @Override
    public void addIntHeader(String name, int value) {
    	log.info("BufferedResponseWrapper addIntHeader().");
        original.addIntHeader(name, value);
    }

    @Override
    public void setStatus(int sc) {
    	log.info("BufferedResponseWrapper setStatus().");
        original.setStatus(sc);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void setStatus(int sc, String sm) {
    	log.info("BufferedResponseWrapper setStatus().");
        original.setStatus(sc, sm);
    }

    @Override
    public String getHeader(String arg0) {
    	log.info("BufferedResponseWrapper getHeader().");
        return original.getHeader(arg0);
    }

    @Override
    public Collection<String> getHeaderNames() {
    	log.info("BufferedResponseWrapper getHeaderNames().");
        return original.getHeaderNames();
    }

    @Override
    public Collection<String> getHeaders(String arg0) {
    	log.info("BufferedResponseWrapper getHeaders().");
        return original.getHeaders(arg0);
    }

    @Override
    public int getStatus() {
    	log.info("BufferedResponseWrapper getStatus().");
        return original.getStatus();
    }
    
    
    
    private class TeeServletOutputStream extends ServletOutputStream {

        private final TeeOutputStream targetStream;

        public TeeServletOutputStream(OutputStream one, OutputStream two) {
        	log.info("TeeServletOutputStream constructor.");
            targetStream = new TeeOutputStream(one, two);
        }

        @Override
        public void write(int arg0) throws IOException {
        	log.info("BufferedResponseWrapper write().");
            this.targetStream.write(arg0);
        }

        public void flush() throws IOException {
        	log.info("BufferedResponseWrapper flush().");
            super.flush();
            this.targetStream.flush();
        }

        public void close() throws IOException {
        	log.info("BufferedResponseWrapper close().");
            super.close();
            this.targetStream.close();
        }

        @Override
        public boolean isReady() {
        	log.info("BufferedResponseWrapper isReady().");
            return false;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
        	log.info("BufferedResponseWrapper setWriteListener().");
        }
    }

}
