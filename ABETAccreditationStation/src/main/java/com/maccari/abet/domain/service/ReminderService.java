package com.maccari.abet.domain.service;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.web.WebEmail;

@Component
public class ReminderService {
	@Autowired
	private ObjectProvider<JobDetailFactoryBean> jobFactoryProvider;
	
	@Autowired
	private ObjectProvider<SimpleTriggerFactoryBean> triggerFactoryProvider;
	
	@Autowired
	private SchedulerFactoryBean schedulerFactory;
	
	@Autowired
	private EmailService emailService;
	
	public void sendImmediately(WebEmail email) {
		emailService.sendEmail(email.getTo(), email.getFrom(), email.getSubject(), 
			email.getBody());
	}
	
	public void scheduleReminder(WebEmail email) {
		try {
			JobDetail job = jobFactoryProvider.getObject(email.getTo()).getObject();
			job.getJobDataMap().put("from", email.getFrom());
			job.getJobDataMap().put("to", email.getTo());
			job.getJobDataMap().put("subject", email.getSubject());
			job.getJobDataMap().put("body", email.getBody());
			
			SimpleTriggerFactoryBean triggerFactory = triggerFactoryProvider
					.getObject(null, email.getTo(), 2, 10);
			Trigger trigger = triggerFactory.getObject();
			Scheduler scheduler = schedulerFactory.getObject();

			scheduler.start();
			scheduler.scheduleJob(job, trigger);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}
}
