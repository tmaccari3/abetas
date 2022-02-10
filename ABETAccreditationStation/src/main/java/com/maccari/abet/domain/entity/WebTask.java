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

	private List<Integer> programs;

	private List<Integer> outcomes;
	
	private List<Program> fullPrograms;

	private List<StudentOutcome> fullOutcomes;

	@NotEmpty(message = "*required")
	private String description;
	
	public WebTask() {
		assignees = new ArrayList<String>();
	}
	
	public WebTask(Task task) {
		this.id = task.getId();
		this.coordinator = task.getCoordinator();
		this.title = task.getTitle();
		this.assignees = task.getAssignees();
		this.fullPrograms = task.getPrograms();
		this.fullOutcomes = task.getOutcomes();
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

	public List<Program> getFullPrograms() {
		return fullPrograms;
	}

	public void setFullPrograms(List<Program> fullPrograms) {
		this.fullPrograms = fullPrograms;
	}

	public List<StudentOutcome> getFullOutcomes() {
		return fullOutcomes;
	}

	public void setFullOutcomes(List<StudentOutcome> fullOutcomes) {
		this.fullOutcomes = fullOutcomes;
	}

	//for debugging
	public String toString() {
		String result = "";
		
		result += title + "\n";
		result += assignees + "\n" + programs;
		
		return result;
	}
}
