package com.maccari.abet.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.File;
import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.ProgramData;
import com.maccari.abet.domain.entity.StudentOutcome;
import com.maccari.abet.domain.entity.Task;
import com.maccari.abet.domain.entity.web.WebTask;
import com.maccari.abet.repository.TaskDao;
import com.maccari.abet.repository.UserDao;

/*
 * TaskService.java 
 * Author: Thomas Maccari
 * 
 * Implements: Service.java
 * 
 * Description: This service class provides access to the data-source, methods 
 * for Task to WebTask conversion, and other task related utilities.
 * 
 */

@Component
public class TaskService implements Service<Task> {
	@Autowired
	private TaskDao taskDao;

	@Autowired
	private UserDao userDao;

	@Autowired
	private FileService fileService;

	@Override
	public List<Task> getAll() {
		return taskDao.getAllTasks();
	}

	@Override
	public Task getById(int id) {
		return taskDao.getTaskById(id);
	}

	public Task getFullTaskById(int id) {
		Task task = getById(id);
		File file = task.getFile();
		if (file != null) {
			task.setFile(fileService.getFileById(file.getId()));
		}

		return task;
	}

	@Override
	public void create(Task item) {
		taskDao.createTask(item);
	}
	
	public int createAndGetId(Task item) {
		return taskDao.createTask(item);
	}

	@Override
	public void remove(Task item) {
		taskDao.removeTask(item);
	}

	@Override
	public Task update(Task item) {
		return taskDao.updateTask(item);
	}

	public void updateSubmit(Task item) {
		taskDao.updateSubmitted(item);
	}

	public void updateComplete(Task item) {
		taskDao.updateComplete(item);
	}

	public List<Task> getAssigned(String email) {
		return taskDao.getAssignedTasks(email);
	}

	public List<Task> getCreated(String email) {
		return taskDao.getCreatedTasks(email);
	}

	// Converts a given WebTask to a Task
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

	// Converts a given Task to a WebTask
	public WebTask taskToWebTask(Task task) {
		WebTask webTask = new WebTask(task);

		ArrayList<Integer> programIds = new ArrayList<Integer>();
		ArrayList<Integer> outcomeIds = new ArrayList<Integer>();

		for (Program program : task.getPrograms()) {
			programIds.add(program.getId());
		}
		for (StudentOutcome outcome : task.getOutcomes()) {
			outcomeIds.add(outcome.getId());
		}

		webTask.setOutcomes(outcomeIds);
		webTask.setPrograms(programIds);

		return webTask;
	}

	// Returns the position of the first user not found by email from a list of emails
	public int userExists(List<String> emails) {
		for (int i = 0; i < emails.size(); i++) {
			if (userDao.getUserByEmail(emails.get(i)) == null) {
				return i;
			}
		}

		return -1;
	}
}
