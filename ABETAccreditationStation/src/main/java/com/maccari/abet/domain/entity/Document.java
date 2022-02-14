package com.maccari.abet.domain.entity;

import java.sql.Timestamp;
import java.util.List;

import javax.validation.constraints.NotEmpty;

public class Document {
	private int id;
	
	@NotEmpty(message = "*required")
	private String title;
	
	private String author;
	
	private List<Program> programs;

	private List<StudentOutcome> outcomes;
	
	@NotEmpty(message = "*required")
	private String description;
	
	private Timestamp submitDate;
	
	private File file;
	
	public Document() {
		
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

	public List<Program> getPrograms() {
		return programs;
	}

	public void setPrograms(List<Program> programs) {
		this.programs = programs;
	}

	public List<StudentOutcome> getOutcomes() {
		return outcomes;
	}

	public void setOutcomes(List<StudentOutcome> outcomes) {
		this.outcomes = outcomes;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public Timestamp getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Timestamp submitDate) {
		this.submitDate = submitDate;
	}
}
