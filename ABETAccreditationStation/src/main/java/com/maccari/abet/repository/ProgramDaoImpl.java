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

import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.QProgram;
import com.maccari.abet.domain.entity.StudentOutcome;
import com.maccari.abet.domain.entity.user.QUserProgram;
import com.maccari.abet.domain.entity.user.UserProgram;
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
	public <S extends Program> S save(S entity) {
		em.persist(entity);
		
		return entity;
	}
	
	@Override
	public void delete(Program entity) {
		em.remove(entity);
		em.createNativeQuery("UPDATE student_outcome SET active=false WHERE prog_id=?")
			.setParameter(1, entity.getId());
	}
	
	@Override
	public void createProgram(Program program) {
		/*
		 * DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		 * def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		 * TransactionStatus status = transactionManager.getTransaction(def);
		 * 
		 * try { String SQL = "INSERT INTO program (name, active) VALUES (?, ?)";
		 * jdbcTemplate.update(SQL, program.getName(), true);
		 * 
		 * transactionManager.commit(status); } catch(Exception e) {
		 * System.out.println("Error in creating program record, rolling back");
		 * transactionManager.rollback(status);
		 * 
		 * throw e; }
		 */
		
	}
	
	@Override
	public void createOutcome(StudentOutcome outcome) {
		/*
		 * DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		 * def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		 * TransactionStatus status = transactionManager.getTransaction(def);
		 * 
		 * try { System.out.println(outcome); String SQL =
		 * "INSERT INTO student_outcome (name, prog_id, active) VALUES (?, ?, ?)";
		 * jdbcTemplate.update(SQL, outcome.getName(), outcome.getProgramId(), true);
		 * 
		 * transactionManager.commit(status); } catch(Exception e) {
		 * System.out.println("Error in creating student outcome record, rolling back");
		 * transactionManager.rollback(status);
		 * 
		 * throw e; }
		 */
	}

	@Override
	public void removeProgram(Program program) {
		/*
		 * DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		 * def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		 * TransactionStatus status = transactionManager.getTransaction(def);
		 * 
		 * try { String SQL = "DELETE FROM program WHERE id=?"; jdbcTemplate.update(SQL,
		 * program.getId());
		 * 
		 * SQL = "UPDATE student_outcome SET active=false WHERE prog_id=?";
		 * jdbcTemplate.update(SQL, program.getId());
		 * 
		 * transactionManager.commit(status); } catch(Exception e) {
		 * System.out.println("Error in deactivating program record, rolling back");
		 * transactionManager.rollback(status); throw e; }
		 */
	}
	
	@Override
	public void removeOutcome(StudentOutcome outcome) {
		/*
		 * DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		 * def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		 * TransactionStatus status = transactionManager.getTransaction(def);
		 * 
		 * try { String SQL = "DELETE FROM student_outcome WHERE id=?";
		 * jdbcTemplate.update(SQL, outcome.getId());
		 * 
		 * transactionManager.commit(status); } catch(Exception e) {
		 * System.out.println("Error in deleting student outcome record, rolling back");
		 * transactionManager.rollback(status); throw e; }
		 */
	}

	@Transactional
	@Override
	public Program updateProgram(Program program) {
		em.joinTransaction();
		em.createNativeQuery("UPDATE user_program SET active=:active "
				+ "WHERE prog_id=:id")
			.setParameter("active", program.isActive())
			.setParameter("id", program.getId())
			.executeUpdate();
		
		return program;
	}
	
	@Override
	public StudentOutcome updateOutcome(StudentOutcome outcome) {
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
	public List<Program> getAllPrograms() {
		TypedQuery<Program> query = em.createQuery("SELECT p FROM Program p", Program.class);
		
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
	public List<StudentOutcome> getAllOutcomesForProgram(int id){
		/*
		 * try { String SQL = "SELECT * FROM student_outcome WHERE prog_id = ?";
		 * ArrayList<StudentOutcome> outcomes = (ArrayList<StudentOutcome>)
		 * jdbcTemplate.query( SQL, new StudentOutcomeMapper(), id);
		 * 
		 * return outcomes; } catch (Exception e) {
		 * System.out.println("Error in getting outcomes for program."); return null; }
		 */
		return null;
	}
	
	@Override
	public List<Program> getActivePrograms() {
		TypedQuery<Program> query = em.createQuery("SELECT p FROM Program p "
				+ "WHERE active = true", Program.class);
		for(Program p : query.getResultList()) {
			System.out.println("getting...");
			System.out.println(p.isActive());
		}
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
	public List<Program> getActivePrograms(String userEmail) {
		QUserProgram program = QUserProgram.userProgram;
		List<UserProgram> programs = queryFactory.selectFrom(program)
				.where(program.email.eq(userEmail)
						.and(program.active.eq(true)))
				.fetch();
		
		List<Program> result = new ArrayList<Program>();
		for(UserProgram userProg : programs) {
			Program prog = new Program();
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

	@Override
	public Optional<Program> findById(Long id) {
		TypedQuery<Program> query = em.createQuery("SELECT p FROM Program p "
				+ "WHERE id=:id", Program.class)
				.setParameter("id", id.intValue());
		
		return Optional.ofNullable(query.getSingleResult());
	}
	
	@Override
	public Program getProgramById(int id) {
		/*
		 * try { String SQL = "SELECT * FROM program WHERE id = ?"; Program program =
		 * jdbcTemplate.queryForObject(SQL, new ProgramMapper(), id);
		 * 
		 * return program; } catch (Exception e) { return null; }
		 */
		return null;
	}
	
	@Override
	public StudentOutcome getOutcomeById(int id) {
		/*
		 * try { String SQL = "SELECT * FROM student_outcome WHERE id = ?";
		 * StudentOutcome outcome = jdbcTemplate.queryForObject(SQL, new
		 * StudentOutcomeMapper(), id);
		 * 
		 * return outcome; } catch (Exception e) {
		 * System.out.println("Error in getting outcomes."); return null; }
		 */
		return null;
	}
	
	// This program mapper is unique for this class and gets more data
	class ProgramMapper implements RowMapper<Program> {
		public Program mapRow(ResultSet rs, int rowNum) throws SQLException {
			Program program = new Program();
			program.setId(rs.getInt("id"));
			program.setName(rs.getString("name"));
			program.setActive(rs.getBoolean("active"));
			program.setOutcomes(getAllOutcomesForProgram(program.getId()));
			
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
	public <S extends Program> Iterable<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<Program> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Program> findAllById(Iterable<Long> ids) {
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
	public void deleteAll(Iterable<? extends Program> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}
}
