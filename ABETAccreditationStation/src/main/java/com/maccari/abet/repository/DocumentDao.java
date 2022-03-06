package com.maccari.abet.repository;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.maccari.abet.domain.entity.Document;

@Repository
public interface DocumentDao {
	List<Document> getAll();
	
	Document getById(final int id);
	
	List<Document> getRecentDocuments(final int amount);
	
	void createDocument(final Document document);
	
	List<Document> getDocsForTask(final int taskId);
}
