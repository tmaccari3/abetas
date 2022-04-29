package com.maccari.abet.domain.entity;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.domain.Persistable;

/*
 * User.java 
 * Author: Thomas Maccari
 * 
 * Description: The domain object that represents a tuple from the user table.
 * 
 */

@Entity
@Table(name = "account")
public class User implements Persistable<UUID> {
	@Id
	private String email;
	
	@ManyToMany
	@JoinTable(
	  name = "user_program", 
	  joinColumns = @JoinColumn(name = "email"), 
	  inverseJoinColumns = @JoinColumn(name = "prog_id"))
	private List<ProgramData> programs;
	
	
	private String password;
	
	@Transient
	private String confPassword;
	
	@Transient
    private boolean update;
	
	@ElementCollection
    @CollectionTable(name = "authority", joinColumns = @JoinColumn(name = "email"))
    @Column(name = "role")
	private List<String> roles;

	public User() {
		roles = new ArrayList<String>();	
		programs = new ArrayList<ProgramData>();
	}
	
	public User(String email, List<String> roles, List<ProgramData> programs) {
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

	public List<ProgramData> getPrograms() {
		return programs;
	}

	public void setPrograms(List<ProgramData> programs) {
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
	
    public boolean isUpdate() {
        return this.update;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
	
	@Override
	public UUID getId() {
		return this.getId();
	}

	@Override
	public boolean isNew() {
		return !this.update;
	}
	
    @PrePersist
    @PostLoad
    void markUpdated() {
        this.update = true;
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
			result += item.substring(5);
		}
		
		return result;
	}
	
	public String formattedPrograms() {
		if(programs.size() == 0) {
			return "N/A";
		}
		
		String result = "";
		int iter;
		for(iter = 0; iter < programs.size() - 1; iter++) {
			result += programs.get(iter).getName() + ", ";
		}
		String item = programs.get(iter).getName();
		if(item != null) {
			result += item;
		}
		
		return result;
	}
}
