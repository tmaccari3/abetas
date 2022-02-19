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
import com.maccari.abet.domain.entity.StudentOutcome;

@Repository
public class ProgramDaoImpl implements ProgramDao {
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
	public void createProgram(Program program) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			String SQL = "INSERT INTO program (name, active) VALUES (?, ?)";
			jdbcTemplate.update(SQL, program.getName(), true);
			
			transactionManager.commit(status);
		} catch(Exception e) {
			System.out.println("Error in creating program record, rolling back");
			transactionManager.rollback(status);
			//throw e;
		}
	}
	
	@Override
	public void createOutcome(StudentOutcome outcome) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			String SQL = "INSERT INTO student_outcome (name, prog_id, active) VALUES (?, ?, ?)";
			jdbcTemplate.update(SQL, outcome.getName(), outcome.getProgramId(), true);
			
			transactionManager.commit(status);
		} catch(Exception e) {
			System.out.println("Error in creating student outcome record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}

	@Override
	public void removeProgram(Program program) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			String SQL = "DELETE FROM program WHERE id=?";
			jdbcTemplate.update(SQL, program.getId());
			
			SQL = "UPDATE student_outcome SET active=false WHERE prog_id=?";
			jdbcTemplate.update(SQL, program.getId());
			
			transactionManager.commit(status);
		} catch(Exception e) {
			System.out.println("Error in deactivating program record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}
	
	@Override
	public void removeOutcome(StudentOutcome outcome) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			String SQL = "DELETE FROM student_outcome WHERE id=?";
			jdbcTemplate.update(SQL, outcome.getId());
			
			transactionManager.commit(status);
		} catch(Exception e) {
			System.out.println("Error in deleting student outcome record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}

	@Override
	public Program updateProgram(Program program) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			String SQL = "UPDATE program SET active=? WHERE id=?";
			jdbcTemplate.update(SQL, program.isActive(), program.getId());
			
			SQL = "UPDATE student_outcome SET active=? WHERE prog_id=?";
			jdbcTemplate.update(SQL, program.isActive(), program.getId());
			
			transactionManager.commit(status);
			return program;
		} catch(Exception e) {
			System.out.println("Error in updating program record, rolling back");
			transactionManager.rollback(status);
			
			return null;
			//throw e;
		}
	}
	
	@Override
	public StudentOutcome updateOutcome(StudentOutcome outcome) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			String SQL = "UPDATE student_outcome SET active=? WHERE id=?";
			jdbcTemplate.update(SQL, outcome.isActive(), outcome.getId());
			
			transactionManager.commit(status);
			return outcome;
		} catch(Exception e) {
			System.out.println("Error in updating student outcome record, rolling back");
			transactionManager.rollback(status);
			
			return null;
			//throw e;
		}
	}

	@Override
	public List<Program> getAllPrograms() {
		try {
			String SQL = "SELECT * FROM program";
			ArrayList<Program> programs = (ArrayList<Program>) jdbcTemplate.query(
					SQL, new ProgramMapper());
			
			for(Program program: programs) {
				program.setOutcomes(getAllOutcomesForProgram(program.getId()));
			}
			return programs;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	@Override
	public List<StudentOutcome> getAllOutcomesForProgram(int id){
		try {
			String SQL = "SELECT * FROM student_outcome WHERE prog_id = ?";
			ArrayList<StudentOutcome> outcomes = (ArrayList<StudentOutcome>) jdbcTemplate.query(
					SQL, new StudentOutcomeMapper(), id);
			
			return outcomes;
		} catch (Exception e) {
			System.out.println("Error in getting outcomes for program.");
			return null;
		}
	}
	
	@Override
	public List<Program> getActivePrograms() {
		try {
			String SQL = "SELECT * FROM program WHERE active = true";
			ArrayList<Program> programs = (ArrayList<Program>) jdbcTemplate.query(
					SQL, new ProgramMapper());
			
			for(Program program: programs) {
				program.setOutcomes(getActiveOutcomesForProgram(program.getId()));
			}
			return programs;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<StudentOutcome> getActiveOutcomesForProgram(int id) {
		try {
			String SQL = "SELECT * FROM student_outcome WHERE prog_id = ? and active = true";
			ArrayList<StudentOutcome> outcomes = (ArrayList<StudentOutcome>) jdbcTemplate.query(
					SQL, new StudentOutcomeMapper(), id);
			
			return outcomes;
		} catch (Exception e) {
			System.out.println("Error in getting outcomes for program.");
			return null;
		}
	}

	@Override
	public StudentOutcome getOutcomeById(int id) {
		try {
			String SQL = "SELECT * FROM student_outcome WHERE id = ?";
			StudentOutcome outcome = jdbcTemplate.queryForObject(SQL, new StudentOutcomeMapper(), id);
			
			return outcome;
		} catch (Exception e) {
			System.out.println("Error in getting outcomes.");
			return null;
		}
	}
	
	@Override
	public Program getProgramById(int id) {
		try {
			String SQL = "SELECT * FROM program WHERE id = ?";
			Program program = jdbcTemplate.queryForObject(SQL, new ProgramMapper(), id);
			
			return program;
		} catch (Exception e) {
			return null;
		}
	}
	
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
	
	class StudentOutcomeMapper implements RowMapper<StudentOutcome> {
		public StudentOutcome mapRow(ResultSet rs, int rowNum) throws SQLException {
			StudentOutcome outcome = new StudentOutcome();
			outcome.setId(rs.getInt("id"));
			outcome.setProgramId(rs.getInt("prog_id"));
			outcome.setName(rs.getString("name"));
			outcome.setActive(rs.getBoolean("active"));
			
			return outcome;
		}
	}
}
