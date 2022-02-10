package com.maccari.abet.domain.entity;

import java.util.List;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class Program {
	private int id;
	
	private String name;
	
	private boolean active;
	
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
	
	//for testing
	public String toString() {
		String result = "";
		result += id + " ";
		result += name + "\n";
		/*for(StudentOutcome outcome : outcomes) {
			result += outcome.getName() + "\n";
		}*/
		
		return result;
	}
}
