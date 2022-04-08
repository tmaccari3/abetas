package com.maccari.abet.domain.entity.relation.task;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.maccari.abet.domain.entity.StudentOutcome;

@Entity
@Table(name = "task_outcome")
@IdClass(TaskStudentOutcome.class)
public class TaskStudentOutcome implements StudentOutcome, Serializable {
	@Transient
	private static final long serialVersionUID = 6745242672032560637L;

	@Id
	@Column(name = "task_id")
	private int taskId;
	
	@Id
	@Column(name = "outcome_id")
	private int outcomeId;
	
	@Column(name = "prog_id")
	private int progId;
	
	private String name;
	
	public TaskStudentOutcome() {
		
	}

	public TaskStudentOutcome(int outcomeId, int progId, String name) {
		this.outcomeId = outcomeId;
		this.progId = progId;
		this.name = name;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getOutcomeId() {
		return outcomeId;
	}

	public void setOutcomeId(int outcomeId) {
		this.outcomeId = outcomeId;
	}
	
	public int getProgId() {
		return this .progId;
	}
	
	public void setProgId(int progId) {
		this.progId = progId;
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getId() {
		return this.outcomeId;
	}
}
