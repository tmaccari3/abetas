package com.maccari.abet.domain.entity.relation.task.id;

public class TaskProgramId {
	private int taskId;
	
	private int programId;

	public TaskProgramId(int taskId, int programId) {
		super();
		this.taskId = taskId;
		this.programId = programId;
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
}
