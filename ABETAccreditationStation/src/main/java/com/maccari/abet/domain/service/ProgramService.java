package com.maccari.abet.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.ProgramData;
import com.maccari.abet.domain.entity.StudentOutcome;
import com.maccari.abet.domain.entity.StudentOutcomeData;
import com.maccari.abet.domain.entity.relation.document.DocumentProgram;
import com.maccari.abet.domain.entity.web.WebDocument;
import com.maccari.abet.domain.entity.web.WebProgram;
import com.maccari.abet.domain.entity.web.WebStudentOutcome;
import com.maccari.abet.domain.entity.web.WebTask;
import com.maccari.abet.domain.entity.web.WebUser;
import com.maccari.abet.repository.ProgramDao;
import com.maccari.abet.repository.StudentOutcomeDao;
import com.maccari.abet.utility.WebProgramOrderById;

/*
 * ProgramService.java 
 * Author: Thomas Maccari
 * 
 * Implements: Service.java
 * 
 * Description: This service class provides access to the data-source, methods 
 * for Program to WebProgram conversion, program validation, 
 * and other program related utilities.
 * 
 */

@Component
public class ProgramService implements Service<ProgramData> {
	@Autowired
	private ProgramDao programDao;
	
	@Autowired
	private StudentOutcomeDao studentOutcomeDao;
	
	@Override
	public List<ProgramData> getAll() {
		return programDao.getAllPrograms();
	}
	
	public List<ProgramData> getActivePrograms(){
		return programDao.getActivePrograms();
	}
	
	public List<ProgramData> getActivePrograms(String userEmail){
		return programDao.getActivePrograms(userEmail);
	}

	@Override
	public ProgramData getById(int id) {
		//return programDao.getProgramById(id);
		return programDao.findById((long) id).get();
	}
	
	public StudentOutcomeData getOutcomeById(int id) {
		return studentOutcomeDao.findById((long) id).get();
	}

	@Override
	public void create(ProgramData item) {
		//programDao.createProgram(item);
		item.setActive(true);
		programDao.save(item);
	}
	
	public void createOutcome(StudentOutcomeData item) {
		//programDao.createOutcome(item);
		item.setActive(true);
		studentOutcomeDao.save(item);
	}

	@Override
	public void remove(ProgramData item) {
		//programDao.removeProgram(item);
		programDao.delete(item);
	}
	
	public void removeOutcome(StudentOutcomeData item) {
		//programDao.removeOutcome(item);
		studentOutcomeDao.delete(item);
	}

	@Override
	public ProgramData update(ProgramData item) {
		return programDao.updateProgram(item);
	}
	
	public StudentOutcomeData updateOutcome(StudentOutcomeData item) {
		return studentOutcomeDao.updateOutcome(item);
	}
	
	// Gets all programs, converts them to WebPrograms, and sorts them by id
	public List<WebProgram> getAllWebPrograms() {
		List<WebProgram> webPrograms = new ArrayList<WebProgram>();
		List<ProgramData> programs = programDao.getAllPrograms();
		
		for(ProgramData program : programs) {
			webPrograms.add(new WebProgram(program));
		}
		
		Collections.sort(webPrograms, new WebProgramOrderById());
		
		return webPrograms;
	}
	
	// Gets all outcomes for a program, converts them to WebOutcomes
	public List<WebStudentOutcome> getAllWebOutcomes(ProgramData program) {
		List<WebStudentOutcome> webStudentOutcomes = new ArrayList<WebStudentOutcome>();
		
		for(StudentOutcomeData outcome : program.getOutcomes()) {
			webStudentOutcomes.add(new WebStudentOutcome(outcome));
		}
		
		//Collections.sort(webPrograms, new WebProgramOrderById());
		
		return webStudentOutcomes;
	}
	
	// Converts the given WebProgram to a Program
	public ProgramData convertWebProgram(WebProgram webProgram) {
		ProgramData program = new ProgramData();
		program.setId(webProgram.getId());
		program.setName(webProgram.getName());
		program.setOutcomes(webProgram.getOutcomes());
		program.setActive(webProgram.isActive());
		
		return program;
	}
	
	// Converts given WebStudentOutcomes to StudentOutcomes
	public StudentOutcomeData convertWebOutcome(WebStudentOutcome webOutcome) {
		StudentOutcomeData outcome = new StudentOutcomeData();
		outcome.setId(webOutcome.getId());
		outcome.setProgId(webOutcome.getProgId());
		outcome.setName(webOutcome.getName());
		outcome.setActive(webOutcome.isActive());
		
		return outcome;
	}
	
	// Fills the program and student outcome lists for a given task
	public void fillPrograms(WebTask task) {
		ArrayList<Program> programs = new ArrayList<Program>();
		ArrayList<StudentOutcome> outcomes = new ArrayList<StudentOutcome>();
		
		for(Integer programId : task.getPrograms()) {
			programs.add(programDao.findById((long) programId).get());
		}
		for(Integer outcomeId : task.getOutcomes()) {
			outcomes.add(studentOutcomeDao.findById((long) outcomeId).get());
		}
		
		task.setFullPrograms(programs);
		task.setFullOutcomes(outcomes);
	}
	
	// Fills the program and student outcome lists for a given document
	public void fillPrograms(WebDocument document) {
		ArrayList<Program> programs = new ArrayList<Program>();
		ArrayList<StudentOutcome> outcomes = new ArrayList<StudentOutcome>();
		
		for(Integer programId : document.getPrograms()) {
			programs.add(programDao.findById((long) programId).get());
		}
		for(Integer outcomeId : document.getOutcomes()) {
			outcomes.add(studentOutcomeDao.findById((long) outcomeId).get());
		} 
		
		document.setFullPrograms(programs);
		document.setFullOutcomes(outcomes);
	}
	
	// Fills the program and student outcome lists for a given user
	public void fillPrograms(WebUser user) {
		ArrayList<ProgramData> programs = new ArrayList<ProgramData>();
		for(Integer id : user.getProgramIds()) {
			programs.add(programDao.findById((long) id).get());
		}
		
		user.setPrograms(programs);
	}
	
	// Checks is the outcomes and programs are valid selections for a user
	public boolean checkPrograms(List<? extends Program> programs, List<? extends StudentOutcome> list,
			String userEmail) {
		for(StudentOutcome outcome : list) {
			if(!isInProgram(programs, outcome)) {
				return true;
			}
		}
		
		return false;
	}
	
	// Determines if a selected outcome belongs to one of the selected programs
	private boolean isInProgram(List<? extends Program> programs, StudentOutcome outcome) {
		if(outcome == null) {
			return false;
		}
		for(Program program : programs) {
			if(program == null) {
				return false;
			}
			if(program.getId() == outcome.getProgId()) {
				return true;
			}
		}
		
		return false;
	}
}
