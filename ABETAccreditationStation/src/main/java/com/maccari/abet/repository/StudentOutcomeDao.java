package com.maccari.abet.repository;

import org.springframework.data.repository.CrudRepository;

import com.maccari.abet.domain.entity.StudentOutcome;

public interface StudentOutcomeDao extends CrudRepository<StudentOutcome, Long>{
	
}
