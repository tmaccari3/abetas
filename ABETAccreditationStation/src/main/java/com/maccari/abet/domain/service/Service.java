package com.maccari.abet.domain.service;

import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface Service<T> {
	List<T> getAll();
	T getById(int id);
	void create(T item);
	void remove(T item);
}
