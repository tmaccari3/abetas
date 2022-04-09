package com.maccari.abet.repository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.Instant;
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

import com.maccari.abet.domain.entity.File;
import com.maccari.abet.domain.entity.ProgramData;
import com.maccari.abet.domain.entity.QTask;
import com.maccari.abet.domain.entity.StudentOutcomeData;
import com.maccari.abet.domain.entity.Task;
import com.maccari.abet.domain.entity.relation.task.QTaskAssignee;
import com.maccari.abet.domain.entity.relation.task.TaskAssignee;
import com.maccari.abet.domain.entity.relation.task.TaskProgram;
import com.maccari.abet.domain.entity.relation.task.TaskStudentOutcome;
import com.maccari.abet.repository.mapper.IdMapper;
import com.maccari.abet.repository.mapper.ProgramMapper;
import com.maccari.abet.repository.mapper.StringMapper;
import com.maccari.abet.repository.mapper.StudentOutcomeMapper;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.Query;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

/*
 * TaskDaoImpl.java 
 * Author: Thomas Maccari
 * 
 * Implements: TaskDao
 * 
 * Description: An implementation using postgreSQL to store, update, and delete
 * Task related data. 
 * 
 */

@Repository
public class TaskDaoImpl implements TaskDao {
	@PersistenceContext
	private EntityManager em;
	
	private JPAQueryFactory queryFactory;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.queryFactory = new JPAQueryFactory(em);
	}

	@Override
	@Transactional
	public <S extends Task> S save(S entity) {
		Instant instant = Instant.now(); Timestamp ts = instant != null ?
				Timestamp.from(instant) : null;
		
		entity.setAssignDate(ts);
		
		Integer tempId = (Integer) em.createNativeQuery("INSERT INTO task (coordinator, "
				+ "title, assign_date, description, due_date, submitted, "
				+ "complete) VALUES (?, ?, ?, ?, ?, ?, ?) RETURNING id")
		.setParameter(1, entity.getCoordinator())
		.setParameter(2, entity.getTitle())
		.setParameter(3, ts)
		.setParameter(4, entity.getDescription())
		.setParameter(5, entity.getDueDate())
		.setParameter(6, entity.isSubmitted())
		.setParameter(7, entity.isComplete())
		.getSingleResult();
		
		System.out.println("saving: " + tempId);
		
		int id = tempId.intValue();
		
		for(TaskProgram program : entity.getPrograms()) {
			program.setTaskId(id);
			em.persist(program);
		}
		
		for(TaskStudentOutcome outcome : entity.getOutcomes()) {
			outcome.setTaskId(id);
			em.persist(outcome);
		}
		
		for(TaskAssignee assignee : entity.getAssignees()){
			assignee.setTaskId(id);
			em.persist(assignee);
		}
		
		if(entity.getFile() != null) {
			em.persist(entity.getFile());
			em.flush();
			
			em.createNativeQuery("INSERT INTO task_file (file_id, task_id) "
					+ "VALUES (?, ?)")
				.setParameter(1, entity.getFile().getId())
				.setParameter(2, id)
				.executeUpdate();	
		}
		
		return entity;
	}
	
	// Inserts a task into the data-source
	@Override
	public int createTask(Task task) {
		/*
		 * DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		 * def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		 * TransactionStatus status = transactionManager.getTransaction(def);
		 * 
		 * try { String SQL = "INSERT INTO task (coordinator, title, assign_date," +
		 * "description, due_date, submitted, complete) VALUES (?, ?, " +
		 * "?, ?, ?, ?, ?) RETURNING id";
		 * 
		 * Instant instant = Instant.now(); Timestamp ts = instant != null ?
		 * Timestamp.from(instant) : null;
		 * 
		 * task.setId(jdbcTemplate.query(SQL, new IdMapper(), task.getCoordinator(),
		 * task.getTitle(), ts.toLocalDateTime(), task.getDescription(),
		 * task.getDueDate(), task.isSubmitted(), task.isComplete()).get(0));
		 * 
		 * insertRelations(task, insertFile(task));
		 * 
		 * transactionManager.commit(status);
		 * 
		 * return task.getId(); } catch (Exception e) {
		 * System.out.println("Error in creating task record, rolling back");
		 * transactionManager.rollback(status); throw e; }
		 */
		return 0;
	}

	// Inserts into the relation tables that, when combined, make up a task
	private void insertRelations(Task task, int fileId) {
		/*
		 * DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		 * def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		 * TransactionStatus status = transactionManager.getTransaction(def); String SQL
		 * = "INSERT INTO assigned (id, assignee) VALUES (?, ?)"; try { for (String
		 * assignee : task.getAssignees()) { jdbcTemplate.update(SQL, task.getId(),
		 * assignee); }
		 * 
		 * SQL =
		 * "INSERT INTO task_program (task_id, program_id, name) VALUES (?, ?, ?)"; for
		 * (Program program : task.getPrograms()) { jdbcTemplate.update(SQL,
		 * task.getId(), program.getId(), program.getName()); }
		 * 
		 * SQL =
		 * "INSERT INTO task_outcome (task_id, outcome_id, name) VALUES (?, ?, ?)"; for
		 * (StudentOutcome outcome : task.getOutcomes()) { jdbcTemplate.update(SQL,
		 * task.getId(), outcome.getId(), outcome.getName()); }
		 * 
		 * if(fileId >= 0) { SQL =
		 * "INSERT INTO task_file (file_id, task_id) VALUES (?, ?)";
		 * jdbcTemplate.update(SQL, fileId, task.getId()); }
		 * 
		 * } catch (Exception e) {
		 * System.out.println("Error in updating document record, rolling back");
		 * transactionManager.rollback(status); throw e; }
		 */

	}

	// Inserts the file portion of the document
	private int insertFile(Task task) {
		/*
		 * DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		 * def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		 * TransactionStatus status = transactionManager.getTransaction(def); try {
		 * String SQL =
		 * "INSERT INTO file (file_name, file_type, file_size, author, data)" +
		 * " VALUES (?, ?, ?, ?, ?) RETURNING id"; File file = task.getFile(); if(file
		 * != null) { int fileId = jdbcTemplate.query(SQL, new IdMapper(),
		 * file.getFileName(), file.getFileType(), file.getFileSize(), file.getAuthor(),
		 * file.getData()).get(0);
		 * 
		 * return fileId; }
		 * 
		 * return -1; } catch (Exception e) {
		 * System.out.println("Error in updating task file record, rolling back");
		 * transactionManager.rollback(status); throw e; }
		 */
		return 0;
	}

	@Override
	@Transactional
	public Task updateTask(Task task) {
		deleteById((long) task.getId());
		save(task);
		/*
		 * DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		 * def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		 * TransactionStatus status = transactionManager.getTransaction(def);
		 * 
		 * try { String SQL = "UPDATE task set coordinator = ?, title = ?," +
		 * " description = ?, submitted = ?, due_date = ?, complete = ? where id = ?";
		 * jdbcTemplate.update(SQL, task.getCoordinator(), task.getTitle(),
		 * task.getDescription(), task.isSubmitted(), task.getDueDate(),
		 * task.isComplete(), task.getId());
		 * 
		 * SQL = "DELETE FROM assigned WHERE id = ?"; jdbcTemplate.update(SQL,
		 * task.getId());
		 * 
		 * SQL = "DELETE FROM task_program WHERE task_id = ?"; jdbcTemplate.update(SQL,
		 * task.getId());
		 * 
		 * SQL = "DELETE FROM task_outcome WHERE task_id = ?"; jdbcTemplate.update(SQL,
		 * task.getId());
		 * 
		 * SQL = "DELETE FROM task_file WHERE task_id = ?"; jdbcTemplate.update(SQL,
		 * task.getId());
		 * 
		 * insertRelations(task, insertFile(task));
		 * 
		 * transactionManager.commit(status);
		 * 
		 * return task; } catch (Exception e) {
		 * System.out.println("Error in updating task record, rolling back");
		 * transactionManager.rollback(status); throw e; }
		 */
		return null;
	}

	@Override
	@Transactional
	public void updateSubmitted(Task task) {
		em.createNativeQuery("UPDATE task SET submitted = ? WHERE id = ?")
			.setParameter(1, task.isSubmitted())
			.setParameter(2, task.getId())
			.executeUpdate();
		/*
		 * DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		 * def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		 * TransactionStatus status = transactionManager.getTransaction(def);
		 * 
		 * try { String SQL = "UPDATE task set submitted = ? where id = ?";
		 * jdbcTemplate.update(SQL, task.isSubmitted(), task.getId());
		 * 
		 * transactionManager.commit(status); } catch (Exception e) {
		 * System.out.println("Error in updating task submitted record, rolling back");
		 * transactionManager.rollback(status); throw e; }
		 */
	}

	@Override
	@Transactional
	public void updateComplete(Task task) {
		em.createNativeQuery("UPDATE task SET complete = ? WHERE id = ?")
			.setParameter(1, task.isComplete())
			.setParameter(2, task.getId())
			.executeUpdate();
		/*
		 * DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		 * def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		 * TransactionStatus status = transactionManager.getTransaction(def);
		 * 
		 * try { String SQL = "UPDATE task set complete = ? where id = ?";
		 * jdbcTemplate.update(SQL, task.isComplete(), task.getId());
		 * 
		 * transactionManager.commit(status); } catch (Exception e) {
		 * System.out.println("Error in updating task submitted record, rolling back");
		 * transactionManager.rollback(status); throw e; }
		 */
	}

	@Override
	@Transactional
	public void delete(Task entity) {
		em.remove(entity);
	}
	
	@Override
	@Transactional
	public void deleteById(Long id) {
		em.createNativeQuery("DELETE FROM task WHERE id=:id")
			.setParameter("id", id.intValue())
			.executeUpdate();
	}
	
	@Override
	public Iterable<Task> findAll() {
		TypedQuery<Task> query = em.createQuery("SELECT t FROM Task t", Task.class);
		
		return query.getResultList();
	}

	@Override
	public List<Task> getAllTasks() {
		/*
		 * ArrayList<Task> tasks = new ArrayList<>(); try { String SQL =
		 * "SELECT * from task"; tasks = (ArrayList<Task>) jdbcTemplate.query(SQL, new
		 * SimpleTaskMapper()); } catch (EmptyResultDataAccessException e) { return
		 * null; }
		 * 
		 * return tasks;
		 */
		return null;
	}
	
	@Override
	public Optional<Task> findById(Long id) {
		int fileId = 0;
		File file = null;
		try {
			fileId = (int) em.createNativeQuery("SELECT file_id FROM task_file"
					+ " WHERE task_id=:id")
					.setParameter("id", id.intValue())
					.getSingleResult();
			file = new File();
			file.setId(fileId);
		} catch (Exception e) {
			System.out.println("Task has no file.");
		}
		
		TypedQuery<Task> query = em.createQuery("SELECT t FROM Task t "
				+ "WHERE t.id=:id", Task.class)
				.setParameter("id", id.intValue());
		
		Task task = query.getSingleResult();
		task.setFile(file);
		
		return Optional.ofNullable(task);
	}

	@Override
	public Task getTaskById(int id) {
		/*
		 * try { String SQL = "SELECT * FROM task WHERE id = ?"; SQL =
		 * "SELECT * FROM task t " + "INNER JOIN assigned a " + "ON t.id = a.id " +
		 * "INNER JOIN task_program p " + "ON t.id = p.task_id " +
		 * "INNER JOIN task_outcome o " + "ON t.id = o.task_id " +
		 * "INNER JOIN task_file f " + "ON t.id = f.task_id " + "WHERE t.id = ?";
		 * 
		 * Task task = jdbcTemplate.queryForObject(SQL, new FullTaskMapper(), id);
		 * 
		 * return task; } catch (Exception e) {
		 * System.out.println("Error getting task record.");
		 * 
		 * return null; }
		 */
		return null;
	}

	@Override
	public List<Task> getAssignedTasks(String email) {
		QTask task = QTask.task;
		QTaskAssignee assignee = QTaskAssignee.taskAssignee;
		JPAQuery<Task> query = queryFactory.selectFrom(task)
				.innerJoin(task.assignees, assignee)
				.on(task.id.eq(assignee.taskId))
				.where(assignee.email.eq(email));
		
		return query.distinct()
				.fetch();
	}

	@Override
	public List<Task> getCreatedTasks(String email) {
		TypedQuery<Task> query = em.createQuery("SELECT t FROM Task t "
				+ "WHERE t.coordinator=:email", Task.class)
				.setParameter("email", email);
		
		return query.getResultList();
	}

	@Override
	public <S extends Task> Iterable<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<Task> findAllById(Iterable<Long> ids) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long count() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Iterable<? extends Task> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}
}
