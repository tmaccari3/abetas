package com.maccari.abet.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.Document;
import com.maccari.abet.domain.entity.File;
import com.maccari.abet.repository.FileDao;

/*
 * FileService.java 
 * Author: Thomas Maccari
 * 
 * Description: A stand alone service that provides access to the data-source 
 * for the purpose of accessing files.
 * 
 */

@Component
public class FileService {
	@Autowired
	private FileDao fileDao;

	public File getFileById(int id) {
		//return fileDao.getFileById(id);
		return fileDao.findById((long) id).get();
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
			if(document.getFile() != null) {
				document.setFile(fileDao.findById(
						(long) (document.getFile().getId())).get());	
			}
		}
	}
}
