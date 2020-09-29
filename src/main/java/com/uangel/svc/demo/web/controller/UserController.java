package com.uangel.svc.demo.web.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.uangel.svc.demo.web.exception.UserException;
import com.uangel.svc.demo.web.exception.UserRuntimeException;
import com.uangel.svc.demo.web.model.User;
import com.uangel.svc.demo.web.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/user")
public class UserController {
	
	@Autowired
	UserService userService;
	
	@PostMapping
	public User c(@RequestBody User user) {
		log.info(user.toString());
		return userService.add(user);
	}
	
	@GetMapping
	public List<User> r() {
		return userService.getAll();
	}
	
	@GetMapping("/{id}")
	public User rid(@PathVariable int id) throws UserException {
		try {
			return userService.get(id);
		} catch (Exception e) {
			throw new UserRuntimeException(101, "Fail to get user.");
		}
	}
	
	@PutMapping("/{id}")
    public User u(@PathVariable int id, @RequestBody User user) throws UserException {
        try {
			return userService.update(id, user);
		} catch (Exception e) {
			throw new UserException(201, "Fail to update user.");
		}
    }
	
	@DeleteMapping("/{id}")
    public int d(@PathVariable int id) throws UserException {
        try {
			return userService.delete(id);
		} catch (Exception e) {
			throw new UserException(301, "Fail to delete user.");
		}
    }
	
	@GetMapping("/error")
	public void error() throws UserException {
		throw new UserException(501, "User Error.");
	}
	
	@GetMapping("/error/global")
	public void errorGlobal() throws Exception {
		throw new Exception("User Global Error.");
	}
	
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler({UserException.class})
	public Map<String, Object> handle(UserException e) {
		log.error("==================== UserController @ExceptionHandler UserException");
		
		Map<String, Object> errorMap = new HashMap<String, Object>();
		errorMap.put("code", e.getCode());
		errorMap.put("message", e.getMessage());
		
		return errorMap;
	}
	
	
}
