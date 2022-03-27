package com.maccari.abet.domain.entity.searchable;

import java.sql.Timestamp;
import java.util.List;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name="document")
public class SearchableDocument {
	@Id
	private int id;
	
	private String title;
	
	private String author;
	
	//private List<Program> programs;
	
	@ElementCollection // 1
    @CollectionTable(name = "document_tag", joinColumns = @JoinColumn(name = "doc_id")) // 2
    @Column(name = "tag") // 3
	private List<String> tags;
	
	private String description;
	
	@Column(name="submit_date")
	private Timestamp submitDate;
	
	@Column(name="task_id")
	private int taskId;
	
	private boolean task;
	
	public SearchableDocument() {
		
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

	public Timestamp getSubmitDate() {
		return submitDate;
	}

	public void setSubmitDate(Timestamp submitDate) {
		this.submitDate = submitDate;
	}

	public int getTaskId() {
		return taskId;
	}

	public void setTaskId(int taskId) {
		this.taskId = taskId;
	}

	public boolean isTask() {
		return task;
	}

	public void setTask(boolean task) {
		this.task = task;
	}
}
