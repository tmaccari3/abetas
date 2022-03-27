package com.maccari.abet.web.validation;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import com.maccari.abet.domain.entity.web.WebDocument;

@Component
public class DocumentValidator implements Validator {
	@Override
	public boolean supports(Class<?> clazz) {
		return WebDocument.class.isAssignableFrom(clazz);
	}
	
	@Override
	public void validate(Object target, Errors errors) {
		WebDocument doc = (WebDocument) target;

		if(doc.getPrograms().isEmpty() && doc.getFullPrograms().isEmpty()) {
			System.out.println("no prog");
			errors.rejectValue("programs", "program.empty");
		}
		
		if(doc.getOutcomes().isEmpty() && !doc.getPrograms().isEmpty()) {
			System.out.println("no outcome");
			errors.rejectValue("programs", "outcome.empty");
		}
		
		if(doc.getUploadedFile() == null) {
			System.out.println("no file");
			errors.rejectValue("uploadedFile", "file.null");
		}
		
		if(doc.getTags().isEmpty()) {
			System.out.println("no tags");
			errors.rejectValue("tags", "tags.empty");
		}
	}
}
