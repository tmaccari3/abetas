package com.maccari.abet.domain.service;

import org.springframework.stereotype.Component;

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
}
