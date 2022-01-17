package com.maccari.abet.domain.entity;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

public class Task {
	private int id;

	private String coordinator;

	@NotEmpty(message = "*required")
	private String title;

	private List<String> assignees;

	private List<String> programs;

	@NotEmpty(message = "*required")
	private String outcome;

	@NotEmpty(message = "*required")
	private String description;
	
	private Date assignDate;
	
	private boolean complete = false;
	
	public Task() {
		assignees = new ArrayList<String>();
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

	public Date getAssignDate() {
		return assignDate;
	}

	public void setAssignDate(Date assignDate) {
		this.assignDate = assignDate;
	}
	
	//for debugging
	public String toString() {
		String result = "";
		
		result += title + "\n";
		result += assignees + "\n" + programs;
		
		return result;
	}
}
