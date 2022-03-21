package com.maccari.abet.domain.service;

import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.Trigger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.web.WebEmail;

@Component
public class ReminderService {
	@Autowired
	private JobDetailFactoryBean jobFactory;
	
	@Autowired
	private SimpleTriggerFactoryBean triggerFactory;
	
	@Autowired
	private SchedulerFactoryBean schedulerFactory;
	
	public void scheduleReminder(WebEmail email) {
		try {
			
			JobDetail job = jobFactory.getObject();
			job.getJobDataMap().put("from", email.getFrom());
			job.getJobDataMap().put("to", email.getTo());
			job.getJobDataMap().put("subject", email.getSubject());
			job.getJobDataMap().put("body", email.getBody());
			Trigger trigger = triggerFactory.getObject();
			Scheduler scheduler = schedulerFactory.getObject();

			scheduler.scheduleJob(job, trigger);
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	/*
	 * private JobDetail buildJobDetail(WebEmail email) { JobDataMap jobDataMap =
	 * new JobDataMap();
	 * 
	 * jobDataMap.put("email", email.getFrom()); jobDataMap.put("subject",
	 * email.getSubject()); jobDataMap.put("body", email.getBody());
	 * 
	 * return JobBuilder.newJob(EmailJob.class)
	 * .withIdentity(UUID.randomUUID().toString(), "email-jobs")
	 * .withDescription("Send Email Job") .usingJobData(jobDataMap) .storeDurably()
	 * .build(); }
	 * 
	 * private Trigger buildJobTrigger(JobDetail jobDetail, ZonedDateTime startAt) {
	 * return TriggerBuilder.newTrigger() .forJob(jobDetail)
	 * .withIdentity(jobDetail.getKey().getName(), "email-triggers")
	 * .withDescription("Send Email Trigger")
	 * .startAt(Date.from(startAt.toInstant()))
	 * .withSchedule(SimpleScheduleBuilder.simpleSchedule().
	 * withMisfireHandlingInstructionFireNow()) .build(); }
	 */

}
