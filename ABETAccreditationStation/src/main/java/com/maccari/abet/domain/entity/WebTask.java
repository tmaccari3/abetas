package com.maccari.abet.domain.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

public class WebTask {
	private int id;

	private String coordinator;

	@NotEmpty(message = "*required")
	private String title;
	
	private List<String> assignees;

	private List<String> programs;

	private List<String> outcomes;

	@NotEmpty(message = "*required")
	private String description;
	
	public WebTask() {
		assignees = new ArrayList<String>();
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

	public List<String> getPrograms() {
		return programs;
	}

	public void setPrograms(List<String> programs) {
		this.programs = programs;
	}

	public List<String> getOutcomes() {
		return outcomes;
	}

	public void setOutcomes(List<String> outcome) {
		this.outcomes = outcome;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public Task convertToTask() {
		Task task = new Task();
		task.setId(this.getId());
		task.setTitle(this.getTitle());
		task.setDescription(this.getDescription());
		task.setAssignees(this.getAssignees());
		task.setPrograms(this.getPrograms());
		task.setOutcomes(this.getOutcomes());
		
		return task;
	}
	
	//for debugging
	public String toString() {
		String result = "";
		
		result += title + "\n";
		result += assignees + "\n" + programs;
		
		return result;
	}
}
