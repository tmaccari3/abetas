package com.maccari.abet.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import com.maccari.abet.domain.entity.User;
import com.maccari.abet.repository.UserDao;

public class UserService implements Service<User> {
	@Autowired
	private UserDao userDao;
	
	@Override
	public List<User> getAll() {
		return userDao.getAllUsers();
	}

	@Override
	public User getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public User getUserByEmail(String email) {
		return userDao.getUserByEmail(email);
	}

	@Override
	public void create(User item) {
		// TODO Auto-generated method stub
		
	}
}
