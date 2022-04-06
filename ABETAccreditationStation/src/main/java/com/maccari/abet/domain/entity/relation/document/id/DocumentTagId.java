package com.maccari.abet.domain.entity.relation.document.id;

import java.util.Objects;

public class DocumentTagId {
	private String name;
		
	private int docId;
	
	public DocumentTagId() {
		
	}

	public DocumentTagId(String name, int docId) {
		super();
		this.name = name;
		this.docId = docId;
	}

	@Override
	public int hashCode() {
		return Objects.hash(docId, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentTagId other = (DocumentTagId) obj;
		return docId == other.docId && Objects.equals(name, other.name);
	}
}
