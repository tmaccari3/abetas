package com.maccari.abet.domain.entity;

import java.util.ArrayList;
import java.util.List;

import com.maccari.abet.utility.WebList;

public class User {
	private String email;
	
	private WebList<Program> programs;
	
	private String password;
	
	private String confPassword;
	
	private WebList<String> roles;
	
	public User() {
		roles = new WebList<String>();
		programs = new WebList<Program>();
	}
	
	public User(String email, List<String> roles, List<Program> programs) {
		this.email = email;
		this.roles = new WebList<String>((ArrayList<String>) roles);
		this.programs = new WebList<Program>((ArrayList<Program>) programs);
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	
	public WebList<Program> getPrograms() {
		return programs;
	}

	public void setPrograms(WebList<Program> programs) {
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

	public WebList<String> getRoles() {
		return roles;
	}

	public void setRoles(WebList<String> roles) {
		this.roles = roles;
	}
}
