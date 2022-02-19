package com.maccari.abet.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;
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
import com.maccari.abet.repository.mapper.IdMapper;
import com.maccari.abet.repository.mapper.StringMapper;

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
					+ "description, due_date, submitted, complete) VALUES (?, ?, "
					+ "?, ?, ?, ?, ?) RETURNING id";

			Instant instant = Instant.now();
			Timestamp ts = instant != null ? Timestamp.from(instant) : null;

			task.setId(jdbcTemplate.query(SQL, new IdMapper(), task.getCoordinator(), 
					task.getTitle(), ts.toLocalDateTime(), task.getDescription(),
					task.getDueDate(), task.isSubmitted(), task.isComplete()).get(0));
			
			insertRelations(task, insertFile(task));

			transactionManager.commit(status);
		} catch (Exception e) {
			System.out.println("Error in creating task record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}

	private void insertRelations(Task task, int fileId) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		String SQL = "INSERT INTO assigned (id, assignee) VALUES (?, ?)";
		try {
			for (String assignee : task.getAssignees()) {
				jdbcTemplate.update(SQL, task.getId(), assignee);
			}

			SQL = "INSERT INTO task_program (task_id, program_id, name) VALUES (?, ?, ?)";
			for (Program program : task.getPrograms()) {
				jdbcTemplate.update(SQL, task.getId(), program.getId(), program.getName());
			}

			SQL = "INSERT INTO task_outcome (task_id, outcome_id, name) VALUES (?, ?, ?)";
			for (StudentOutcome outcome : task.getOutcomes()) {
				jdbcTemplate.update(SQL, task.getId(), outcome.getId(), outcome.getName());
			}

			if(fileId >= 0) {
				SQL = "INSERT INTO task_file (file_id, task_id) VALUES (?, ?)";
				jdbcTemplate.update(SQL, fileId, task.getId());	
			}

		} catch (Exception e) {
			System.out.println("Error in updating document record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}
	
	private int insertFile(Task task) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			String SQL = "INSERT INTO file (file_name, file_type, file_size, author, data)"
					+ " VALUES (?, ?, ?, ?, ?) RETURNING id";
			File file = task.getFile();
			if(file != null) {
				int fileId = jdbcTemplate.query(SQL, new IdMapper(), file.getFileName(),
						file.getFileType(), file.getFileSize(), file.getAuthor(),
						file.getData()).get(0);
				
				return fileId;
			}
			
			return -1;
		} catch (Exception e) {
			System.out.println("Error in updating task file record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}

	@Override
	public Task updateTask(Task task) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		
		try {
			String SQL = "UPDATE task set coordinator = ?, title = ?,"
					+ " description = ?, submitted = ?, due_date = ?, complete = ? where id = ?";
			jdbcTemplate.update(SQL, task.getCoordinator(), task.getTitle(), 
					task.getDescription(), task.isSubmitted(), task.getDueDate(),
					task.isComplete(), task.getId());

			SQL = "DELETE FROM assigned WHERE id = ?";
			jdbcTemplate.update(SQL, task.getId());

			SQL = "DELETE FROM task_program WHERE task_id = ?";
			jdbcTemplate.update(SQL, task.getId());
			
			SQL = "DELETE FROM task_outcome WHERE task_id = ?";
			jdbcTemplate.update(SQL, task.getId());

			SQL = "DELETE FROM task_file WHERE task_id = ?";
			jdbcTemplate.update(SQL, task.getId());

			insertRelations(task, insertFile(task));

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
			task.setSubmitted(rs.getBoolean("submitted"));
			task.setDueDate(rs.getDate("due_date"));
			task.setComplete(rs.getBoolean("complete"));

			try {
				String SQL = "SELECT assignee FROM assigned WHERE id = ?";
				task.setAssignees(jdbcTemplate.query(SQL, new StringMapper(), task.getId()));
			} catch (Exception e) {
				task.setAssignees(null);
			}

			try {
				String SQL = "SELECT * FROM task_program WHERE task_id = ?";
				System.out.println(SQL + task.getId());
				task.setPrograms(jdbcTemplate.query(SQL, new ProgramMapper(), task.getId()));
			} catch (Exception e) {
				System.out.println("Error in getting programs for task.");
				
				task.setPrograms(null);
			}

			try {
				String SQL = "SELECT * FROM task_outcome WHERE task_id = ?";
				System.out.println(SQL + task.getId());
				task.setOutcomes(jdbcTemplate.query(SQL, new StudentOutcomeMapper(), task.getId()));
				System.out.println(task.getOutcomes());
			} catch (Exception e) {
				System.out.println("Error in getting outcomes for task.");
				
				task.setPrograms(null);
			}
			
			try {
				String SQL = "SELECT * FROM task_file WHERE task_id = ?";
				int fileId = jdbcTemplate.query(SQL, new IdMapper(), task.getId()).get(0);
				
				File file = new File();
				file.setId(fileId);
				task.setFile(file);

			} catch (Exception e) {
				System.out.println("Error in getting file for task.");
				
				task.setFile(null);
			}

			return task;
		}
	}
	
	private List<StudentOutcome> getAllOutcomesForProgram(int id){
		try {
			String SQL = "SELECT * FROM student_outcome WHERE prog_id = ?";
			ArrayList<StudentOutcome> outcomes = (ArrayList<StudentOutcome>) jdbcTemplate.query(
					SQL, new StudentOutcomeMapper(), id);
			
			return outcomes;
		} catch (Exception e) {
			System.out.println("Error in getting outcomes for program.");

			return new ArrayList<StudentOutcome>();
		}
	}

	class FileMapper implements RowMapper<File> {
		public File mapRow(ResultSet rs, int rowNum) throws SQLException {
			File file = new File();
			file.setId(rs.getInt("id"));
			file.setFileName(rs.getString("filename"));
			file.setFileType(rs.getString("filetype"));
			file.setFileSize(rs.getLong("fileSize"));
			file.setAuthor(rs.getString("author"));
			file.setData(rs.getBytes("data"));

			return file;
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
			outcome.setId(rs.getInt("outcome_id"));
			outcome.setName(rs.getString("name"));
			
			return outcome;
		}
	}
}
