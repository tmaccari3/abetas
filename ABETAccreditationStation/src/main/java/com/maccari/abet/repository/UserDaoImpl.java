package com.maccari.abet.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.jpa.repository.support.SimpleJpaRepository;
import org.springframework.data.repository.CrudRepository;
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
	public <S extends User> S save(S entity) {
	    if (entity.isNew()) {
	        em.persist(entity);
	        
	        return entity;
	    } else {
	    	em.createNativeQuery("DELETE FROM authority WHERE email = ?")
	    		.setParameter(1, entity.getEmail())
	    		.executeUpdate();
	    	em.createNativeQuery("DELETE FROM user_program WHERE email = ?")
	    		.setParameter(1, entity.getEmail())
	    		.executeUpdate();
	    	
			for(String role : entity.getRoles()) {
				em.createNativeQuery("INSERT INTO authority (email, role) VALUES (?, ?)")
					.setParameter(1, entity.getEmail())
					.setParameter(2, role)
					.executeUpdate();
			}
			
			for(Program program : entity.getPrograms()) {
				em.createNativeQuery("INSERT INTO user_program (email, prog_id, "
						+ "program, active) VALUES (?, ?, ?, ?)")
					.setParameter(1, entity.getEmail())
					.setParameter(2, program.getId())
					.setParameter(3, program.getName())
					.setParameter(4, program.isActive())
					.executeUpdate();
			}
			
	        return entity;
	    }
	}
	
	@Override
	public void delete(User entity) {
		em.remove(entity);
	}

	@Override
	public List<User> getAllUsers() {
		TypedQuery<User> query = em.createQuery("SELECT u from User u", User.class);
		
		return query.getResultList();

		/*try {
			String SQL = "SELECT * from account";
			ArrayList<User> users = (ArrayList<User>) jdbcTemplate.query(SQL, 
					new UserMapper());
			
			return users;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}*/
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
		try {
			TypedQuery<User> query = em.createQuery("SELECT u from User u "
					+ "WHERE u.email=:email", User.class);
			query.setParameter("email", email);

			return query.getSingleResult();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			
			return null;
		}
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

	@Override
	public <S extends User> Iterable<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Optional<User> findById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<User> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<User> findAllById(Iterable<Long> ids) {
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
	public void deleteAll(Iterable<? extends User> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}
}
