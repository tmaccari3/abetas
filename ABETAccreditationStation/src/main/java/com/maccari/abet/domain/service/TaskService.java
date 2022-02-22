package com.maccari.abet.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.StudentOutcome;
import com.maccari.abet.domain.entity.Task;
import com.maccari.abet.domain.entity.web.WebTask;
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
	
	public Task webTaskToTask(WebTask webTask) {
		Task task = new Task();
		
		task.setId(webTask.getId());
		task.setTitle(webTask.getTitle());
		task.setAssignees(webTask.getAssignees());
		task.setDescription(webTask.getDescription());
		task.setCoordinator(webTask.getCoordinator());
		task.setPrograms(webTask.getFullPrograms());
		task.setOutcomes(webTask.getFullOutcomes());
		task.setDueDate(webTask.getDueDate());
		task.setFile(webTask.getUploadedFile());
		
		return task;
	}
	
	public WebTask taskToWebTask(Task task) {
		WebTask webTask = new WebTask(task);
		
		ArrayList<Integer> programIds = new ArrayList<Integer>();
		ArrayList<Integer> outcomeIds = new ArrayList<Integer>();
		
		for(Program program : task.getPrograms()) {
			programIds.add(program.getId());
		}
		for(StudentOutcome outcome : task.getOutcomes()) {
			outcomeIds.add(outcome.getId());
		}
		
		webTask.setOutcomes(outcomeIds);
		webTask.setPrograms(programIds);
		
		return webTask;
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
