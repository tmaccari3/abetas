package com.maccari.abet.domain.entity.web;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.ProgramData;
import com.maccari.abet.domain.entity.StudentOutcomeData;

/*
 * DocumentSearch.java 
 * Author: Thomas Maccari
 * 
 * Description: This class serves as a form backing object for 
 * a Program.
 * 
 */

public class WebProgram implements Program {
	private int id;
	
	@NotEmpty(message = "Required")
	@Size(min=1, max=100, message = "Name must be between 1 and 100 characters.")
	private String name;
	
	private boolean active;
	
	private List<StudentOutcomeData> outcomes;
	
	private boolean newRow;
	
	public WebProgram() {
		
	}
	
	public WebProgram(String name, boolean newRow) {
		this.name = name;
		this.active = true;
		this.newRow = newRow;
	}
	
	public WebProgram(ProgramData program) {
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
	
	public List<StudentOutcomeData> getOutcomes() {
		return outcomes;
	}
	
	public void setOutcomes(List<StudentOutcomeData> outcomes) {
		this.outcomes = outcomes;
	}

	public boolean isNewRow() {
		return newRow;
	}

	public void setNewRow(boolean newRow) {
		this.newRow = newRow;
	}
}
