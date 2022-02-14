package com.maccari.abet.repository;

import org.springframework.stereotype.Repository;

import com.maccari.abet.domain.entity.Document;

@Repository
public interface DocumentDao {
	void createDocument(final Document document);
}
