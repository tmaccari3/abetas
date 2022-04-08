package com.maccari.abet.domain.entity.relation.task;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.maccari.abet.domain.entity.Program;

@Entity
@Table(name = "task_program")
public class TaskProgram implements Program, Serializable {
	@Transient
	private static final long serialVersionUID = -4859976926484594317L;

	@Id
	@Column(name = "task_id")
	private int taskId;
	
	@Id
	@Column(name = "program_id")
	private int programId;
	
	private String name;
	
	public TaskProgram() {
		
	}
	
	public TaskProgram(int programId) {
		this.programId = programId;
	}
	
	public TaskProgram(int programId, String name) {
		this.programId = programId;
		this.name = name;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public int getProgramId() {
		return programId;
	}

	public void setProgramId(int programId) {
		this.programId = programId;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public String getName() {
		return this.name;
	}
	
	@Override
	public int getId() {
		return this.programId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(programId, taskId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskProgram other = (TaskProgram) obj;
		return programId == other.programId && taskId == other.taskId;
	}
}
