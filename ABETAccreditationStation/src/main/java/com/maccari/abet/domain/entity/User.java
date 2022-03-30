package com.maccari.abet.domain.entity;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

/*
 * User.java 
 * Author: Thomas Maccari
 * 
 * Description: The domain object that represents a tuple from the user table.
 * 
 */

@Entity
@Table(name = "account")
public class User {
	@Id
	private String email;
	
	@ManyToMany
	@JoinTable(
	  name = "user_program", 
	  joinColumns = @JoinColumn(name = "email"), 
	  inverseJoinColumns = @JoinColumn(name = "prog_id"))
	private List<Program> programs;
	
	private String password;
	
	@Transient
	private String confPassword;
	
	@ElementCollection
    @CollectionTable(name = "authority", joinColumns = @JoinColumn(name = "email"))
    @Column(name = "role")
	private List<String> roles;

	public User() {
		roles = new ArrayList<String>();	
		programs = new ArrayList<Program>();
	}
	
	public User(String email, List<String> roles, List<Program> programs) {
		this.email = email;
		this.roles = roles;
		this.programs = programs;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public List<Program> getPrograms() {
		return programs;
	}

	public void setPrograms(List<Program> programs) {
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
	
	public String formattedRoles() {
		if(roles.size() == 0) {
			return "N/A";
		}
		
		String result = "";
		int iter;
		for(iter = 0; iter < roles.size() - 1; iter++) {
			result += roles.get(iter).toString().substring(5) + ", ";
		}
		String item = roles.get(iter);
		if(item != null) {
			result += item.toString();
		}
		
		return result;
	}
}
