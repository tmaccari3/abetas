package com.maccari.abet.repository;

import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import com.maccari.abet.domain.entity.StudentOutcomeData;

public class StudentOutcomeDaoImpl implements StudentOutcomeDao {
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public <S extends StudentOutcomeData> S save(S entity) {
		em.persist(entity);
		
		return entity;
	}

	@Override
	public <S extends StudentOutcomeData> Iterable<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void delete(StudentOutcomeData entity) {
		em.remove(entity);
	}

	@Override
	public Optional<StudentOutcomeData> findById(Long id) {
		TypedQuery<StudentOutcomeData> query = em.createQuery("SELECT s FROM StudentOutcomeData s "
				+ "WHERE id=:id", StudentOutcomeData.class);
		query.setParameter("id", id.intValue());
		
		return Optional.ofNullable(query.getSingleResult());
	}
	
	@Override
	public StudentOutcomeData updateOutcome(StudentOutcomeData outcome) {
		/*
		 * DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		 * def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		 * TransactionStatus status = transactionManager.getTransaction(def);
		 * 
		 * try { String SQL = "UPDATE student_outcome SET active=? WHERE id=?";
		 * jdbcTemplate.update(SQL, outcome.isActive(), outcome.getId());
		 * 
		 * transactionManager.commit(status); return outcome; } catch(Exception e) {
		 * System.out.println("Error in updating student outcome record, rolling back");
		 * transactionManager.rollback(status);
		 * 
		 * return null; }
		 */
		return null;
	}

	@Override
	public boolean existsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<StudentOutcomeData> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<StudentOutcomeData> findAllById(Iterable<Long> ids) {
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
	public void deleteAll(Iterable<? extends StudentOutcomeData> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}
}
