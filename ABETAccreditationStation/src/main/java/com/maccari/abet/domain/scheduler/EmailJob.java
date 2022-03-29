package com.maccari.abet.domain.scheduler;

/*
 * EmailJob.java 
 * Author: Thomas Maccari
 * 
 * Description: This implementation of the Job class sends the an email stored
 * in a job's data map.
 * 
 */

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.service.EmailService;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

@Component
public class EmailJob implements Job {
	@Autowired
	private EmailService emailService;
	
	@Override
	public void execute(JobExecutionContext context) throws JobExecutionException {
		JobDataMap jobDataMap = context.getMergedJobDataMap();
		String subject = jobDataMap.getString("subject");
        String body = jobDataMap.getString("body");
        String to = jobDataMap.getString("to");
        String from = jobDataMap.getString("from");
        
		System.out.println("*** EMAILING " + to + " NOW ***");
        
        emailService.sendEmail(to, from, subject, body);
	}
}
