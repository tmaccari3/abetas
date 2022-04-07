package com.maccari.abet.domain.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

/*
 * Task.java 
 * Author: Thomas Maccari
 * 
 * Description: The domain object that represents a tuple from the task table.
 * 
 */

@Entity
@Table(name = "task")
public class Task {
	@Id
    @SequenceGenerator(name = "task_id_seq",
                       sequenceName = "task_id_seq",
                       allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "task_id_seq")
	private int id;

	private String coordinator;

	@NotEmpty(message = "*required")
	private String title;

	@Transient
	private List<String> assignees;

	@Transient
	private List<ProgramData> programs;

	@Transient
	private List<StudentOutcomeData> outcomes;

	@NotEmpty(message = "*required")
	private String description;
	
	@Column(name = "assign_date")
	private Timestamp assignDate;
	
	@Column(name = "submit_date")
	private Timestamp submitDate;
	
	@Column(name = "due_date")
	private Date dueDate;
	
	private File file;
	
	private boolean submitted = false;
	
	private boolean complete = false;
	
	public Task() {
		assignees = new ArrayList<String>();
		programs = new ArrayList<ProgramData>();
		outcomes = new ArrayList<StudentOutcomeData>();
	}
	
	public Task(List<String> assignees) {
		this.assignees = assignees;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getCoordinator() {
		return coordinator;
	}

	public void setCoordinator(String coordinator) {
		this.coordinator = coordinator;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<String> getAssignees() {
		return assignees;
	}

	public void setAssignees(List<String> assignees) {
		this.assignees = assignees;
	}

	public List<ProgramData> getPrograms() {
		return programs;
	}

	public void setPrograms(List<ProgramData> programs) {
		this.programs = programs;
	}

	public List<StudentOutcomeData> getOutcomes() {
		return outcomes;
	}

	public void setOutcomes(List<StudentOutcomeData> outcomes) {
		this.outcomes = outcomes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public boolean isComplete() {
		return complete;
	}

	public void setComplete(boolean complete) {
		this.complete = complete;
	}

	public Timestamp getAssignDate() {
		return assignDate;
	}

	public void setAssignDate(Timestamp assignDate) {
		this.assignDate = assignDate;
	}

	public Date getDueDate() {
		return dueDate;
	}

	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}

	public synchronized File getFile() {
		return file;
	}

	public synchronized void setFile(File file) {
		this.file = file;
	}

	public boolean isSubmitted() {
		return submitted;
	}

	public void setSubmitted(boolean submitted) {
		this.submitted = submitted;
	}
	public Timestamp getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Timestamp submitDate) {
		this.submitDate = submitDate;
	}
	
	// Returns a string representation the status based on the boolean value
	public String getStatus() {
		String result = "incomplete";
		if(submitted) {
			result = "submitted";
		}
		if(complete) {
			result = "complete";
		}
		
		return result;
	}
	
	public String getFormattedDate() {
		if(assignDate == null) {
			return "";
		}
		
		else {
			return new SimpleDateFormat("yyyy-MM-dd hh:mm:ss").format(assignDate);
		}
	}
	
	//for debugging
	public String toString() {
		String result = "";
		
		result += id + ": " + title + "\n";
		result += assignees + "\n" + programs + "\n";
		result += assignDate;
		
		return result;
	}
}
