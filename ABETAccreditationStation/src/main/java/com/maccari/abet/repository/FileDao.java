package com.maccari.abet.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.maccari.abet.domain.entity.File;

/*
 * FileDao.java 
 * Author: Thomas Maccari
 * 
 * Description: This service defines data access related operations associated 
 * with a File
 * 
 */

@Repository
public interface FileDao extends CrudRepository<File, Long>{
	
}
