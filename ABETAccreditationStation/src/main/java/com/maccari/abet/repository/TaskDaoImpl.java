package com.maccari.abet.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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

import com.maccari.abet.domain.entity.Task;
import com.maccari.abet.domain.entity.User;
import com.maccari.abet.repository.UserDaoImpl.UserMapper;

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
			String SQL = "INSERT INTO task (coordinator, title, outcome, "
					+ "description, assign_date, complete) VALUES (?, ?, ?, ?, "
					+ "?, ?) RETURNING id";

			Instant instant = Instant.now();
			Timestamp ts = instant != null ? Timestamp.from(instant) : null;
			
			taskId = jdbcTemplate.query(SQL, new IdMapper(), task.getCoordinator(),
					task.getTitle(), task.getOutcome(), ts, 
					task.isComplete(), task.getDescription()).get(0);

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
		// TODO Auto-generated method stub
		return null;
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

	class SimpleTaskMapper implements RowMapper<Task> {
		public Task mapRow(ResultSet rs, int rowNum) throws SQLException {
			Task task = new Task();
			task.setTitle(rs.getString("title"));
			task.setCoordinator(rs.getString("coordinator"));
			task.setComplete(rs.getBoolean("complete"));

			return task;
		}
	}
	
	class IdMapper implements RowMapper<Integer> {
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt(1);

			return id;
		}
	}
}
