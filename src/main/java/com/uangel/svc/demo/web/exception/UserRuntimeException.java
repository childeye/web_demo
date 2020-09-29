package com.uangel.svc.demo.web.exception;

import lombok.Getter;

// Exception에 응답코드 달기 가능? 안 먹히더라
//@ResponseStatus(HttpStatus.BAD_GATEWAY)
public class UserRuntimeException extends RuntimeException {
	
	private static final long serialVersionUID = -37771660597567991L;

	@Getter
	int code;
	
	String message;
	
	public UserRuntimeException(int code, String message) {
		super("[UserRuntimeException] " + message);
		this.code = code;
	}

}
