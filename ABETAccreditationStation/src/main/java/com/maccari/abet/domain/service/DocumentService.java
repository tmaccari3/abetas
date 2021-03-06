package com.maccari.abet.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.Document;
import com.maccari.abet.domain.entity.File;
import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.StudentOutcome;
import com.maccari.abet.domain.entity.web.DocumentSearch;
import com.maccari.abet.domain.entity.web.WebDocument;
import com.maccari.abet.repository.DocumentDao;

/*
 * DocumentService.java 
 * Author: Thomas Maccari
 * 
 * Implements: Service.java
 * 
 * Description: This service class provides access to the data-source, methods 
 * for Document to WebDocument conversion, and other document related utilities.
 * 
 */

@Component
public class DocumentService implements Service<Document> {
	@Autowired
	private DocumentDao docDao;
	
	@Autowired
	private FileService fileService;
	
	@Override
	public List<Document> getAll() {
		return docDao.getAll();
	}
	
	public List<Document> getBySearch(DocumentSearch search) {
		return docDao.getBySearch(search);
	}
	
	public List<Document> getRecentDocuments(int amount) {
		return docDao.getRecentDocuments(amount);
	}

	@Override
	public Document getById(int id) {
		return docDao.getById(id);
	}
	
	// Retrieves the document as well as its designated file.
	public Document getFullDocById(int id) {
		Document document = getById(id);
		File file = document.getFile();
		if (file != null) {
			document.setFile(fileService.getFileById(file.getId()));
		}

		return document;
	}
	
	public List<String> getAllTags() {
		return docDao.getAllTags();
	}

	@Override
	public void create(Document item) {
		docDao.createDocument(item);
	}

	@Override
	public void remove(Document item) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Document update(Document item) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public List<Document> getDocsForTask(int taskId){
		return docDao.getDocsForTask(taskId);
	}

	// Converts the given WebDocument to a Document
	public Document webDoctoDoc(WebDocument webDoc) {
		Document document = new Document();
		
		document.setId(webDoc.getId());
		document.setTitle(webDoc.getTitle());
		document.setAuthor(webDoc.getAuthor());
		document.setPrograms(webDoc.getFullPrograms());
		document.setOutcomes(webDoc.getFullOutcomes());
		document.setTags(webDoc.getTags());
		document.setSubmitDate(webDoc.getSubmitDate());
		document.setDescription(webDoc.getDescription());
		document.setFile(webDoc.getUploadedFile());
		document.setTask(webDoc.isTask());
		document.setTaskId(webDoc.getTaskId());
		
		return document;
	}
	
	// Fills the id lists that correspond to the programs and outcomes of the WebDocument
	public void fillDocIds(WebDocument webDoc) {
		for(Program program : webDoc.getFullPrograms()) {
			webDoc.getPrograms().add(program.getId());
		}
		for(StudentOutcome outcome : webDoc.getFullOutcomes()) {
			webDoc.getOutcomes().add(outcome.getId());
		}
	}
}
