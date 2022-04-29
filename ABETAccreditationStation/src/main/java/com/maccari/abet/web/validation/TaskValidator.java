package com.maccari.abet.web.validation;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.maccari.abet.domain.entity.web.WebTask;

@Component
public class TaskValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return WebTask.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		WebTask task = (WebTask) target;

		if(task.getPrograms().isEmpty()) {
			errors.rejectValue("programs", "program.empty");
		}
		
		if(task.getOutcomes().isEmpty() && !task.getPrograms().isEmpty()) {
			errors.rejectValue("programs", "outcome.empty");
		}
		
		if(task.getAssignees().isEmpty()) {
			errors.rejectValue("assignees", "task.assignees.empty");
		}
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date(System.currentTimeMillis()));
		calendar.add(Calendar.WEEK_OF_MONTH, 1);
		
		if(task.getDueDate().before(calendar.getTime())) {
			errors.rejectValue("dueDate", "dueDate.invalid");
		}
		
		/*if(task.getUploadedFile() == null) {
			errors.rejectValue("uploadedFile", "file.null");
		}*/
	}
}
