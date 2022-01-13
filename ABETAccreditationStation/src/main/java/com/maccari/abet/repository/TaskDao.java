package com.maccari.abet.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maccari.abet.domain.entity.Task;

@Repository
public interface TaskDao {
	void createTask(final Task task);
	
	void removeTask(final Task task);
	
	Task updateTask(final Task task);
	
	List<Task> getAllTask();
	
	Task getTaskById(int id);
	
	List<String> getPrograms();
}
