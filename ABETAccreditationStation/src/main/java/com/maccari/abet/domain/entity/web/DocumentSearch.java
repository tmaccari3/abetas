package com.maccari.abet.domain.entity.web;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.ProgramData;
import com.maccari.abet.domain.entity.relation.document.DocumentProgram;

/*
 * DocumentSearch.java 
 * Author: Thomas Maccari
 * 
 * Description: This class serves as a form backing object for 
 * the user's search criteria (the fields the enter)
 * 
 */

public class DocumentSearch {
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date toDate;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date fromDate;
	
	private List<Integer> programs;
	
	private List<Integer> outcomes;
	
	private List<String> tags;
	
	private int searchCount = 20;
	
	private int recentCount = 20;
	
	public DocumentSearch() {
		this.programs = new ArrayList<Integer>();
		this.outcomes = new ArrayList<Integer>();
		this.tags = new ArrayList<String>();
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

	public List<String> getTags() {
		return tags;
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}

	public void setOutcomes(List<Integer> outcomes) {
		this.outcomes = outcomes;
	}
	
	public int getSearchCount() {
		return searchCount;
	}

	public void setSearchCount(int searchCount) {
		this.searchCount = searchCount;
	}
	
	public int getRecentCount() {
		return recentCount;
	}

	public void setRecentCount(int recentCount) {
		this.recentCount = recentCount;
	}
	
	public String getFormattedDate(Date date) {
		if(date == null) {
			return "";
		}
		
		else {
			return new SimpleDateFormat("yyyy-MM-dd").format(date);
		}
	}
	
	//for testing
	public String toString() {
		String result = "";
		
		result += getFormattedDate(toDate) + "\n";
		result += getFormattedDate(fromDate) + "\n";
		result += programs + "\n";
		result += outcomes + "\n";
		result += searchCount + "\n";
		
		return result;
	}
}
