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

import com.maccari.abet.domain.entity.Document;
import com.maccari.abet.domain.entity.File;
import com.maccari.abet.domain.entity.Program;
import com.maccari.abet.domain.entity.StudentOutcome;
import com.maccari.abet.domain.entity.Task;
import com.maccari.abet.domain.entity.web.DocumentSearch;
import com.maccari.abet.repository.TaskDaoImpl.SimpleTaskMapper;

@Repository
public class DocumentDaoImpl implements DocumentDao {
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
	public void createDocument(Document document) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);

		try {
			String SQL = "INSERT INTO document (author, title, description, "
					+ "submit_date, task_id, task) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";

			Instant instant = Instant.now();
			Timestamp ts = instant != null ? Timestamp.from(instant) : null;

			document.setId(jdbcTemplate.query(SQL, new IdMapper(), document.getAuthor(), 
					document.getTitle(), document.getDescription(), 
					ts.toLocalDateTime(), document.getTaskId(), document.isTask()).get(0));

			insertRelations(document, insertFile(document));

			transactionManager.commit(status);
		} catch (Exception e) {
			System.out.println("Error in creating task record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}

	private int insertFile(Document document) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);
		try {
			String SQL = "INSERT INTO file (file_name, file_type, file_size, author, data)"
					+ " VALUES (?, ?, ?, ?, ?) RETURNING id";
			File file = document.getFile();
			int fileId = jdbcTemplate.query(SQL, new IdMapper(), file.getFileName(), 
					file.getFileType(), file.getFileSize(), file.getAuthor(), 
					file.getData()).get(0);

			return fileId;
		} catch (Exception e) {
			System.out.println("Error in updating task file record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}

	private void insertRelations(Document document, int fileId) {
		DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		TransactionStatus status = transactionManager.getTransaction(def);

		try {
			String SQL = "INSERT INTO document_program (doc_id, program_id, name) "
					+ "VALUES (?, ?, ?)";
			for (Program program : document.getPrograms()) {
				jdbcTemplate.update(SQL, document.getId(), program.getId(), program.getName());
			}

			SQL = "INSERT INTO document_outcome (doc_id, outcome_id, name) VALUES (?, ?, ?)";
			for (StudentOutcome outcome : document.getOutcomes()) {
				jdbcTemplate.update(SQL, document.getId(), outcome.getId(), outcome.getName());
			}

			SQL = "INSERT INTO document_file (file_id, doc_id) VALUES (?, ?)";
			jdbcTemplate.update(SQL, fileId, document.getId());

		} catch (Exception e) {
			System.out.println("Error in updating document record, rolling back");
			transactionManager.rollback(status);
			throw e;
		}
	}

	@Override
	public List<Document> getAll() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public List<Document> getBySearch(DocumentSearch search) {
		ArrayList<Document> docs = new ArrayList<>();
		try {
			String SQL = "SELECT * FROM document d "
					+ "INNER JOIN document_program dp "
					+ "ON d.id = dp.doc_id";
			int size = search.getPrograms().size();
			boolean dates = search.getToDate() != null || search.getFromDate() != null;
			boolean programs = size > 0;
			if(dates || programs) {
				SQL += " WHERE";
			}
			
			if(size == 0) {
			}
			else if(size == 1) {
				SQL += " p.program_id = " + search.getPrograms().get(0);
			}
			else {
				int i;
				for(i = 0; i < size - 1; i++) {
					SQL += " dp.program_id = " + search.getPrograms().get(i) + " or";
				}
				SQL += " dp.program_id = " + search.getPrograms().get(i);
			}
			if(search.getToDate() != null && search.getFromDate() != null) {
				SQL += " submit_date >= '" + search.getFormattedDate(search.getFromDate()) 
					+ "' and submit_date <= '" + search.getFormattedDate(search.getToDate()) + "'";
			}
			else if(search.getFromDate() != null) {
				System.out.println(search.getFromDate());
				if(size == 0) {
					SQL += " submit_date >= '" + search.getFormattedDate(search.getFromDate()) + "'";
				}
			}
			else if(search.getToDate() != null) {
				System.out.println(search.getToDate());
				if(size == 0) {
					SQL += " submit_date <= '" + search.getFormattedDate(search.getToDate()) + "'";
				}
			}
			SQL += " LIMIT ?";
			System.out.println(SQL);
			docs = (ArrayList<Document>) jdbcTemplate.query(SQL, new FullDocMapper(), search.getCount());
			
			return docs;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	@Override
	public Document getById(int id) {
		try {
			String SQL = "SELECT * FROM document WHERE id = ?";
			Document doc = jdbcTemplate.queryForObject(SQL, new FullDocMapper(), id);
			
			return doc;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}

	@Override
	public List<Document> getRecentDocuments(int amount) {
		ArrayList<Document> docs = new ArrayList<>();
		try {
			String SQL = "SELECT * FROM document ORDER BY submit_date DESC LIMIT ?";
			docs = (ArrayList<Document>) jdbcTemplate.query(SQL, new FullDocMapper(), amount);
		} catch (EmptyResultDataAccessException e) {
			return null;
		}

		return docs;
	}
	
	@Override
	public List<Document> getDocsForTask(int taskId) {
		ArrayList<Document> docs = new ArrayList<>();
		try {
			String SQL = "SELECT * FROM document WHERE task_id = ?";
			docs = (ArrayList<Document>) jdbcTemplate.query(SQL, new FullDocMapper(), taskId);
			
			return docs;
		} catch (EmptyResultDataAccessException e) {
			return null;
		}
	}
	
	class SimpleDocMapper implements RowMapper<Document> {
		public Document mapRow(ResultSet rs, int rowNum) throws SQLException {
			Document document = new Document();
			document.setId(rs.getInt("id"));
			document.setTaskId(rs.getInt("task_id"));
			document.setTitle(rs.getString("title"));
			document.setAuthor(rs.getString("author"));
			document.setDescription(rs.getString("description"));
			document.setSubmitDate(rs.getObject("submit_date", Timestamp.class));
			document.setTask(rs.getBoolean("task"));

			return document;
		}
	}
	
	class FullDocMapper implements RowMapper<Document> {
		public Document mapRow(ResultSet rs, int rowNum) throws SQLException {
			Document document = new Document();
			document.setId(rs.getInt("id"));
			document.setTaskId(rs.getInt("task_id"));
			document.setTitle(rs.getString("title"));
			document.setAuthor(rs.getString("author"));
			document.setDescription(rs.getString("description"));
			document.setSubmitDate(rs.getObject("submit_date", Timestamp.class));
			document.setTask(rs.getBoolean("task"));


			try {
				String SQL = "SELECT * FROM document_program WHERE doc_id = ?";
				document.setPrograms(jdbcTemplate.query(SQL, new ProgramMapper(), 
						document.getId()));

			} catch (EmptyResultDataAccessException e) {
				document.setPrograms(null);
			}

			try {
				String SQL = "SELECT * FROM document_outcome WHERE doc_id = ?";
				document.setOutcomes(jdbcTemplate.query(SQL, new StudentOutcomeMapper(), 
						document.getId()));

			} catch (EmptyResultDataAccessException e) {
				document.setPrograms(null);
			}
			
			try {
				String SQL = "SELECT * FROM document_file WHERE doc_id = ?";
				int fileId = jdbcTemplate.query(SQL, new IdMapper(), document.getId()).get(0);
				
				File file = new File();
				file.setId(fileId);
				document.setFile(file);

			} catch (EmptyResultDataAccessException e) {
				document.setPrograms(null);
			}

			return document;
		}
	}
	
	class ProgramMapper implements RowMapper<Program> {
		public Program mapRow(ResultSet rs, int rowNum) throws SQLException {
			Program program = new Program();
			program.setId(rs.getInt("program_id"));
			program.setName(rs.getString("name"));
			
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

	class IdMapper implements RowMapper<Integer> {
		public Integer mapRow(ResultSet rs, int rowNum) throws SQLException {
			int id = rs.getInt(1);

			return id;
		}
	}
}
