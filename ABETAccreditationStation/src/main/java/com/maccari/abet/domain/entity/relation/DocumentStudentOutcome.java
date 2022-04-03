package com.maccari.abet.domain.entity.relation;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.maccari.abet.domain.entity.StudentOutcome;

@Entity
@Table(name = "document_outcome")
@IdClass(DocumentStudentOutcome.class)
public class DocumentStudentOutcome implements StudentOutcome, Serializable {
	@Transient
	private static final long serialVersionUID = 743508590339605375L;

	@Id
	@Column(name = "doc_id")
	private int docId;
	
	@Id
	@Column(name = "outcome_id")
	private int outcomeId;
	
	@Column(name = "prog_id")
	private int progId;
	
	private String name;

	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public int getOutcomeId() {
		return outcomeId;
	}

	public void setOutcomeId(int outcomeId) {
		this.outcomeId = outcomeId;
	}

	public int getProgId() {
		return progId;
	}

	public void setProgId(int progId) {
		this.progId = progId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	@Override
	public int getId() {
		return this.getOutcomeId();
	}
}
