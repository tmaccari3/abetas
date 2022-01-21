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
	public void createTask(Task task) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		int taskId = -1;

		try {
			String SQL = "INSERT INTO task (coordinator, title, outcome, assign_date,"
					+ "description, complete) VALUES (?, ?, ?, ?," + "?, ?) RETURNING id";

			Instant instant = Instant.now();
			Timestamp ts = instant != null ? Timestamp.from(instant) : null;

			taskId = jdbcTemplate.query(SQL, new IdMapper(), task.getCoordinator(), 
					task.getTitle(), task.getOutcome(),
					ts.toLocalDateTime(), task.getDescription(), task.isComplete()).get(0);

			SQL = "INSERT INTO assigned (id, assignee) VALUES (?, ?)";
			for (String assignee : task.getAssignees()) {
				jdbcTemplate.update(SQL, taskId, assignee);
			}

			SQL = "INSERT INTO task_program (id, program) VALUES (?, ?)";
			for (String program : task.getPrograms()) {
				jdbcTemplate.update(SQL, taskId, program);
			}

			SQL = "INSERT INTO file (task_id) VALUES (?)";
			jdbcTemplate.update(SQL, taskId);

			transactionManager.commit(status);
		} catch (Exception e) {
			System.out.println("Error in creating task record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}

	@Override
	public void removeTask(Task task) {
		// TODO Auto-generated method stub

	}

	@Override
	public Task updateTask(Task task) {
		// TODO Auto-generated method stub
		return null;
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
	public List<String> getPrograms() {
		try {
			String sql = "select * from program";

			return jdbcTemplate.query(sql, (rs, rowNum) -> (rs.getString("name")));
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
	public List<Task> getCreatedTasks(String email){
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
			task.setOutcome(rs.getString("outcome"));
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
				String SQL = "SELECT program FROM task_program WHERE id = ?";
				task.setPrograms(jdbcTemplate.query(SQL, new StringMapper(), task.getId()));

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
}
