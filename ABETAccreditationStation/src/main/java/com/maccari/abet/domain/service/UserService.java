package com.maccari.abet.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.User;
import com.maccari.abet.repository.UserDao;

@Component
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

	@Override
	public void create(User item) {
		userDao.createUser(item);
	}

	@Override
	public void remove(User item) {
		userDao.removeUser(item);
	}
	
	public User getUserByEmail(String email) {
		return userDao.getUserByEmail(email);
	}
}
