package com.maccari.abet.repository;

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
	public static String defaultTableName = "file";
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	

	@Autowired
	public void setDataSource(DataSource dataSource) {
	}

	@Override
	public int save(File file) {
		return save(defaultTableName, file);
	}
	
	@Override
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
	public File getFileById(int id) {
		return getFileById(defaultTableName, id);
	}
	
	@Override
	public File getFileById(String tableName, int id) {
		/*
		 * String SQL = "SELECT id, file_name, file_type, file_size, author, data " +
		 * " FROM " + tableName + " WHERE id = ?"; File file =
		 * jdbcTemplate.queryForObject(SQL, new FileMapper(), id);
		 * 
		 * logger.info("Retrieved File Id = " + file.getId());
		 * 
		 * return file;
		 */
		return null;
	}

	@Override
	public int delete(File file) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(File file) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int delete(String tableName, File file) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int update(String tableName, File file) {
		// TODO Auto-generated method stub
		return 0;
	}
}
