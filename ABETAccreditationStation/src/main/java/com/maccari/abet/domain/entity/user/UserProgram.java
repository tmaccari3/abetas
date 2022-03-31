package com.maccari.abet.domain.entity.user;

import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

import com.maccari.abet.domain.entity.StudentOutcome;

@Entity
@Table(name = "user_program")
public class UserProgram {
	@Id
	@Column(name = "prog_id")
	private int id;
	
	@Column(name = "program")
	private String name;
	
	@Column(name = "email")
	private String email;
	
	private boolean active;
	
	@ElementCollection
    @CollectionTable(name = "student_outcome", joinColumns = @JoinColumn(name = "prog_id"))
	private List<StudentOutcome> outcomes;

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

	public List<StudentOutcome> getOutcomes() {
		return outcomes;
	}

	public void setOutcomes(List<StudentOutcome> outcomes) {
		this.outcomes = outcomes;
	}

	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}
}
