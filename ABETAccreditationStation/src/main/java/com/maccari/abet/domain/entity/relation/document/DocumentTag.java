package com.maccari.abet.domain.entity.relation.document;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = "document_tag")
public class DocumentTag implements Serializable {
	@Transient
	private static final long serialVersionUID = -8990246555571309005L;

	@Id
	@Column(name = "tag")
	private String name;
	
	@Id
	@Column(name = "doc_id")
	private int docId;
	
	public DocumentTag() {
		
	}

	public DocumentTag(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getDocId() {
		return docId;
	}

	public void setDocId(int docId) {
		this.docId = docId;
	}
}
