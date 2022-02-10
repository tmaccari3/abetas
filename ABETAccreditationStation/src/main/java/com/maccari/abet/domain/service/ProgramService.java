package com.maccari.abet.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.StudentOutcome;
import com.maccari.abet.domain.entity.WebProgram;
import com.maccari.abet.domain.entity.WebTask;
import com.maccari.abet.repository.ProgramDao;

@Component
public class ProgramService implements Service<Program> {
	@Autowired
	private ProgramDao programDao;
	
	@Override
	public List<Program> getAll() {
		return programDao.getAllPrograms();
	}

	@Override
	public Program getById(int id) {
		return programDao.getProgramById(id);
	}
	
	public StudentOutcome getOutcomeById(int id) {
		return programDao.getOutcomeById(id);
	}

	@Override
	public void create(Program item) {
		programDao.createProgram(item);
	}
	
	public void createOutcome(StudentOutcome item) {
		programDao.createOutcome(item);
	}

	@Override
	public void remove(Program item) {
		programDao.removeProgram(item);
	}
	
	public void removeOutcome(StudentOutcome item) {
		programDao.removeOutcome(item);
	}

	@Override
	public Program update(Program item) {
		return programDao.updateProgram(item);
	}
	
	public StudentOutcome updateOutcome(StudentOutcome item) {
		return programDao.updateOutcome(item);
	}
	
	public List<Program> getAllPrograms(){
		return programDao.getAllPrograms();
	}
	
	public List<WebProgram> getAllWebPrograms() {
		List<WebProgram> webPrograms = new ArrayList<WebProgram>();
		List<Program> programs = programDao.getAllPrograms();
		
		for(Program program : programs) {
			webPrograms.add(new WebProgram(program));
		}
		
		Collections.sort(webPrograms, new WebProgramOrderById());
		
		return webPrograms;
	}
	
	public Program convertWebProgram(WebProgram webProgram) {
		Program program = new Program();
		program.setId(webProgram.getId());
		program.setName(webProgram.getName());
		program.setOutcomes(webProgram.getOutcomes());
		program.setActive(webProgram.isActive());
		
		return program;
	}
	
	public void fillPrograms(WebTask task) {
		ArrayList<Program> programs = new ArrayList<Program>();
		ArrayList<StudentOutcome> outcomes = new ArrayList<StudentOutcome>();
		
		for(Integer programId : task.getPrograms()) {
			programs.add(programDao.getProgramById(programId));
		}
		for(Integer outcomeId : task.getOutcomes()) {
			outcomes.add(programDao.getOutcomeById(outcomeId));
		}
		
		task.setFullPrograms(programs);
		task.setFullOutcomes(outcomes);
	}
	
	public boolean checkOutcomes(List<Program> programs) {
		for(Program program : programs) {
			for(StudentOutcome outcome : program.getOutcomes()) {
				if(outcome.getProgramId() != program.getId()) {
					return true;
				}
			}
		}
		
		return false;
	}
}
