package com.maccari.abet.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.repository.ProgramDao;

@Component
public class ProgramService implements Service<Program> {
	@Autowired
	private ProgramDao programDao;
	
	@Override
	public List<Program> getAll() {
		return programDao.getAllPrograms();
	}

	@Override
	public Program getById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void create(Program item) {
		programDao.createProgram(item);
	}

	@Override
	public void remove(Program item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Program update(Program item) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public Program emptyProgram() {
		return new Program();
	}
}
