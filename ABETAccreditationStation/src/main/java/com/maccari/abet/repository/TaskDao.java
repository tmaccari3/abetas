package com.maccari.abet.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maccari.abet.domain.entity.Task;

@Repository
public interface TaskDao {
	void createUser(final Task task);
	
	void removeUser(final Task task);
	
	Task updateUser(final Task task);
	
	List<Task> getAllUsers();
	
	Task getUserById(int id);
	
	List<String> getPrograms();
}
