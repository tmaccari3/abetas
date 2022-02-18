package com.maccari.abet.domain.entity;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class WebUser {
	@NotEmpty(message = "Required")
	@Email(message = "Enter a valid email address")
	private String email;
	
	private List<String> program;

	private List<String> roles;

	public WebUser() {
		
	}
	
	public WebUser(String email, List<String> roles) {
		this.email = email;
		this.roles = roles;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
