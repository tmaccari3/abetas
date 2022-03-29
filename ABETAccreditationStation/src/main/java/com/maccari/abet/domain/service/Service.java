package com.maccari.abet.domain.service;

import java.util.List;

import org.springframework.stereotype.Component;

/*
 * Service.java 
 * Author: Thomas Maccari
 * 
 * Description: This service defines CRUD operations to be implemented.
 * 
 */

@Component
public interface Service<T> {
	List<T> getAll();
	T getById(int id);
	void create(T item);
	void remove(T item);
	T update(T item);
}
