package com.maccari.abet.domain.service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.web.WebEmail;

/*
 * EmailService.java 
 * Author: Thomas Maccari
 * 
 * Implements: EmailService.java
 * 
 * Description: Provides an implementation for sending emails with the 
 * SimpleMailMessage and MimeMessage classes.
 * 
 */

@Component
public class EmailServiceImpl implements EmailService {

	@Autowired
	private JavaMailSender emailSender;

	public void sendEmail(String to, String from, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);

		emailSender.send(message);
	}

	public void sendRegistrationEmail(WebEmail email) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);
		String password = "\nRequested password: " + email.getPassword();

		helper.setTo(email.getTo());
		helper.setSubject(email.getSubject() + ": " + email.getFrom());
		helper.setText(email.getBody() + password);		

		emailSender.send(message);
	}
}
