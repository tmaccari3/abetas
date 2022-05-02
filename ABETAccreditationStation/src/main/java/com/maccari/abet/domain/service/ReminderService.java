package com.maccari.abet.domain.service;

import javax.annotation.PostConstruct;

import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.web.WebEmail;
import com.maccari.abet.domain.entity.web.WebTask;
import com.maccari.abet.domain.scheduler.JobTriggerNameContainer;

/*
 * ReminderService.java 
 * Author: Thomas Maccari
 * 
 * Description: A unique service that handles reminder scheduling and managing 
 * the jobs that send the reminders.
 * 
 */

@Component
public class ReminderService {
	@Autowired
	private ObjectProvider<JobDetailFactoryBean> jobFactoryProvider;
	
	@Autowired
	private ObjectProvider<SimpleTriggerFactoryBean> triggerFactoryProvider;
	
	@Autowired
	private SchedulerFactoryBean schedulerFactory;
	
	private Scheduler scheduler;
	
	@Autowired
	private JobTriggerNameContainer JTNameContainer;
	
	@Autowired
	private EmailService emailService;
	
	@PostConstruct
	public void initScheduler() {
		this.scheduler = schedulerFactory.getObject();
	}
	
	public void sendImmediately(WebEmail email, WebTask task) {
		email.setSubject("ABET Accrediation Station: Task Notification");
		email.setBody(task.toString());
		emailService.sendEmail(email.getTo(), email.getFrom(), email.getSubject(), 
			email.getBody());
	}
	
	// Schedule a reminder given an email and a task
	public void scheduleReminder(WebEmail email, WebTask task) {
		try {
			// Set group name to sender email + task id for a unique key combination
			JobDetail job = jobFactoryProvider.getObject(email.getTo() + task.getId())
					.getObject();
			
			JobDetail assessCordJob = jobFactoryProvider.getObject(task.getCoordinator() + task.getId())
					.getObject();
			email.setSubject("ABET Accrediation Station: Task Notification");
			email.setBody(task.toString());
			
			assessCordJob.getJobDataMap().put("from", email.getFrom());
			assessCordJob.getJobDataMap().put("to", task.getCoordinator());
			assessCordJob.getJobDataMap().put("subject", email.getSubject());
			String body = "This task is a week overdue. Consider reaching out to "
					+ "its assignees.\n\n" + task.toString() + "\nAssignee List: \n";
			for(String assignee : task.getAssignees()) {
				body += assignee + "\n";
			}
			assessCordJob.getJobDataMap().put("body", body);
			
			job.getJobDataMap().put("from", email.getFrom());
			job.getJobDataMap().put("to", email.getTo());
			job.getJobDataMap().put("subject", email.getSubject());
			job.getJobDataMap().put("body", email.getBody());
			
			// Produce a trigger with group name same as job, interval of execution 
		    // and the amount of times to repeat the trigger
			SimpleTriggerFactoryBean triggerFactory = triggerFactoryProvider
					.getObject(null, email.getTo() + task.getId(), 3, 60, 5);
			Trigger trigger = triggerFactory.getObject();
			
			SimpleTriggerFactoryBean assessCordTriggerFactory = triggerFactoryProvider
					.getObject(null, task.getCoordinator() + task.getId(), 0, 0, 180 * 1000);
			Trigger assessCordTrigger = assessCordTriggerFactory.getObject();

			scheduler.start();
			scheduler.scheduleJob(job, trigger);
			scheduler.scheduleJob(assessCordJob, assessCordTrigger);
		} catch (Exception e) {
			System.out.println("Job schedule error: " + e.getMessage());
		}
	}
	
	// Given the group, delete the job by its key.
	public void deleteJob(String group) {
		// The job factory produces jobs with a set name. 
		// Combine this with the given group to get the job's key.
		JobKey key = new JobKey(JTNameContainer.getJobName(), group);
		try {
			scheduler.deleteJob(key);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void unscheduleJob(String group) {
		// TODO Auto-generated method stub
	}
}
