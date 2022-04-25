package com.maccari.abet.domain.entity.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import org.springframework.format.annotation.DateTimeFormat;

import com.maccari.abet.domain.entity.File;
import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.ProgramData;
import com.maccari.abet.domain.entity.StudentOutcome;
import com.maccari.abet.domain.entity.StudentOutcomeData;
import com.maccari.abet.domain.entity.Task;
import com.maccari.abet.domain.entity.relation.task.TaskAssignee;

/*
 * DocumentSearch.java 
 * Author: Thomas Maccari
 * 
 * Description: This class serves as a form backing object for 
 * a Task created by a user
 * 
 */

public class WebTask {
	private int id;

	private String coordinator;

	@NotEmpty(message = "*required")
	private String title;
	
	private List<String> assignees;

	private List<Integer> programs;

	private List<Integer> outcomes;
	
	private List<? extends Program> fullPrograms;

	private List<? extends StudentOutcome> fullOutcomes;
	
	@NotNull(message = "*required")
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dueDate;
	
	private File uploadedFile;

	@NotEmpty(message = "*required")
	private String description;
	
	public WebTask() {
		assignees = new ArrayList<String>();
		programs = new ArrayList<Integer>();
		outcomes = new ArrayList<Integer>();
	}
	
	public WebTask(Task task) {
		this.id = task.getId();
		this.coordinator = task.getCoordinator();
		this.title = task.getTitle();
		this.assignees = new ArrayList<String>();
		for(TaskAssignee assignee : task.getAssignees()) {
			this.assignees.add(assignee.getEmail());
		}
		this.fullPrograms = task.getPrograms();
		this.fullOutcomes = task.getOutcomes();
		this.description = task.getDescription();
		this.dueDate = task.getDueDate();
		this.uploadedFile = task.getFile();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(String coordinator) {
		this.coordinator = coordinator;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getAssignees() {
		return assignees;
	}

	public void setAssignees(List<String> assignees) {
		this.assignees = assignees;
	}

	public List<Integer> getPrograms() {
		return programs;
	}

	public void setPrograms(List<Integer> programs) {
		this.programs = programs;
	}

	public List<Integer> getOutcomes() {
		return outcomes;
	}

	public void setOutcomes(List<Integer> outcomes) {
		this.outcomes = outcomes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<? extends Program> getFullPrograms() {
		return fullPrograms;
	}

	public void setFullPrograms(List<Program> fullPrograms) {
		this.fullPrograms = fullPrograms;
	}

	public List<? extends StudentOutcome> getFullOutcomes() {
		return fullOutcomes;
	}

	public void setFullOutcomes(List<StudentOutcome> fullOutcomes) {
		this.fullOutcomes = fullOutcomes;
	}

	public synchronized File getUploadedFile() {
		return uploadedFile;
	}

	public synchronized void setUploadedFile(File uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	
	public List<TaskAssignee> getFullAssignees() {
		ArrayList<TaskAssignee> fullAssignees = new ArrayList<TaskAssignee>();
		
		for(String assignee : assignees) {
			fullAssignees.add(new TaskAssignee(assignee));
		}
		
		return fullAssignees;
	}

	// For email body convenience
	public String toString() {
		String result = "";
		
		result += title + "\n\n";
		result += "Due:" + new SimpleDateFormat("yyyy-MM-dd").format(dueDate) + "\n";
		result += "Assigned by: " + coordinator + "\n";
		result += description;
		
		return result;
	}
}
