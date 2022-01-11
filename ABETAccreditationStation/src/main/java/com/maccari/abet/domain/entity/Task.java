package com.maccari.abet.domain.entity;

import java.util.List;

public class Task {
	private int id;

	private String coordinator;

	private String title;

	private List<String> assignee;

	private List<String> program;

	private String outcome;

	private String description;

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

	public List<String> getAssignee() {
		return assignee;
	}

	public void setAssignee(List<String> assignee) {
		this.assignee = assignee;
	}

	public List<String> getProgram() {
		return program;
	}

	public void setProgram(List<String> program) {
		this.program = program;
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
}
