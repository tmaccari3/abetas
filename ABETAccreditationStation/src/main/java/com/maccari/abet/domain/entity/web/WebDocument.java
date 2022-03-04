package com.maccari.abet.domain.entity.web;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotEmpty;

import com.maccari.abet.domain.entity.File;
import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.StudentOutcome;

public class WebDocument {
	private int id;

	@NotEmpty(message = "*required")
	private String title;
	
	private String author;
	
	private List<Integer> programs;

	private List<Integer> outcomes;
	
	private List<Program> fullPrograms;

	private List<StudentOutcome> fullOutcomes;
	
	@NotEmpty(message = "*required")
	private String description;
	
	private Timestamp submitDate;
	
	private File uploadedFile;
	
	private int taskId;
	
	private boolean task;
	
	public WebDocument() {
		programs = new ArrayList<Integer>();
		outcomes = new ArrayList<Integer>();
		fullPrograms = new ArrayList<Program>();
		fullOutcomes = new ArrayList<StudentOutcome>();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public List<Integer> getPrograms() {
		return programs;
	}

	public void setPrograms(List<Integer> programs) {
		this.programs = programs;
	}

	public List<Integer> getOutcomes() {
		return outcomes;
	}

	public void setOutcomes(List<Integer> outcomes) {
		this.outcomes = outcomes;
	}

	public List<Program> getFullPrograms() {
		return fullPrograms;
	}

	public void setFullPrograms(List<Program> fullPrograms) {
		this.fullPrograms = fullPrograms;
	}

	public List<StudentOutcome> getFullOutcomes() {
		return fullOutcomes;
	}

	public void setFullOutcomes(List<StudentOutcome> fullOutcomes) {
		this.fullOutcomes = fullOutcomes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Timestamp getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Timestamp submitDate) {
		this.submitDate = submitDate;
	}

	public File getUploadedFile() {
		return uploadedFile;
	}

	public void setUploadedFile(File uploadedFile) {
		this.uploadedFile = uploadedFile;
	}

	public boolean isTask() {
		return task;
	}

	public void setTask(boolean task) {
		this.task = task;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}
}
