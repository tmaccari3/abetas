package com.maccari.abet.domain.entity;

import java.util.ArrayList;
import java.util.List;

public class WebTask {
	private int id;

	private String coordinator;

	private String title;
	
	private List<WebUser> assignees;

	private List<String> programs;

	private String outcome;

	private String description;
	
	public WebTask() {
		assignees = new ArrayList<WebUser>();
	}
	
	public WebTask(List<WebUser> assignees) {
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

	public List<WebUser> getAssignees() {
		return assignees;
	}

	public void setAssignees(List<WebUser> assignees) {
		this.assignees = assignees;
	}

	public List<String> getPrograms() {
		return programs;
	}

	public void setPrograms(List<String> programs) {
		this.programs = programs;
	}

	public String getOutcome() {
		return outcome;
	}

	public void setOutcome(String outcome) {
		this.outcome = outcome;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	//for debugging
	public String toString() {
		String result = "";
		
		result += title + "\n";
		result += assignees + "\n" + programs;
		
		return result;
	}
}
