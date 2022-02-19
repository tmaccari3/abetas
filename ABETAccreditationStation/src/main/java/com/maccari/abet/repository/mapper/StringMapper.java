package com.maccari.abet.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

public class StringMapper implements RowMapper<String> {
	public String mapRow(ResultSet rs, int rowNum) throws SQLException {
		String result = rs.getString(1);

		return result;
	}
}
