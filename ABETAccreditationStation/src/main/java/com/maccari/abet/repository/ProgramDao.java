package com.maccari.abet.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maccari.abet.domain.entity.Program;

@Repository
public interface ProgramDao {
	void createProgram(final Program program);
	
	void removeProgram(final Program program);
	
	Program updateProgram(final Program program);
	
	List<Program> getAllPrograms();
	
	Program getProgramById(int id);
}