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
	
	public void sendImmediately(WebEmail email) {
		emailService.sendEmail(email.getTo(), email.getFrom(), email.getSubject(), 
			email.getBody());
	}
	
	public void scheduleReminder(WebEmail email, WebTask task) {
		try {
			JobDetail job = jobFactoryProvider.getObject(email.getTo() + task.getId())
					.getObject();
			
			job.getJobDataMap().put("from", email.getFrom());
			job.getJobDataMap().put("to", email.getTo());
			job.getJobDataMap().put("subject", email.getSubject());
			job.getJobDataMap().put("body", email.getBody());
			
			SimpleTriggerFactoryBean triggerFactory = triggerFactoryProvider
					.getObject(null, email.getTo() + task.getId(), 2, 10);
			Trigger trigger = triggerFactory.getObject();

			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void deleteJob(String group) {
		JobKey key = new JobKey(JTNameContainer.getJobName(), group);
		try {
			scheduler.deleteJob(key);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
	
	public void unscheduleJob(String group) {
		
	}
}
