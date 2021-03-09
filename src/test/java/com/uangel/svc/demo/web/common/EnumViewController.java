package com.uangel.svc.demo.web.common;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.JsonObject;

@RestController
@RequestMapping("/docs")
public class EnumViewController {

	@GetMapping
	public JsonObject get() {
		JsonObject obj3 = new JsonObject();
		obj3.addProperty("MALE", "0");
		obj3.addProperty("FEMALE", "1");
		
		JsonObject obj2 = new JsonObject();
		obj2.add("genders", obj3);
		
		JsonObject obj1 = new JsonObject();
		obj1.add("data", obj2);
		
		return obj1;
	}
	
}
