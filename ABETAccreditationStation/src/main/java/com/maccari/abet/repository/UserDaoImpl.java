package com.maccari.abet.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.maccari.abet.domain.entity.User;

@Repository
public class UserDaoImpl implements UserDao {
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
	public void createUser(User user) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			String SQL = "INSERT INTO account (email, password) " + " VALUES (?, ?)";
			jdbcTemplate.update(SQL, user.getEmail(), user.getPassword());
			
			
			SQL = "INSERT INTO authority (email, role) " + " VALUES (?, ?)";
			for(String role : user.getRoles()) {
				jdbcTemplate.update(SQL, user.getEmail(), role);
			}
			
			transactionManager.commit(status);
		} catch(DataAccessException e) {
			System.out.println("Error in creating user record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}

	@Override
	public List<User> getAllUsers() {
		return null;
	}
	
	@Override
	public User getUserByEmail(String email) {
		try {
			String SQL = "SELECT email FROM account WHERE email=?";
			User user = new User();
			user = jdbcTemplate.queryForObject(SQL,
					new Object[] { email }, new UserMapper());
			
			return user;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<String> getRoles() {
		// TODO Auto-generated method stub
		return null;
	}
	
	class UserMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException{
			User user = new User();
			user.setEmail(rs.getString("email"));
			
			return user;
		}
	}
}
