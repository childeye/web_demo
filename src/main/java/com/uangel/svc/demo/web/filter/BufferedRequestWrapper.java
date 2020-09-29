package com.uangel.svc.demo.web.filter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.io.IOUtils;
import org.springframework.util.StringUtils;

public class BufferedRequestWrapper extends HttpServletRequestWrapper {

	// https://preamtree.tistory.com/160
	// https://meetup.toast.com/posts/44
	
	private final Charset encoding;
    private byte[] buffer;
    
	public BufferedRequestWrapper(HttpServletRequest request) throws IOException {
		super(request);
		
		String characterEncoding = request.getCharacterEncoding();
        if (StringUtils.isEmpty(characterEncoding)) {
            characterEncoding = StandardCharsets.UTF_8.name();
        }
        this.encoding = Charset.forName(characterEncoding);
		
        
//		InputStream is = request.getInputStream();
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();
//        byte buf[] = new byte[1024];
//        int read;
//        while ((read = is.read(buf)) > 0) {
//            baos.write(buf, 0, read);
//        }
//        this.buffer = baos.toByteArray();
        this.buffer = IOUtils.toByteArray(request.getInputStream());
	}
	
	@Override
    public ServletInputStream getInputStream() {
        return new BufferedServletInputStream(new ByteArrayInputStream(this.buffer));
    }
	
	@Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream(), this.encoding));
    }

    public String getRequestBody() throws IOException {
//        BufferedReader reader = getReader();
//        String line = null;
//        StringBuilder inputBuffer = new StringBuilder();
//        do {
//            line = reader.readLine();
//            if (null != line) {
//                inputBuffer.append(line.trim());
//            }
//        } while (line != null);
//        reader.close();
//        return inputBuffer.toString().trim();
    	
    	return new String(this.buffer, Charset.forName("UTF-8"));
    }
    
	private static final class BufferedServletInputStream extends ServletInputStream {

		private ByteArrayInputStream bais;

		public BufferedServletInputStream(ByteArrayInputStream bais) {
			this.bais = bais;
		}

		@Override
		public int available() {
			return this.bais.available();
		}

		@Override
		public int read() {
			return this.bais.read();
		}

		@Override
		public int read(byte[] buf, int off, int len) {
			return this.bais.read(buf, off, len);
		}

		@Override
		public boolean isFinished() {
			return false;
		}

		@Override
		public boolean isReady() {
			return true;
		}

		@Override
		public void setReadListener(ReadListener readListener) {
		}
	}

}
