package com.maccari.abet.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.Task;
import com.maccari.abet.repository.TaskDao;

@Component
public class TaskService implements Service<Task>{
	@Autowired
	private TaskDao taskDao;

	@Override
	public List<Task> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(Task item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(Task item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Task update(Task item) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<String> getPrograms(){
		return taskDao.getPrograms();
	}


}
