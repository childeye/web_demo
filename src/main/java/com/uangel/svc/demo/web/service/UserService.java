package com.uangel.svc.demo.web.service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.stereotype.Service;

import com.uangel.svc.demo.web.model.User;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserService {

	List<User> users = new ArrayList<User>();
	
	public User add(User user) {
		users.add(user);
		log.info(users.toString());
		
		return user;
	}
	
	public List<User> getAll() {
		log.info(users.toString());
		return users;
	}
	
	public User get(int id) throws Exception {
		return findById(id);
	}

	public User update(int id, User user) throws Exception {
		User find = findById(id);
		find.setAge(user.getAge());
		find.setName(user.getName());
		
		return find;
	}
	
	public int delete(int id) throws Exception {
		User find = findById(id);
		users.remove(find);
		
        return id;
	}
	
	private User findById(int id) throws Exception {
		log.info("findById = {}", id);
		return users.stream().filter(u -> id == u.getId()).findFirst().orElseThrow(() -> new NoSuchElementException());
	}
}
