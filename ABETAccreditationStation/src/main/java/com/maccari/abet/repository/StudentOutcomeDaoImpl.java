package com.maccari.abet.repository;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.maccari.abet.domain.entity.StudentOutcome;

public class StudentOutcomeDaoImpl implements StudentOutcomeDao {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public <S extends StudentOutcome> S save(S entity) {
		em.persist(entity);
		
		return entity;
	}

	@Override
	public <S extends StudentOutcome> Iterable<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void delete(StudentOutcome entity) {
		em.remove(entity);
	}

	@Override
	public Optional<StudentOutcome> findById(Long id) {
		TypedQuery<StudentOutcome> query = em.createQuery("SELECT s FROM StudentOutcome s "
				+ "WHERE id=:id", StudentOutcome.class);
		query.setParameter("id", id.intValue());
		
		return Optional.ofNullable(query.getSingleResult());
	}

	@Override
	public boolean existsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<StudentOutcome> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<StudentOutcome> findAllById(Iterable<Long> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteById(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Iterable<? extends StudentOutcome> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}

}
