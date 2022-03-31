package com.maccari.abet.repository.mapper;

/*
 * StudentOutcomeMapper.java 
 * Author: Thomas Maccari
 * 
 * Description: This class maps a student outcome from the database
 * 
 */

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.maccari.abet.domain.entity.StudentOutcome;

public 	class StudentOutcomeMapper implements RowMapper<StudentOutcome> {
	public StudentOutcome mapRow(ResultSet rs, int rowNum) throws SQLException {
		StudentOutcome outcome = new StudentOutcome();
		//outcome.setProgramId(rs.getInt("outcome_id"));
		outcome.setName(rs.getString("name"));
		
		return outcome;
	}
}
