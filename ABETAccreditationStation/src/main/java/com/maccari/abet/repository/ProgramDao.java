package com.maccari.abet.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.StudentOutcome;

@Repository
public interface ProgramDao {
	void createProgram(final Program program);
	
	void removeProgram(final Program program);
	
	Program updateProgram(final Program program);
	
	Program getProgramById(int id);
	
	List<Program> getAllPrograms();
	
	List<StudentOutcome> getAllOutcomesForProgram(int id);
	
	StudentOutcome getOutcomeById(int id);
}