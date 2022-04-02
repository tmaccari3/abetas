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

import com.maccari.abet.domain.entity.StudentOutcomeData;

public 	class StudentOutcomeMapper implements RowMapper<StudentOutcomeData> {
	public StudentOutcomeData mapRow(ResultSet rs, int rowNum) throws SQLException {
		StudentOutcomeData outcome = new StudentOutcomeData();
		//outcome.setProgramId(rs.getInt("outcome_id"));
		outcome.setName(rs.getString("name"));
		
		return outcome;
	}
}
