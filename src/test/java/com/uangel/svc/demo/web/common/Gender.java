package com.uangel.svc.demo.web.common;

public enum Gender implements EnumType {
	
	MALE("0"), FEMALE("1");
	
	private String id;
	
	Gender(String id) {
		this.id = id;
	}
	
	public String getId() {
		return this.id;
	}
	
	public String getText() {
		return this.name();
	}
	
	
}
