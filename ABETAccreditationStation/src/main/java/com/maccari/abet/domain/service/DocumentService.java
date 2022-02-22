package com.maccari.abet.domain.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maccari.abet.domain.entity.Document;
import com.maccari.abet.domain.entity.web.WebDocument;
import com.maccari.abet.repository.DocumentDao;

@Component
public class DocumentService implements Service<Document> {
	@Autowired
	private DocumentDao docDao;
	
	@Override
	public List<Document> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Document getById(int id) {
		// TODO Auto-generated method stub
		return null;
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

	public Document webDoctoDoc(WebDocument webDoc) {
		Document document = new Document();
		
		document.setId(webDoc.getId());
		document.setTitle(webDoc.getTitle());
		document.setAuthor(webDoc.getAuthor());
		document.setPrograms(webDoc.getFullPrograms());
		document.setOutcomes(webDoc.getFullOutcomes());
		document.setSubmitDate(webDoc.getSubmitDate());
		document.setDescription(webDoc.getDescription());
		document.setFile(webDoc.getUploadedFile());
		
		return document;
	}
}
