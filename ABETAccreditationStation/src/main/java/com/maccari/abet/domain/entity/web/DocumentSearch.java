package com.maccari.abet.domain.entity.web;

import java.sql.Timestamp;
import java.util.List;

public class DocumentSearch {
	private Timestamp date;
	
	private List<Integer> programs;
	
	private List<Integer> outcomes;
	
	private int count;

	public Timestamp getDate() {
		return date;
	}

	public void setDate(Timestamp date) {
		this.date = date;
	}

	public List<Integer> getPrograms() {
		return programs;
	}

	public void setPrograms(List<Integer> programs) {
		this.programs = programs;
	}

	public List<Integer> getOutcomes() {
		return outcomes;
	}

	public void setOutcomes(List<Integer> outcomes) {
		this.outcomes = outcomes;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}
}
