package com.maccari.abet.domain.entity;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;

public class WebUser {
	@NotEmpty(message = "Required")
	@Email(message = "Enter a valid email address")
	private String email;
	
	private List<Integer> programIds;
	
	private List<Program> programs;

	private List<String> roles;

	public WebUser() {
		
	}
	
	public WebUser(String email, List<String> roles, List<Program> programs) {
		this.email = email;
		this.roles = roles;
		this.programs = programs;
		this.programIds = new ArrayList<Integer>();
		for(Program program : this.programs) {
			this.programIds.add(program.getId());
		}
	}
	
	public WebUser(List<String> roles, String email, List<Integer> programIds) {
		this.email = email;
		this.roles = roles;
		this.programIds = programIds;
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

	public List<Program> getPrograms() {
		return programs;
	}

	public void setPrograms(List<Program> programs) {
		this.programs = programs;
	}

	public List<Integer> getProgramIds() {
		return programIds;
	}

	public void setProgramIds(List<Integer> programIds) {
		this.programIds = programIds;
	}
}
