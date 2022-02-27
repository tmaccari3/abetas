package com.maccari.abet.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.Document;
import com.maccari.abet.domain.entity.File;
import com.maccari.abet.repository.FileDao;

@Component
public class FileService {
	@Autowired
	private FileDao fileDao;

	public File getFileById(int id) {
		return fileDao.getFileById(id);
	}
	
	public File getFileById(String tableName, int id) {
		return fileDao.getFileById(tableName, id);
	}

	public void save(File item) {
		fileDao.save(item);
	}

	public void delete(File item) {
		// TODO Auto-generated method stub
		
	}

	public File update(File item) {
		// TODO Auto-generated method stub
		return null;
	}

	public void fillFiles(List<Document> documents) {
		for(Document document : documents) {
			document.setFile(fileDao.getFileById(document.getFile().getId()));
		}
	}
}
