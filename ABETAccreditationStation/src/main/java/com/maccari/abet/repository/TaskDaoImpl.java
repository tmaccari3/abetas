package com.maccari.abet.repository;

import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;

import com.maccari.abet.domain.entity.Task;

@Repository
public class TaskDaoImpl implements TaskDao {
	@Autowired
	private DataSourceTransactionManager transactionManager;
	
	private DataSource dataSource;
	
	private JdbcTemplate jdbcTemplate;
	
	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.dataSource = dataSource;
		this.jdbcTemplate = new JdbcTemplate(this.dataSource);
	}

	@Override
	public void createUser(Task task) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void removeUser(Task task) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Task updateUser(Task task) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Task> getAllUsers() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Task getUserById(int id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<String> getPrograms() {
		try {
			String sql = "select * from program";
			
			return jdbcTemplate.query(sql, (rs, rowNum) -> (rs.getString("name")));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

}
