package com.maccari.abet.repository;

import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.maccari.abet.domain.entity.File;
import com.maccari.abet.repository.mapper.FileMapper;

/*
 * FileDaoImpl.java 
 * Author: Yaodong Bi, Thomas Maccari
 * 
 * Implements: FileDao
 * 
 * Description: An implementation using postgreSQL to store, update, and delete
 * file related data. 
 * 
 */

@Repository
public class FileDaoImpl implements FileDao {
	@PersistenceContext
	private EntityManager em;
	
	public int save(String tableName, File file) {
		/*
		 * DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		 * def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		 * TransactionStatus status = transactionManager.getTransaction(def); int fileId
		 * = -1; try { String SQL = "INSERT INTO " + tableName +
		 * "(file_name, file_type, file_size, author, data) " +
		 * " VALUES (?, ?, ?, ?, ?)"; jdbcTemplate.update(SQL, file.getFileName(),
		 * file.getFileType(), file.getFileSize(), file.getAuthor(), file.getData());
		 * 
		 * fileId = jdbcTemplate.queryForObject("SELECT max(id) from " + tableName,
		 * Integer.class);
		 * 
		 * transactionManager.commit(status); } catch (DataAccessException e) {
		 * transactionManager.rollback(status); throw e; } return fileId;
		 */
		return 0;
	}

	@Override
	public <S extends File> S save(S entity) {
		em.persist(entity);
		
		return entity;
	}

	@Override
	public <S extends File> Iterable<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<File> findById(Long id) {
		TypedQuery<File> query = em.createQuery("SELECT f FROM File f"
				+ " WHERE f.id=:id", File.class);
			query.setParameter("id", id.intValue());
		
		return Optional.ofNullable(query.getSingleResult());
	}

	@Override
	public boolean existsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<File> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<File> findAllById(Iterable<Long> ids) {
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
	public void delete(File entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Iterable<? extends File> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}
}
