package com.maccari.abet.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
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
import com.maccari.abet.domain.entity.QUser;
import com.maccari.abet.domain.entity.User;
import com.maccari.abet.domain.entity.searchable.QSearchableDocument;
import com.maccari.abet.domain.entity.searchable.SearchableDocument;
import com.maccari.abet.utility.WebList;
import com.querydsl.jpa.impl.JPAQueryFactory;

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
	@PersistenceContext
	private EntityManager em;

	@Override
	public void createUser(User user) {
		/*DefaultTransactionDefinition def = new DefaultTransactionDefinition();
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
		}*/
	}
	
	@Override
	public void removeUser(User user) {
		/*DefaultTransactionDefinition def = new DefaultTransactionDefinition();
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
		}*/
	}
	
	@Override
	public User updateUser(User user) {
		/*DefaultTransactionDefinition def = new DefaultTransactionDefinition();
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
		}*/
		return null;
	}

	@Override
	public List<User> getAllUsers() {
		TypedQuery<User> query = em.createQuery("SELECT u from User u", User.class);
		List<User> users = query.getResultList();
		
		return users;
		/*try {
			String SQL = "SELECT * from account";
			ArrayList<User> users = (ArrayList<User>) jdbcTemplate.query(SQL, 
					new UserMapper());
			
			return users;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}*/
	}
	
	private List<String> getUserRoles(String email) {
		/*try {
			String SQL = "SELECT role FROM authority WHERE email=?";
			ArrayList<String> roles = new ArrayList<>();
			roles = (ArrayList<String>) jdbcTemplate.query(SQL, new AuthMapper(), email);
			
			return roles;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}*/
		return null;
	}
	
	// Gets all programs this user is able to access
	private List<Program> getUserPrograms(String email) {
		TypedQuery<Program> query = em.createQuery("SELECT p FROM Program p "
				+ "WHERE p.email=:email", Program.class);
		query.setParameter("email", email);

		return query.getResultList();
		/*try {
			String SQL = "SELECT * FROM user_program WHERE email=?";
			ArrayList<Program> programs = new ArrayList<>();
			programs = (ArrayList<Program>) jdbcTemplate.query(SQL, new ProgramMapper(), email);
			
			return programs;
		} catch (EmptyResultDataAccessException e) {
			System.out.println("Error getting programs assigned to user.");
			
			return null;
		}*/
	}
	
	@Override
	public User getUserByEmail(String email) {
		TypedQuery<User> query = em.createQuery("SELECT u from User u "
				+ "WHERE u.email=:email", User.class);
		query.setParameter("email", email);

		return query.getSingleResult();
		/*try {
			String SQL = "SELECT email FROM account WHERE email=?";
			User user = jdbcTemplate.queryForObject(SQL, new UserMapper(), email);
			
			return user;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}*/
	}

	@Override
	public List<String> getRoles() {
		Query query = em.createQuery("SELECT name FROM Role", String.class);
		List<String> roles = (List<String>) query.getResultList();
		
		return roles;
		/*try {
			String SQL = "SELECT * from role";
			
			return jdbcTemplate.query(SQL, (rs, rowNum) -> ("ROLE_" + rs.getString("name")));
		} catch (EmptyResultDataAccessException e) {
			return null;
		}*/
	}
	
	class UserMapper implements RowMapper<User> {
		public User mapRow(ResultSet rs, int rowNum) throws SQLException {
			User user = new User();
			user.setEmail(rs.getString("email"));
			ArrayList<String> roles = (ArrayList<String>) 
					getUserRoles(user.getEmail());
			//user.setRoles(new WebList<String>(roles));
			ArrayList<Program> programs = (ArrayList<Program>) 
					getUserPrograms(user.getEmail());
			//user.setPrograms(new WebList<Program>(programs));
			
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
