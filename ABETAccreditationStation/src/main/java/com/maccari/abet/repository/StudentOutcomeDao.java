package com.maccari.abet.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.maccari.abet.domain.entity.StudentOutcomeData;

public interface StudentOutcomeDao extends CrudRepository<StudentOutcomeData, Long>{
	StudentOutcomeData updateOutcome(final StudentOutcomeData outcome);
}
