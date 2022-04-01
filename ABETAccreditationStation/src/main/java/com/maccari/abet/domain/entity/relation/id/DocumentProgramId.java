package com.maccari.abet.domain.entity.relation.id;

import java.util.Objects;

public class DocumentProgramId {
	private int docId;
	
	private int programId;

	public DocumentProgramId() {
		
	}
	
	public DocumentProgramId(int docId, int programId) {
		this.docId = docId;
		this.programId = programId;
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
		DocumentProgramId other = (DocumentProgramId) obj;
		return docId == other.docId && programId == other.programId;
	}
}
