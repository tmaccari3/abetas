package com.maccari.abet.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.maccari.abet.domain.entity.ProgramData;

/*
 * ProgramMapper.java 
 * Author: Thomas Maccari
 * 
 * Description: This class maps a program from the database
 * 
 */

public 	class ProgramMapper implements RowMapper<ProgramData> {
	public ProgramData mapRow(ResultSet rs, int rowNum) throws SQLException {
		ProgramData program = new ProgramData();
		program.setId(rs.getInt("program_id"));
		program.setName(rs.getString("name"));
		
		return program;
	}
}