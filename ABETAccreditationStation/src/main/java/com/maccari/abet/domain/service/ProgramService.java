package com.maccari.abet.domain.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.WebProgram;
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
		return programDao.getProgramById(id);
	}

	@Override
	public void create(Program item) {
		programDao.createProgram(item);
	}

	@Override
	public void remove(Program item) {
		programDao.removeProgram(item);
	}

	@Override
	public Program update(Program item) {
		return programDao.updateProgram(item);
	}
	
	public List<WebProgram> getAllWebPrograms() {
		List<WebProgram> webPrograms = new ArrayList<WebProgram>();
		List<Program> programs = programDao.getAllPrograms();
		
		for(Program program : programs) {
			webPrograms.add(new WebProgram(program));
		}
		
		return webPrograms;
	}
}
