package com.maccari.abet.domain.scheduler;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.quartz.QuartzDataSource;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;;

//make wrapper class for getObject() method
@Configuration
@EnableAutoConfiguration
public class SpringQrtzScheduler {

    Logger logger = LoggerFactory.getLogger(getClass());
    
    static final int weekInSeconds = 604800;

    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public void init() {
        logger.info("Hello world from Spring...");
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

    @Bean
    @Scope("prototype")
    public JobDetailFactoryBean jobDetail(String group) {
        JobDetailFactoryBean jobDetailFactory = new JobDetailFactoryBean();
        jobDetailFactory.setJobClass(EmailJob.class);
        jobDetailFactory.setDescription("Invoke Email Job service...");
        jobDetailFactory.setDurability(true);
        jobDetailFactory.setName("Reminder_JobDetail");
        jobDetailFactory.setGroup(group);
        
        return jobDetailFactory;
    }

    @Bean
    @Scope("prototype")
    public SimpleTriggerFactoryBean trigger(JobDetail job, String group, 
    	int repeatCount, int interval) {
        SimpleTriggerFactoryBean triggerFactory = new SimpleTriggerFactoryBean();
        if(job != null) {
            triggerFactory.setJobDetail(job);	
        }

        logger.info("Configuring trigger to fire every {} seconds", interval);

        triggerFactory.setRepeatInterval(interval * 1000);
        triggerFactory.setRepeatCount(repeatCount);
        triggerFactory.setStartDelay(5);
        triggerFactory.setName("Reminder_Trigger");
        triggerFactory.setGroup(group);
        
        return triggerFactory;
    }
}
