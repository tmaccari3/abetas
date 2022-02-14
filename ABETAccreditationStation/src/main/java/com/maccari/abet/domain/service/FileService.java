package com.maccari.abet.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.File;
import com.maccari.abet.repository.FileDao;

@Component
public class FileService implements Service<File> {
	@Autowired
	private FileDao fileDao;
	
	@Override
	public List<File> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public File getById(int id) {
		return fileDao.getFileById(id);
	}

	@Override
	public void create(File item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void remove(File item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public File update(File item) {
		// TODO Auto-generated method stub
		return null;
	}

}
