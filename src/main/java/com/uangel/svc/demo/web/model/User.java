package com.uangel.svc.demo.web.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class User {
	int id;
	String name;
	int age;
}
