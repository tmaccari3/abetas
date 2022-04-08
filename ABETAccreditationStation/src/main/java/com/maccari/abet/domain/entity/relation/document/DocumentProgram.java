package com.maccari.abet.domain.entity.relation.document;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.maccari.abet.domain.entity.Program;

@Entity
@Table(name = "document_program")
public class DocumentProgram implements Program, Serializable {
	@Transient
	private static final long serialVersionUID = 4749221601789689592L;

	@Id
	@Column(name = "doc_id")
	private int docId;
	
	@Id
	@Column(name = "program_id")
	private int programId;
	
	private String name;

	public DocumentProgram() {
		
	}
	
	public DocumentProgram(int programId) {
		this.programId = programId;
	}
	
	public DocumentProgram(int programId, String name) {
		this.programId = programId;
		this.name = name;
	}

	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}

	public int getProgramId() {
		return programId;
	}

	public void setProgramId(int programId) {
		this.programId = programId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int getId() {
		return this.programId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(docId, programId);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentProgram other = (DocumentProgram) obj;
		return docId == other.docId && programId == other.programId;
	}
}
