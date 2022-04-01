package com.maccari.abet.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;

import com.maccari.abet.domain.entity.ProgramData;
import com.maccari.abet.domain.entity.StudentOutcome;
import com.maccari.abet.domain.entity.relation.QUserProgram;
import com.maccari.abet.domain.entity.relation.UserProgram;
import com.maccari.abet.repository.mapper.IdMapper;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

/*
 * ProgramDaoImpl.java 
 * Author: Thomas Maccari
 * 
 * Implements: ProgramDao
 * 
 * Description: An implementation using postgreSQL to store, update, and delete
 * Program related data. 
 * 
 */

@Repository
public class ProgramDaoImpl implements ProgramDao {
	@PersistenceContext
	private EntityManager em;

	private JPAQueryFactory queryFactory;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	@Override
	public <S extends ProgramData> S save(S entity) {
		em.persist(entity);
		
		return entity;
	}
	
	@Override
	public void delete(ProgramData entity) {
		em.remove(entity);
		em.createNativeQuery("UPDATE student_outcome SET active=false WHERE prog_id=?")
			.setParameter(1, entity.getId());
	}
	
	@Override
	public Optional<ProgramData> findById(Long id) {
		TypedQuery<ProgramData> query = em.createQuery("SELECT p FROM ProgramData p "
				+ "WHERE id=:id", ProgramData.class)
				.setParameter("id", id.intValue());
		
		return Optional.ofNullable(query.getSingleResult());
	}

	@Transactional
	@Override
	public ProgramData updateProgram(ProgramData program) {
		em.joinTransaction();
		em.createNativeQuery("UPDATE user_program SET active=:active "
				+ "WHERE prog_id=:id")
			.setParameter("active", program.isActive())
			.setParameter("id", program.getId())
			.executeUpdate();
		
		return program;
	}
	
	@Override
	public List<ProgramData> getAllPrograms() {
		TypedQuery<ProgramData> query = em.createQuery("SELECT p FROM ProgramData p", 
				ProgramData.class);
		
		return query.getResultList();
		/*
		 * try { String SQL = "SELECT * FROM program"; ArrayList<Program> programs =
		 * (ArrayList<Program>) jdbcTemplate.query( SQL, new ProgramMapper());
		 * 
		 * for(Program program: programs) {
		 * program.setOutcomes(getAllOutcomesForProgram(program.getId())); } return
		 * programs; } catch (EmptyResultDataAccessException e) { return null; }
		 */
	}
	
	@Override
	public List<ProgramData> getActivePrograms() {
		TypedQuery<ProgramData> query = em.createQuery("SELECT p FROM ProgramData p "
				+ "WHERE active = true", ProgramData.class);
		
		return query.getResultList();
		/*
		 * try { String SQL = "SELECT * FROM program WHERE active = true";
		 * ArrayList<Program> programs = (ArrayList<Program>) jdbcTemplate.query( SQL,
		 * new ProgramMapper());
		 * 
		 * for(Program program: programs) {
		 * program.setOutcomes(getActiveOutcomesForProgram(program.getId())); } return
		 * programs; } catch (EmptyResultDataAccessException e) { return null; }
		 */
	}
	
	// Gets all Programs that are 'active' in the system
	@Override
	public List<ProgramData> getActivePrograms(String userEmail) {
		QUserProgram program = QUserProgram.userProgram;
		List<UserProgram> programs = queryFactory.selectFrom(program)
				.where(program.email.eq(userEmail)
						.and(program.active.eq(true)))
				.fetch();
		
		List<ProgramData> result = new ArrayList<ProgramData>();
		for(UserProgram userProg : programs) {
			ProgramData prog = new ProgramData();
			prog.setId(userProg.getId());
			prog.setName(userProg.getName());
			prog.setOutcomes(getActiveOutcomesForProgram(userProg.getId()));
			result.add(prog);
		}
		
		return result;
	}

	// Gets all Outcomes that are 'active' in the system for a given Program
	@Override
	public List<StudentOutcome> getActiveOutcomesForProgram(int id) {
		TypedQuery<StudentOutcome> query = em.createQuery("SELECT s FROM StudentOutcome s "
				+ "WHERE prog_id=:id AND active = true", StudentOutcome.class);
		query.setParameter("id", id);
		
		return query.getResultList();
		/*
		 * try { String SQL =
		 * "SELECT * FROM student_outcome WHERE prog_id = ? and active = true";
		 * ArrayList<StudentOutcome> outcomes = (ArrayList<StudentOutcome>)
		 * jdbcTemplate.query( SQL, new StudentOutcomeMapper(), id);
		 * 
		 * return outcomes; } catch (Exception e) {
		 * System.out.println("Error in getting outcomes for program."); return null; }
		 */
	}
	
	// This program mapper is unique for this class and gets more data
	class ProgramMapper implements RowMapper<ProgramData> {
		public ProgramData mapRow(ResultSet rs, int rowNum) throws SQLException {
			ProgramData program = new ProgramData();
			program.setId(rs.getInt("id"));
			program.setName(rs.getString("name"));
			program.setActive(rs.getBoolean("active"));
			//program.setOutcomes(getAllOutcomesForProgram(program.getId()));
			
			return program;
		}
	}
	
	// This outcome mapper is unique for this class and gets more data
	class StudentOutcomeMapper implements RowMapper<StudentOutcome> {
		public StudentOutcome mapRow(ResultSet rs, int rowNum) throws SQLException {
			StudentOutcome outcome = new StudentOutcome();
			outcome.setId(rs.getInt("id"));
			//outcome.setProgramId(rs.getInt("prog_id"));
			outcome.setName(rs.getString("name"));
			outcome.setActive(rs.getBoolean("active"));
			
			return outcome;
		}
	}

	@Override
	public <S extends ProgramData> Iterable<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<ProgramData> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<ProgramData> findAllById(Iterable<Long> ids) {
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
	public void deleteAll(Iterable<? extends ProgramData> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}
}
