package com.maccari.abet.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.maccari.abet.domain.entity.WebDocument;

@Component
public class DocumentValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return WebDocument.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		WebDocument doc = (WebDocument) target;

		if(doc.getPrograms().isEmpty()) {
			errors.rejectValue("programs", "program.empty");
		}
		
		if(doc.getOutcomes().isEmpty() && !doc.getPrograms().isEmpty()) {
			errors.rejectValue("programs", "outcome.empty");
		}
		
		if(doc.getUploadedFile() == null) {
			errors.rejectValue("uploadedFile", "file.null");
		}
	}
}
