package com.uangel.svc.demo.web.test;

public class TestCode {

	@SuppressWarnings("null")
	public static void makeNullPointException() {
		String s = null;
		boolean test = true;
		if(test) {
			s.isEmpty();
		}
	}
}
