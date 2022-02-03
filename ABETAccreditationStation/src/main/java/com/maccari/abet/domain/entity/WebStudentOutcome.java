package com.maccari.abet.domain.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class WebStudentOutcome {
	private int id;
	
	@NotEmpty(message = "Required")
	@Size(min=1, max=100, message = "Name must be between 1 and 100 characters.")
	private String name;
	
	private boolean active;
	
	private boolean newRow;
	
	public WebStudentOutcome() {
		
	}
	
	public WebStudentOutcome(String name, boolean newRow) {
		this.name = name;
		this.active = true;
		this.newRow = newRow;
	}
	
	public WebStudentOutcome(StudentOutcome studentOutcome) {
		this.id = studentOutcome.getId();
		this.name = studentOutcome.getName();
		this.active = studentOutcome.isActive();
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
	
	public boolean isNewRow() {
		return newRow;
	}
	
	public void setNewRow(boolean newRow) {
		this.newRow = newRow;
	}
}
