package com.maccari.abet.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

/*
 * StringMapper.java 
 * Author: Thomas Maccari
 * 
 * Description: A general purpose mapper class for obtaining the a string in given 
 * a ResultSet. Contains a constructor for setting which column the string is found at.
 * 
 */

public class StringMapper implements RowMapper<String> {
	private int index;
	
	public StringMapper() {
		index = 1;
	}
	
	public StringMapper(int index) {
		this.index = index;
	}
	
	public String mapRow(ResultSet rs, int rowNum) throws SQLException {
		String result = rs.getString(index);

		return result;
	}
}
