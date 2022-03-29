package com.maccari.abet.domain.entity.web;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.StudentOutcome;

/*
 * DocumentSearch.java 
 * Author: Thomas Maccari
 * 
 * Description: This class serves as a form backing object for 
 * a Program.
 * 
 */

public class WebProgram {
	private int id;
	
	@NotEmpty(message = "Required")
	@Size(min=1, max=100, message = "Name must be between 1 and 100 characters.")
	private String name;
	
	private boolean active;
	
	private List<StudentOutcome> outcomes;
	
	private boolean newRow;
	
	public WebProgram() {
		
	}
	
	public WebProgram(String name, boolean newRow) {
		this.name = name;
		this.active = true;
		this.newRow = newRow;
	}
	
	public WebProgram(Program program) {
		this.id = program.getId();
		this.name = program.getName();
		this.active = program.isActive();
		this.outcomes = program.getOutcomes();
		this.newRow = false;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isActive() {
		return active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
	
	public List<StudentOutcome> getOutcomes() {
		return outcomes;
	}
	
	public void setOutcomes(List<StudentOutcome> outcomes) {
		this.outcomes = outcomes;
	}

	public boolean isNewRow() {
		return newRow;
	}

	public void setNewRow(boolean newRow) {
		this.newRow = newRow;
	}
}
