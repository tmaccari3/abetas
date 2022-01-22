package com.maccari.abet.domain.service;

import org.springframework.stereotype.Component;

@Component
public interface EmailService {
	void sendEmail(String to, String from, String subject, String text);
}
