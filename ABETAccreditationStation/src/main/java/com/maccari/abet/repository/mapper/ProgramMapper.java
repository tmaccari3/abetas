package com.maccari.abet.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.maccari.abet.domain.entity.Program;

/*
 * ProgramMapper.java 
 * Author: Thomas Maccari
 * 
 * Description: This class maps a program from the database
 * 
 */

public 	class ProgramMapper implements RowMapper<Program> {
	public Program mapRow(ResultSet rs, int rowNum) throws SQLException {
		Program program = new Program();
		program.setId(rs.getInt("program_id"));
		program.setName(rs.getString("name"));
		
		return program;
	}
}