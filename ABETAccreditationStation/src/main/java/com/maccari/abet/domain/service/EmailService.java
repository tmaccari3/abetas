package com.maccari.abet.domain.service;

import javax.mail.MessagingException;

import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.web.WebEmail;

/*
 * EmailService.java 
 * Author: Thomas Maccari
 * 
 * Description: A more specific type of service interface, this interface foregoes
 * the CRUD definitions of the Service interface and has only methods required 
 * for sending emails.
 * 
 */

@Component
public interface EmailService {
	void sendEmail(String to, String from, String subject, String text);
	
	void sendRequestEmail(WebEmail email) throws MessagingException;
	
	void sendRegistrationEmail(String to, String from, String password) throws MessagingException;
}
