package com.maccari.abet.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.maccari.abet.domain.entity.ProgramData;
import com.maccari.abet.domain.entity.StudentOutcome;
import com.maccari.abet.domain.entity.relation.UserProgram;

/*
 * ProgramDao.java 
 * Author: Thomas Maccari
 * 
 * Description: This service defines data access related operations associated 
 * with a Program
 * 
 */

@Repository
public interface ProgramDao extends CrudRepository<ProgramData, Long>{
	ProgramData updateProgram(final ProgramData program);
	
	List<ProgramData> getAllPrograms();
	
	List<ProgramData> getActivePrograms();
	
	List<ProgramData> getActivePrograms(String userEmail);
	
	List<StudentOutcome> getActiveOutcomesForProgram(int id);
}