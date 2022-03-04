package com.maccari.abet.domain.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.StudentOutcome;
import com.maccari.abet.domain.entity.web.WebDocument;
import com.maccari.abet.domain.entity.web.WebProgram;
import com.maccari.abet.domain.entity.web.WebTask;
import com.maccari.abet.domain.entity.web.WebUser;
import com.maccari.abet.repository.ProgramDao;
import com.maccari.abet.utility.WebProgramOrderById;

@Component
public class ProgramService implements Service<Program> {
	@Autowired
	private ProgramDao programDao;
	
	@Override
	public List<Program> getAll() {
		return programDao.getAllPrograms();
	}
	
	public List<Program> getActivePrograms(){
		return programDao.getActivePrograms();
	}
	
	public List<Program> getActivePrograms(String userEmail){
		return programDao.getActivePrograms(userEmail);
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
	
	public void fillPrograms(WebDocument document) {
		ArrayList<Program> programs = new ArrayList<Program>();
		ArrayList<StudentOutcome> outcomes = new ArrayList<StudentOutcome>();
		
		for(Integer programId : document.getPrograms()) {
			programs.add(programDao.getProgramById(programId));
		}
		for(Integer outcomeId : document.getOutcomes()) {
			outcomes.add(programDao.getOutcomeById(outcomeId));
		}
		
		document.setFullPrograms(programs);
		document.setFullOutcomes(outcomes);
	}
	
	public void fillPrograms(WebUser user) {
		ArrayList<Program> programs = new ArrayList<Program>();
		for(Integer id : user.getProgramIds()) {
			programs.add(programDao.getProgramById(id));
		}
		
		user.setPrograms(programs);
	}
	
	public boolean checkPrograms(List<Program> programs, List<StudentOutcome> outcomes,
			String userEmail) {
		/*List<Program> validPrograms = programDao.getActivePrograms(userEmail);
		System.out.println(validPrograms);
		for(Program program : programs) {
			if(!validPrograms.contains(program)) {
				System.out.println("prog invalid");
				return true;
			}
			
			for(StudentOutcome outcome : outcomes) {
				List<StudentOutcome> validOutcomes = programDao
						.getActiveOutcomesForProgram(program.getId());
				if(!validOutcomes.contains(outcome)) {
					System.out.println("gotta be in here: "+validOutcomes);
					System.out.println("the guy: "+outcome);
					return true;
				}
			}
		}*/
		for(StudentOutcome outcome : outcomes) {
			if(!isInProgram(programs, outcome)) {
				return true;
			}
		}
		
		return false;
	}
	
	private boolean isInProgram(List<Program> programs, StudentOutcome outcome) {
		if(outcome == null) {
			return false;
		}
		for(Program program : programs) {
			if(program == null) {
				return false;
			}
			if(program.getId() == outcome.getProgramId()) {
				return true;
			}
		}
		
		return false;
	}
}
