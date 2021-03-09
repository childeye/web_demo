package com.uangel.svc.demo.web.common;

import static org.springframework.restdocs.snippet.Attributes.key;

import org.springframework.restdocs.snippet.Attributes;

public interface DocumentFormatGenerator {

	// 반복적으로 사용되는 것들은 static 메서드로 사용
	static Attributes.Attribute getDateFormat() {
        return key("format").value("yyyy-MM-dd");
    }
	
}
