package com.maccari.abet.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maccari.abet.domain.entity.User;

@Repository
public interface UserDao {
	public void createUser(final User user);
	
	public List<User> getAllUsers();
	
	public User getUserByEmail(String email);
	
	public List<String> getRoles();
}
