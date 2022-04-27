package com.maccari.abet.domain.scheduler;

import javax.annotation.PostConstruct;

import org.quartz.JobDetail;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;;

/*
 * SpringQrtzScheduler.java 
 * Author: Thomas Maccari
 * 
 * Description: This configuration class instantiates factory beans for jobs,
 * triggers, and schedulers.
 * 
 */

@Configuration
@EnableAutoConfiguration
public class SpringQrtzScheduler {

    Logger logger = LoggerFactory.getLogger(getClass());
    
    private static final int weekInSeconds = 604800;
    
    // static names for jobs and triggers
    private static final String jobName = "RDJob";
    
    private static final String triggerName = "RDTrigger";

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        logger.info("Hello world from Spring...");
    }
    
    @Bean
    public JobTriggerNameContainer jobTriggerNameContainer() {
    	return new JobTriggerNameContainer(jobName, triggerName);
    }
    
    @Bean
    public SpringBeanJobFactory springBeanJobFactory() {
        AutoWiringSpringBeanJobFactory jobFactory = new AutoWiringSpringBeanJobFactory();
        logger.debug("Configuring Job factory");
        jobFactory.setApplicationContext(applicationContext);
        
        return jobFactory;
    }

    @Bean
    public SchedulerFactoryBean scheduler() {
        SchedulerFactoryBean schedulerFactory = new SchedulerFactoryBean();
        schedulerFactory.setConfigLocation(new ClassPathResource("quartz.properties"));

        logger.debug("Setting the Scheduler up");
        schedulerFactory.setJobFactory(springBeanJobFactory());

        // Comment the following line to use the default Quartz job store.
        //schedulerFactory.setDataSource(quartzDataSource);

        return schedulerFactory;
    }

    // This factory can take parameters specified here when producing a job
    @Bean
    @Scope("prototype")
    public JobDetailFactoryBean jobDetail(String group) {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(EmailJob.class);
        jobDetailFactory.setDescription("Invoke Email Job service...");
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setName(jobName);
        jobDetailFactory.setGroup(group);
        
        return jobDetailFactory;
    }

    // This factory can take parameters specified here when producing a trigger
    @Bean
    @Scope("prototype")
    public SimpleTriggerFactoryBean trigger(JobDetail job, String group, 
    	int repeatCount, int interval) {
        SimpleTriggerFactoryBean triggerFactory = new SimpleTriggerFactoryBean();
        if(job != null) {
            triggerFactory.setJobDetail(job);	
        }

        logger.info("Configuring trigger to fire every {} seconds", interval);

        triggerFactory.setRepeatInterval((interval) * 1000);
        triggerFactory.setRepeatCount(repeatCount);
        triggerFactory.setStartDelay(5);
        triggerFactory.setName(triggerName);
        triggerFactory.setGroup(group);
        
        return triggerFactory;
    }
}
