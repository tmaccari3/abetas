package com.maccari.abet.domain.entity;

import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/*
 * Program.java 
 * Author: Thomas Maccari
 * 
 * Description: The domain object that represents a tuple from the program table.
 * 
 */

@Entity
@Table(name = "program")
public class ProgramData implements Program {
	@Id
    @SequenceGenerator(name = "program_id_seq",
                       sequenceName = "program_id_seq",
                       allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "program_id_seq")
	private int id;
	
	private String name;
	
	private boolean active;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "prog_id")
	private List<StudentOutcomeData> outcomes;
	
	public ProgramData() {
		
	}
	
	public ProgramData(String name) {
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
	public List<StudentOutcomeData> getOutcomes() {
		return outcomes;
	}
	public void setOutcomes(List<StudentOutcomeData> outcomes) {
		this.outcomes = outcomes;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof ProgramData) {
			ProgramData program = (ProgramData) other;
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