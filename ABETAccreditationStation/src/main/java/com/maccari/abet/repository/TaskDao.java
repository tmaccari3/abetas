package com.maccari.abet.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maccari.abet.domain.entity.Task;

@Repository
public interface TaskDao {
	int createTask(final Task task);
	
	void removeTask(final Task task);
	
	Task updateTask(final Task task);
	
	void updateSubmitted(final Task task);
	
	void updateComplete(final Task task);
	
	List<Task> getAllTasks();
	
	Task getTaskById(int id);
	
	List<Task> getAssignedTasks(String email);
	
	List<Task> getCreatedTasks(String email);
}
