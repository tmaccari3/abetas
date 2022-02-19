package com.maccari.abet.domain.entity;

import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

public class User {
	@NotEmpty(message = "Required")
	@Email(message = "Enter a valid email address")
	private String email;
	
	private List<String> programs;
	
	@Size(min=8, max=15, message = "Password must be between 8 and 15 characters.")
	private String password;
	
	@Size(min=8, max=15, message = "Password must be between 8 and 15 characters.")
	private String confPassword;
	
	private List<String> roles;
	
	public User() {
		
	}
	
	public User(String email, List<String> roles) {
		this.email = email;
		this.roles = roles;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public List<String> getPrograms() {
		return programs;
	}

	public void setPrograms(List<String> programs) {
		this.programs = programs;
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

	public List<String> getRoles() {
		return roles;
	}

	public void setRoles(List<String> roles) {
		this.roles = roles;
	}
}
