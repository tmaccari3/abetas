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

	@Override
	public void sendEmail(String to, String from, String subject, String text) {
		SimpleMailMessage message = new SimpleMailMessage();

		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject);
		message.setText(text);

		emailSender.send(message);
	}
	
	@Override
	public void sendRequestEmail(WebEmail email) throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(email.getTo());
		helper.setSubject(email.getSubject() + ": " + email.getFrom());
		helper.setText(email.getBody());		

		emailSender.send(message);
	}
	
	@Override
	public void sendRegistrationEmail(String to, String from, String password) 
			throws MessagingException {
		MimeMessage message = emailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(message, true);

		helper.setTo(to);
		helper.setSubject("ABETAS Account Created");	

		String text = "An account has been created for: " + to;
		text += "with password: " + password;
		text += "\n\nIt is recommended that you change your password to something"
				+ " you are more likely to remember.";
		text += "\n\nYou can do this by clicking your email at the top left of the"
				+ " screen, then clicking the change password button on the Account"
				+ " Summary page.";
		helper.setText(text);

		emailSender.send(message);
	}
}
