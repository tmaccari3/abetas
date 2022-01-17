package com.maccari.abet.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.maccari.abet.domain.entity.Task;

@Component
public class TaskValidator implements Validator {
	
	@Override
	public boolean supports(Class<?> clazz) {
		return Task.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		Task task = (Task) target;

		if (task.getPrograms().isEmpty()) {
			errors.rejectValue("programs", "task.program.empty");
		}
		
		if (task.getAssignees().isEmpty()) {
			errors.rejectValue("assignees", "task.assignees.empty");
		}
	}
}
