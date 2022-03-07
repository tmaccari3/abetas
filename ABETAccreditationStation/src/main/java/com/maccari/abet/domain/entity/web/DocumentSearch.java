package com.maccari.abet.domain.entity.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

public class DocumentSearch {
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date toDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fromDate;
	
	private List<Integer> programs;
	
	private List<Integer> outcomes;
	
	private int count = 10;
	
	public DocumentSearch() {
		this.programs = new ArrayList<Integer>();
		this.outcomes = new ArrayList<Integer>();
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
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
	
	public String getFormattedDate(Date date) {
		if(date == null) {
			return "";
		}
		
		else {
			return new SimpleDateFormat("yyyy-MM-dd").format(date);
		}
	}
}
