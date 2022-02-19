package com.maccari.abet.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.User;
import com.maccari.abet.domain.entity.WebUser;
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
	
	@Override
	public User update(User item) {
		return userDao.updateUser(item);
	}
	
	public User getUserByEmail(String email) {
		return userDao.getUserByEmail(email);
	}
	
	public List<String> getRoles(){
		return userDao.getRoles();
	}
	
	public User convertWebUser(WebUser webUser) {
		return new User(webUser.getEmail(), webUser.getRoles(), webUser.getPrograms());
	}
}
