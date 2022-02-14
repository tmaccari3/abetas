package com.maccari.abet.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.maccari.abet.domain.entity.WebTask;

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
			errors.rejectValue("programs", "task.program.empty");
		}
		
		if(task.getOutcomes().isEmpty() && !task.getPrograms().isEmpty()) {
			errors.rejectValue("programs", "task.outcome.empty");
		}
		
		if(task.getAssignees().isEmpty()) {
			errors.rejectValue("assignees", "task.assignees.empty");
		}
		
		if(task.getUploadedFile() == null) {
			errors.rejectValue("uploadedFile", "task.file.null");
		}
	}
}
