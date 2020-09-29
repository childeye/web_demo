package com.uangel.svc.demo.web.exception;

import lombok.Getter;

public class UserException extends Exception {

	private static final long serialVersionUID = 1010189387587189032L;
	
	@Getter
	int code;
	
	String message;
	
	public UserException(int code, String message) {
		super("[UserException] " + message);
		this.code = code;
	}

}
