package com.maccari.abet.domain.entity.web;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

import com.maccari.abet.domain.entity.ProgramData;

/*
 * DocumentSearch.java 
 * Author: Thomas Maccari
 * 
 * Description: This class serves as a form backing object for a User
 * 
 */

public class WebUser {
	@NotEmpty(message = "*Email required")
	@Email(message = "Enter a valid email address")
	private String email;
	
	private List<Integer> programIds;
	
	private List<ProgramData> programs;

	private List<String> roles;
	
	private String password = "";
	
	private String confPassword = "";

	public WebUser() {
		this.programIds = new ArrayList<Integer>();
	}
	
	public WebUser(String email, List<String> roles, List<ProgramData> programs) {
		this.email = email;
		this.roles = roles;
		this.programs = programs;
		this.programIds = new ArrayList<Integer>();
		for(ProgramData program : this.programs) {
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

	public List<ProgramData> getPrograms() {
		return programs;
	}

	public void setPrograms(List<ProgramData> programs) {
		this.programs = programs;
	}

	public List<Integer> getProgramIds() {
		return programIds;
	}

	public void setProgramIds(List<Integer> programIds) {
		this.programIds = programIds;
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
}
