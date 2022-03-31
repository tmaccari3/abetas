package com.maccari.abet.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.StudentOutcome;
import com.maccari.abet.domain.entity.user.UserProgram;

/*
 * ProgramDao.java 
 * Author: Thomas Maccari
 * 
 * Description: This service defines data access related operations associated 
 * with a Program
 * 
 */

@Repository
public interface ProgramDao extends CrudRepository<Program, Long>{
	Program updateProgram(final Program program);
	
	List<Program> getAllPrograms();
	
	List<Program> getActivePrograms();
	
	List<Program> getActivePrograms(String userEmail);
	
	List<StudentOutcome> getActiveOutcomesForProgram(int id);
}