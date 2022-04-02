package com.maccari.abet.domain.entity;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotEmpty;

import com.maccari.abet.domain.entity.relation.DocumentProgram;
import com.maccari.abet.domain.entity.relation.DocumentStudentOutcome;

/*
 * Document.java 
 * Author: Thomas Maccari
 * 
 * Description: The domain object that represents a tuple from the document table.
 * 
 */
@Entity
@Table(name = "document")
public class Document {
	@Id
    @SequenceGenerator(name = "document_id_seq",
                       sequenceName = "document_id_seq",
                       allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "document_id_seq")
	private int id;
	
	@NotEmpty(message = "*required")
	private String title;
	
	private String author;
	
	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "doc_id")
	private List<DocumentProgram> programs;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "doc_id")
	private List<DocumentStudentOutcome> outcomes;
	
	@Transient
	private List<String> tags;
	
	@NotEmpty(message = "*required")
	private String description;
	
	@Column(name = "submit_date")
	private Timestamp submitDate;
	
	@Transient
	private File file;
	
	@Column(name = "task_id")
	private int taskId;
	
	private boolean task;
	
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

	public List<DocumentProgram> getPrograms() {
		return programs;
	}

	public void setPrograms(List<DocumentProgram> programs) {
		this.programs = programs;
	}

	public List<DocumentStudentOutcome> getOutcomes() {
		return outcomes;
	}

	public void setOutcomes(List<DocumentStudentOutcome> outcomes) {
		this.outcomes = outcomes;
	}

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
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
	
	// Returns the formatted date in year-month-day form as a string
	public String getFormattedDate() {
		if(submitDate == null) {
			return "";
		}
		
		else {
			return new SimpleDateFormat("yyyy-MM-dd").format(submitDate);
		}
	}
}
