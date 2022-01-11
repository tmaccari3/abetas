package com.maccari.abet.web;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.maccari.abet.domain.entity.User;

@Component
public class UserValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return User.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		User user = (User) target;

		String password = user.getPassword();
		String confPassword = user.getConfPassword();

		if (!password.equals(confPassword)) {
			errors.rejectValue("password", "user.password.misMatch");
		}
		
		if (user.getRoles().isEmpty()) {
			errors.rejectValue("roles", "webUser.roles.empty");
		}
	}
}
