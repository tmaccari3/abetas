package com.maccari.abet.repository.mapper;

/*
 * IdMapper.java 
 * Author: Thomas Maccari
 * 
 * Description: A general purpose mapper class for obtaining the id given a ResultSet.
 * Contains a constructor for setting which column the id is found at.
 * 
 */

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public 	class IdMapper implements RowMapper<Integer> {
	private int index;
	
	public IdMapper() {
		index = 1;
	}
	
	public IdMapper(int index) {
		this.index = index;
	}
	public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
		int id = rs.getInt(index);

		return id;
	}
}
