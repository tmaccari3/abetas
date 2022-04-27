package com.maccari.abet.repository;

import java.util.List;

import org.springframework.context.annotation.Primary;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.maccari.abet.domain.entity.Document;
import com.maccari.abet.domain.entity.web.DocumentSearch;

/*
 * DocumentDao.java 
 * Author: Thomas Maccari
 * 
 * Description: This service defines data access related operations associated 
 * with a Document
 * 
 */

@Primary
@Repository
public interface DocumentDao extends CrudRepository<Document, Long>{
	List<Document> getAll();
	
	List<Document> getBySearch(final DocumentSearch search);
	
	List<Document> getByFullTextSearch(final DocumentSearch search);
	
	List<Document> getRecentDocuments(final int amount);
	
	void createDocument(final Document document);
	
	List<Document> getDocsForTask(final int taskId);
	
	List<String> getAllTags();
}
