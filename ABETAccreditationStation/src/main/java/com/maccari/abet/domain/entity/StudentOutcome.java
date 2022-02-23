package com.maccari.abet.domain.entity;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class StudentOutcome {
	private int id;
	
	private int programId;
	
	@NotEmpty(message = "Required")
	@Size(min=1, max=100, message = "Name must be between 1 and 100 characters.")
	private String name;
	
	private boolean active;
	
	private boolean newRow;
	
	public StudentOutcome() {
		
	}
	
	public StudentOutcome(String name, boolean newRow) {
		this.name = name;
		this.setNewRow(newRow);
		this.active = true;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getProgramId() {
		return programId;
	}
	public void setProgramId(int programId) {
		this.programId = programId;
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
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof StudentOutcome) {
			StudentOutcome outcome = (StudentOutcome) other;
			
			return this.id == outcome.getId();
		}
		
		return false;
	}
	
	public String toString() {
		String result = "";
		//result += programId + " ";
		result += name + "\n";
		
		return result;
	}
}
