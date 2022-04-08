package com.maccari.abet.domain.entity.relation.task;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "assigned")
public class TaskAssignee implements Serializable {
	@Transient
	private static final long serialVersionUID = 610297866235370581L;

	@Id
	@Column(name = "task_id")
	private int taskId;
	
	@Id
	@Column(name = "assignee")
	private String email;
	
	public TaskAssignee() {
		
	}

	public TaskAssignee(String email) {
		this.email = email;
	}

	public TaskAssignee(int taskId, String email) {
		this.taskId = taskId;
		this.email = email;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
}
