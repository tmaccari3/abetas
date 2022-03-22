package com.maccari.abet.domain.scheduler;

public class JobTriggerNameContainer {
	private String jobName;
	
	private String triggerName;
	
	public JobTriggerNameContainer(String jobName, String triggerName) {
		this.jobName = jobName;
		this.triggerName = triggerName;
	}
	
	public String getJobName() {
		return jobName;
	}
	
	public void setJobName(String jobName) {
		this.jobName = jobName;
	}
	
	public String getTriggerName() {
		return triggerName;
	}
	
	public void setTriggerName(String triggerName) {
		this.triggerName = triggerName;
	}
}
