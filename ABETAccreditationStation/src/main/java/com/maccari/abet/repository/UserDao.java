package com.maccari.abet.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
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
public interface UserDao extends CrudRepository<User, Long> {
	List<User> getAllUsers();
	
	User getUserByEmail(String email);
	
	List<String> getRoles();
}
