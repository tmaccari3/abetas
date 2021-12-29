package com.maccari.abet.domain.service;

import java.util.List;

public interface Service<T> {
	List<T> getAll();
	T getById(int id);
	void create(T item);
}
