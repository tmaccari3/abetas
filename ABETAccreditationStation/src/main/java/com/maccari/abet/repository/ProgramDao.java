package com.maccari.abet.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.StudentOutcome;

@Repository
public interface ProgramDao {
	void createProgram(final Program program);
	
	void createOutcome(final StudentOutcome outcome);
	
	void removeProgram(final Program program);
	
	void removeOutcome(final StudentOutcome outcome);
	
	Program updateProgram(final Program program);
	
	StudentOutcome updateOutcome(final StudentOutcome outcome);
	
	Program getProgramById(int id);
	
	List<Program> getAllPrograms();
	
	List<Program> getActivePrograms();
	
	List<StudentOutcome> getAllOutcomesForProgram(int id);
	
	List<StudentOutcome> getActiveOutcomesForProgram(int id);
	
	StudentOutcome getOutcomeById(int id);
}