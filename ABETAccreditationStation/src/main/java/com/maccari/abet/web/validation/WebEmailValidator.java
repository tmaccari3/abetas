package com.maccari.abet.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.maccari.abet.domain.entity.web.WebEmail;

@Component
public class WebEmailValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return WebEmail.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		WebEmail user = (WebEmail) target;

		String password = user.getPassword();
		String confPassword = user.getConfPassword();

		if (!password.equals(confPassword)) {
			errors.rejectValue("password", "user.password.misMatch");
		}
	}
}
