package com.maccari.abet.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maccari.abet.domain.entity.User;

/*
 * UserDao.java 
 * Author: Thomas Maccari
 * 
 * Description: This service defines data access related operations associated 
 * with a User
 * 
 */

@Repository
public interface UserDao {
	void createUser(final User user);
	
	void removeUser(final User user);
	
	User updateUser(final User user);
	
	List<User> getAllUsers();
	
	User getUserByEmail(String email);
	
	List<String> getRoles();
}
