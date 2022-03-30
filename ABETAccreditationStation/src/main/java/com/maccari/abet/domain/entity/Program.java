package com.maccari.abet.domain.entity;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

/*
 * Program.java 
 * Author: Thomas Maccari
 * 
 * Description: The domain object that represents a tuple from the program table.
 * 
 */

@Entity
@Table(name = "program")
public class Program {
	@Id
	private int id;
	
	private String name;
	
	private boolean active;
	
	@ElementCollection
    @CollectionTable(name = "student_outcome", joinColumns = @JoinColumn(name = "prog_id"))
	private List<StudentOutcome> outcomes;
	
	public Program() {
		
	}
	
	public Program(String name) {
		this.name = name;
		active = true;
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
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof Program) {
			Program program = (Program) other;
			return this.id == program.getId();
		}
		
		return false;
	}
	
	// For testing
	public String toString() {
		String result = "";
		//result += id + " ";
		result += name;
		/*for(StudentOutcome outcome : outcomes) {
			result += outcome.getName() + "\n";
		}*/
		
		return result;
	}
}
