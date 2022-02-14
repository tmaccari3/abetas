package com.maccari.abet.repository;

import org.springframework.stereotype.Repository;

import com.maccari.abet.domain.entity.File;

@Repository
public interface FileDao {
	public int save(File file);
	public int delete(File file);
	public int update(File file);
	
	public File getFileById(int id);
	
	public int save(String tableName, File file);
	public int delete(String tableName, File file);
	public int update(String tableName, File file);
	
	public File getFileById(String tableName, int id);
}
