package com.maccari.abet.domain.entity.relation.task.id;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.IdClass;
import javax.persistence.Table;

import com.maccari.abet.domain.entity.relation.task.TaskStudentOutcome;

public class TaskStudentOutcomeId {
	private int taskId;
	
	private int outcomeId;
	
	public TaskStudentOutcomeId() {
		
	}
	
	public TaskStudentOutcomeId(int taskId, int outcomeId) {
		this.taskId = taskId;
		this.outcomeId = outcomeId;
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

	@Override
	public int hashCode() {
		return Objects.hash(outcomeId, taskId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TaskStudentOutcomeId other = (TaskStudentOutcomeId) obj;
		return outcomeId == other.outcomeId && taskId == other.taskId;
	}
}
