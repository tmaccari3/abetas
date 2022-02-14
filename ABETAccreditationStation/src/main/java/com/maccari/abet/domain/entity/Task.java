package com.maccari.abet.domain.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

public class Task {
	private int id;

	private String coordinator;

	@NotEmpty(message = "*required")
	private String title;

	private List<String> assignees;

	private List<Program> programs;

	private List<StudentOutcome> outcomes;

	@NotEmpty(message = "*required")
	private String description;
	
	private Timestamp assignDate;
	
	private File file;
	
	private boolean submitted = false;
	
	private boolean complete = false;
	
	public Task() {
		assignees = new ArrayList<String>();
		programs = new ArrayList<Program>();
		outcomes = new ArrayList<StudentOutcome>();
	}
	
	public Task(List<String> assignees) {
		this.assignees = assignees;
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

	public List<Program> getPrograms() {
		return programs;
	}

	public void setPrograms(List<Program> programs) {
		this.programs = programs;
	}

	public List<StudentOutcome> getOutcomes() {
		return outcomes;
	}

	public void setOutcomes(List<StudentOutcome> outcomes) {
		this.outcomes = outcomes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}
	
	public String getStatus() {
		String result = "incomplete";
		if(complete) {
			result = "complete";
		}
		
		return result;
	}

	public Timestamp getAssignDate() {
		return assignDate;
	}

	public void setAssignDate(Timestamp assignDate) {
		this.assignDate = assignDate;
	}
	
	public synchronized File getFile() {
		return file;
	}

	public synchronized void setFile(File file) {
		this.file = file;
	}

	public boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}
	
	public String getFormattedDate() {
		if(assignDate == null) {
			return "";
		}
		
		else {
			return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(assignDate);
		}
	}
	
	//for debugging
	public String toString() {
		String result = "";
		
		result += id + ": " + title + "\n";
		result += assignees + "\n" + programs + "\n";
		result += assignDate;
		
		return result;
	}
}
