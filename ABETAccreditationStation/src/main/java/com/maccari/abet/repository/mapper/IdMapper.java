package com.maccari.abet.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public 	class IdMapper implements RowMapper<Integer> {
	public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
		int id = rs.getInt(1);

		return id;
	}
}
