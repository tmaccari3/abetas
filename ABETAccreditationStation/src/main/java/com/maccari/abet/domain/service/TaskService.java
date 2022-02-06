package com.maccari.abet.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.Task;
import com.maccari.abet.repository.TaskDao;
import com.maccari.abet.repository.UserDao;

@Component
public class TaskService implements Service<Task>{
	@Autowired
	private TaskDao taskDao;
	
	@Autowired
	private UserDao userDao;

	@Override
	public List<Task> getAll() {
		return taskDao.getAllTasks();
	}

	@Override
	public Task getById(int id) {
		return taskDao.getTaskById(id);
	}

	@Override
	public void create(Task item) {
		taskDao.createTask(item);
	}

	@Override
	public void remove(Task item) {
		taskDao.removeTask(item);
	}

	@Override
	public Task update(Task item) {
		return taskDao.updateTask(item);
	}
	
	public List<Task> getAssigned(String email) {
		return taskDao.getAssignedTasks(email);
	}
	
	public List<Task> getCreated(String email){
		return taskDao.getCreatedTasks(email);
	}
	
	public int userExists(List<String> emails) {
		for(int i = 0; i < emails.size(); i++) {
			if(userDao.getUserByEmail(emails.get(i)) == null) {
				return i;
			}
		}
		
		return -1;
	}
}
