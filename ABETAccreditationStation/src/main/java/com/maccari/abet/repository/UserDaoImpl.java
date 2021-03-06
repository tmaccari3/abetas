package com.maccari.abet.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.User;
import com.maccari.abet.utility.WebList;

/*
 * UserDaoImpl.java 
 * Author: Thomas Maccari
 * 
 * Implements: UserDao
 * 
 * Description: An implementation using postgreSQL to store, update, and delete
 * User related data. 
 * 
 */

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
			String SQL = "INSERT INTO account (email, password) VALUES (?, ?)";
			jdbcTemplate.update(SQL, user.getEmail(), user.getPassword());
			
			
			SQL = "INSERT INTO authority (email, role) VALUES (?, ?)";
			for(String role : user.getRoles().getList()) {
				jdbcTemplate.update(SQL, user.getEmail(), role);
			}
			
			transactionManager.commit(status);
		} catch(Exception e) {
			System.out.println("Error in creating user record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}
	
	@Override
	public void removeUser(User user) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			String SQL = "DELETE FROM account WHERE email = ?";
			jdbcTemplate.update(SQL, user.getEmail());
			
			transactionManager.commit(status);
		} catch(Exception e) {
			System.out.println("Error in removing user record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}
	
	@Override
	public User updateUser(User user) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			String SQL = "DELETE FROM authority WHERE email = ?";
			jdbcTemplate.update(SQL, user.getEmail());
			
			SQL = "DELETE FROM user_program WHERE email = ?";
			jdbcTemplate.update(SQL, user.getEmail());
			
			SQL = "INSERT INTO authority (email, role) VALUES (?, ?)";
			for(String role : user.getRoles().getList()) {
				jdbcTemplate.update(SQL, user.getEmail(), role);
			}
			
			SQL = "INSERT INTO user_program (email, prog_id, program) VALUES (?, ?, ?)";
			for(Program program : user.getPrograms().getList()) {
				jdbcTemplate.update(SQL, user.getEmail(), program.getId(), 
						program.getName());
			}
			
			transactionManager.commit(status);
			
			return user;
		} catch(Exception e) {
			System.out.println("Error in removing user record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}

	@Override
	public List<User> getAllUsers() {
		try {
			String SQL = "SELECT * from account";
			ArrayList<User> users = (ArrayList<User>) jdbcTemplate.query(SQL, 
					new UserMapper());
			
			return users;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	private List<String> getUserRoles(String email) {
		try {
			String SQL = "SELECT role FROM authority WHERE email=?";
			ArrayList<String> roles = new ArrayList<>();
			roles = (ArrayList<String>) jdbcTemplate.query(SQL, new AuthMapper(), email);
			
			return roles;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	// Gets all programs this user is able to access
	private List<Program> getUserPrograms(String email) {
		try {
			String SQL = "SELECT * FROM user_program WHERE email=?";
			ArrayList<Program> programs = new ArrayList<>();
			programs = (ArrayList<Program>) jdbcTemplate.query(SQL, new ProgramMapper(), email);
			
			return programs;
		} catch (EmptyResultDataAccessException e) {
			System.out.println("Error getting programs assigned to user.");
			
			return null;
		}
	}
	
	@Override
	public User getUserByEmail(String email) {
		try {
			String SQL = "SELECT email FROM account WHERE email=?";
			User user = jdbcTemplate.queryForObject(SQL, new UserMapper(), email);
			
			return user;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<String> getRoles() {
		try {
			String SQL = "select * from role";
			
			return jdbcTemplate.query(SQL, (rs, rowNum) -> ("ROLE_" + rs.getString("name")));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	class UserMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setEmail(rs.getString("email"));
			ArrayList<String> roles = (ArrayList<String>) 
					getUserRoles(user.getEmail());
			user.setRoles(new WebList<String>(roles));
			ArrayList<Program> programs = (ArrayList<Program>) 
					getUserPrograms(user.getEmail());
			user.setPrograms(new WebList<Program>(programs));
			
			return user;
		}
	}
	
	class AuthMapper implements RowMapper<String> {
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			String role = rs.getString("role");
			
			return role;
		}
	}
	
	class ProgramMapper implements RowMapper<Program> {
		public Program mapRow(ResultSet rs, int rowNum) throws SQLException {
			Program program = new Program();
			program.setId(rs.getInt("prog_id"));
			program.setName(rs.getString("program"));
			
			return program;
		}
	}
}
