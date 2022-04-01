package com.maccari.abet.domain.entity.relation;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.maccari.abet.domain.entity.Program;

@Entity
@Table(name = "document_program")
@IdClass(DocumentProgram.class)
public class DocumentProgram implements Program, Serializable {
	private static final long serialVersionUID = 4749221601789689592L;

	@Id
    @Column(name = "doc_id")
	private int docId;
	
	@Id
	@Column(name = "program_id")
	private int programId;
	
	private String name;

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
}
