package com.maccari.abet.domain.entity.relation;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "document_tag")
public class DocumentTag {
	//@Id
	private String name;
	
	@Id
	@Column(name = "doc_id")
	private int docId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
