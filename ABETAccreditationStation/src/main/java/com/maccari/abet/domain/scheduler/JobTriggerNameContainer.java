package com.maccari.abet.domain.scheduler;

/*
 * JobTRiggerNameContainer.java 
 * Author: Thomas Maccari
 * 
 * Description: This utility class makes storing the combination of a job name
 * and a trigger name simple. Since the combination of the 2 make up the key for
 * a job, so this comes in handy for creating those keys and accessing jobs.
 * 
 */

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
