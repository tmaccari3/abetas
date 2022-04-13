package com.maccari.abet.domain.entity.web;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

/*
 * DocumentSearch.java 
 * Author: Thomas Maccari
 * 
 * Description: This class serves as a form backing object for 
 * an email message to be sent
 * 
 */

public class WebEmail {
	private String to;
	
	@NotEmpty(message = "Required")
	@Email(message = "Enter a valid email address")
	private String from;
	
	@Size(min=8, message = "Password must be at least 8 characters.")
	private String password;
	
	@Size(min=8, message = "Password must be at least 8 characters.")
	private String confPassword;
	
	private String subject;
	
	private String body;

	public String getTo() {
		return to;
	}

	public void setTo(String to) {
		this.to = to;
	}

	public String getFrom() {
		return from;
	}

	public void setFrom(String from) {
		this.from = from;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfPassword() {
		return confPassword;
	}

	public void setConfPassword(String confPassword) {
		this.confPassword = confPassword;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}
}
