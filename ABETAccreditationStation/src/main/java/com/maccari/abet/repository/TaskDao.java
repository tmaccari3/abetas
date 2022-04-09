package com.maccari.abet.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.maccari.abet.domain.entity.Task;

/*
 * TaskDao.java 
 * Author: Thomas Maccari
 * 
 * Description: This service defines data access related operations associated 
 * with a Task
 * 
 */

@Repository
public interface TaskDao extends CrudRepository<Task, Long> {
	int createTask(final Task task);
	
	Task updateTask(final Task task);
	
	void updateSubmitted(final Task task);
	
	void updateComplete(final Task task);
	
	List<Task> getAllTasks();
	
	Task getTaskById(int id);
	
	List<Task> getAssignedTasks(String email);
	
	List<Task> getCreatedTasks(String email);
}
