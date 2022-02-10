package com.maccari.abet.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
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

import com.maccari.abet.domain.entity.File;
import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.StudentOutcome;
import com.maccari.abet.domain.entity.Task;
import com.maccari.abet.repository.ProgramDaoImpl.StudentOutcomeMapper;

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
	public void createTask(Task task) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);

		try {
			String SQL = "INSERT INTO task (coordinator, title, assign_date,"
					+ "description, complete) VALUES (?, ?, ?, ?, ?) RETURNING id";

			Instant instant = Instant.now();
			Timestamp ts = instant != null ? Timestamp.from(instant) : null;

			task.setId(jdbcTemplate.query(SQL, new IdMapper(), task.getCoordinator(), task.getTitle(),
					ts.toLocalDateTime(), task.getDescription(), task.isComplete()).get(0));

			insertRelations(task);

			transactionManager.commit(status);
		} catch (Exception e) {
			System.out.println("Error in creating task record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}

	public void insertRelations(Task task) {
		String SQL = "INSERT INTO assigned (id, assignee) VALUES (?, ?)";
		for (String assignee : task.getAssignees()) {
			jdbcTemplate.update(SQL, task.getId(), assignee);
		}

		SQL = "INSERT INTO task_program (task_id, program_id, name) VALUES (?, ?, ?)";
		for (Program program : task.getPrograms()) {
			jdbcTemplate.update(SQL, task.getId(), program.getId(), program.getName());
		}
		
		SQL = "INSERT INTO task_outcome (task_id, outcome_id, name) VALUES (?, ?, ?)";
		for (StudentOutcome outcome: task.getOutcomes()) {
			jdbcTemplate.update(SQL, task.getId(), outcome.getId(), outcome.getName());
		}

		SQL = "INSERT INTO file (task_id) VALUES (?)";
		jdbcTemplate.update(SQL, task.getId());
	}

	@Override
	public Task updateTask(Task task) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		System.out.println("ID: " + task.getId());
		try {
			String SQL = "UPDATE task set coordinator = ?, title = ?,"
					+ " description = ?, complete = ? where id = ?";
			jdbcTemplate.update(SQL, task.getCoordinator(), task.getTitle(), 
					task.getDescription(), task.isComplete(), 
					task.getId());

			SQL = "DELETE FROM assigned WHERE id = ?";
			jdbcTemplate.update(SQL, task.getId());

			SQL = "DELETE FROM task_program WHERE id = ?";
			jdbcTemplate.update(SQL, task.getId());
			
			SQL = "DELETE FROM task_outcome WHERE id = ?";
			jdbcTemplate.update(SQL, task.getId());

			SQL = "DELETE FROM file WHERE task_id = ?";
			jdbcTemplate.update(SQL, task.getId());

			insertRelations(task);

			transactionManager.commit(status);

			return task;
		} catch (Exception e) {
			System.out.println("Error in updating task record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}

	@Override
	public void removeTask(Task task) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);

		try {
			String SQL = "DELETE FROM task WHERE id = ?";
			jdbcTemplate.update(SQL, task.getId());

			transactionManager.commit(status);
		} catch (Exception e) {
			System.out.println("Error in removing task record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}

	@Override
	public List<Task> getAllTasks() {
		ArrayList<Task> tasks = new ArrayList<>();
		try {
			String SQL = "SELECT * from task";
			tasks = (ArrayList<Task>) jdbcTemplate.query(SQL, new SimpleTaskMapper());
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

		return tasks;
	}

	@Override
	public Task getTaskById(int id) {
		try {
			String SQL = "SELECT * FROM task WHERE id = ?";
			Task task = jdbcTemplate.queryForObject(SQL, new FullTaskMapper(), id);

			return task;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<Task> getAssignedTasks(String email) {
		try {
			List<Task> tasks = new ArrayList<Task>();
			List<Integer> taskIds = getAssignedTaskIds(email);
			String SQL = "SELECT * FROM task WHERE id = ?";

			for (Integer id : taskIds) {
				tasks.add(jdbcTemplate.queryForObject(SQL, new SimpleTaskMapper(), id));
			}

			return tasks;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	private List<Integer> getAssignedTaskIds(String email) {
		try {
			String SQL = "SELECT id FROM assigned WHERE assignee = ?";
			List<Integer> taskIds = jdbcTemplate.query(SQL, new IdMapper(), email);

			return taskIds;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<Task> getCreatedTasks(String email) {
		try {
			String SQL = "SELECT * FROM task WHERE coordinator = ?";
			List<Task> tasks = jdbcTemplate.query(SQL, new SimpleTaskMapper(), email);

			return tasks;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	class SimpleTaskMapper implements RowMapper<Task> {
		public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
			Task task = new Task();
			task.setId(rs.getInt("id"));
			task.setTitle(rs.getString("title"));
			task.setCoordinator(rs.getString("coordinator"));
			task.setAssignDate(rs.getObject("assign_date", Timestamp.class));
			task.setComplete(rs.getBoolean("complete"));

			return task;
		}
	}

	class FullTaskMapper implements RowMapper<Task> {
		public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
			Task task = new Task();
			task.setId(rs.getInt("id"));
			task.setTitle(rs.getString("title"));
			task.setCoordinator(rs.getString("coordinator"));
			task.setDescription(rs.getString("description"));
			task.setAssignDate(rs.getObject("assign_date", Timestamp.class));
			task.setComplete(rs.getBoolean("complete"));

			try {
				String SQL = "SELECT assignee FROM assigned WHERE id = ?";
				task.setAssignees(jdbcTemplate.query(SQL, new StringMapper(), task.getId()));

			} catch (EmptyResultDataAccessException e) {
				task.setAssignees(null);
			}

			try {
				String SQL = "SELECT * FROM task_program WHERE task_id = ?";
				task.setPrograms(jdbcTemplate.query(SQL, new ProgramMapper(), task.getId()));

			} catch (EmptyResultDataAccessException e) {
				task.setPrograms(null);
			}

			try {
				String SQL = "SELECT * FROM task_outcome WHERE task_id = ?";
				task.setOutcomes(jdbcTemplate.query(SQL, new StudentOutcomeMapper(), task.getId()));
				System.out.println(task.getOutcomes());

			} catch (EmptyResultDataAccessException e) {
				task.setPrograms(null);
			}
			
			try {
				String SQL = "SELECT * FROM file WHERE task_id = ?";
				task.setFiles(jdbcTemplate.query(SQL, new FileMapper(), task.getId()));

			} catch (EmptyResultDataAccessException e) {
				task.setFiles(null);
			}

			return task;
		}
	}
	
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

	class FileMapper implements RowMapper<File> {
		public File mapRow(ResultSet rs, int rowNum) throws SQLException {
			File file = new File();
			file.setId(rs.getInt("id"));
			file.setTaskId(rs.getInt("task_id"));

			return file;
		}
	}

	class IdMapper implements RowMapper<Integer> {
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt(1);

			return id;
		}
	}

	class StringMapper implements RowMapper<String> {
		public String mapRow(ResultSet rs, int rowNum) throws SQLException {
			String result = rs.getString(1);

			return result;
		}
	}
	
	class ProgramMapper implements RowMapper<Program> {
		public Program mapRow(ResultSet rs, int rowNum) throws SQLException {
			Program program = new Program();
			program.setId(rs.getInt("program_id"));
			program.setName(rs.getString("name"));
			program.setOutcomes(getAllOutcomesForProgram(program.getId()));
			
			return program;
		}
	}
	
	class StudentOutcomeMapper implements RowMapper<StudentOutcome> {
		public StudentOutcome mapRow(ResultSet rs, int rowNum) throws SQLException {
			StudentOutcome outcome = new StudentOutcome();
			outcome.setProgramId(rs.getInt("outcome_id"));
			outcome.setName(rs.getString("name"));
			
			return outcome;
		}
	}
}
