package com.maccari.abet.repository.mapper;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.maccari.abet.domain.entity.File;

/*
 * FileMapper.java 
 * Author: Thomas Maccari
 * 
 * Description: This class maps a file from the database
 * 
 */

public class FileMapper implements RowMapper<File> {
	public File mapRow(ResultSet rs, int rowNum) throws SQLException {
		File file = new File();
		file.setId(rs.getInt("id"));
		file.setFileName(rs.getString("file_name"));
		file.setFileType(rs.getString("file_type"));
		file.setFileSize(rs.getLong("file_size"));
		file.setAuthor(rs.getString("author"));
		file.setData(rs.getBytes("data"));

		return file;
	}
}
