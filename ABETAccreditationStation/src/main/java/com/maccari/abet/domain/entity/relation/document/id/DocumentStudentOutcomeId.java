package com.maccari.abet.domain.entity.relation.document.id;

public class DocumentStudentOutcomeId {
	private int docId;
	
	private int outcomeId;

	public DocumentStudentOutcomeId() {
		
	}
	
	public DocumentStudentOutcomeId(int docId, int outcomeId) {
		this.docId = docId;
		this.outcomeId = outcomeId;
	}

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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + docId;
		result = prime * result + outcomeId;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		DocumentStudentOutcomeId other = (DocumentStudentOutcomeId) obj;
		if (docId != other.docId)
			return false;
		if (outcomeId != other.outcomeId)
			return false;
		return true;
	}
}
