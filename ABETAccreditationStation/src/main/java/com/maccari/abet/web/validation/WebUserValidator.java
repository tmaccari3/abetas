package com.maccari.abet.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.maccari.abet.domain.entity.User;
import com.maccari.abet.domain.entity.web.WebUser;

@Component
public class WebUserValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		WebUser user = (WebUser) target;

		if (user.getRoles().isEmpty()) {
			errors.rejectValue("roles", "webUser.roles.empty");
		}
	}
}
