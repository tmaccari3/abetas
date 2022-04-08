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
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

import com.maccari.abet.domain.entity.Document;
import com.maccari.abet.domain.entity.File;
import com.maccari.abet.domain.entity.QDocument;
import com.maccari.abet.domain.entity.relation.document.DocumentProgram;
import com.maccari.abet.domain.entity.relation.document.DocumentStudentOutcome;
import com.maccari.abet.domain.entity.relation.document.DocumentTag;
import com.maccari.abet.domain.entity.relation.document.QDocumentProgram;
import com.maccari.abet.domain.entity.relation.document.QDocumentTag;
import com.maccari.abet.domain.entity.web.DocumentSearch;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;

/*
 * DocumentDaoImpl.java 
 * Author: Thomas Maccari
 * 
 * Implements: DocumentDao
 * 
 * Description: An implementation using postgreSQL to store, search, and delete
 * document related data. 
 * 
 */

@Repository
public class DocumentDaoImpl implements DocumentDao {
	@PersistenceContext
	private EntityManager em;

	private JPAQueryFactory queryFactory;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		this.queryFactory = new JPAQueryFactory(em);
	}
	
	@Override
	public <S extends Document> S save(S entity) {
		Instant instant = Instant.now(); Timestamp ts = instant != null ?
				Timestamp.from(instant) : null;
		
		entity.setSubmitDate(ts);
		
		Integer tempId = (Integer) em.createNativeQuery("INSERT INTO document (author, title, description, " +
				 "submit_date, task_id, task) VALUES (?, ?, ?, ?, ?, ?) RETURNING id")
		.setParameter(1, entity.getAuthor())
		.setParameter(2, entity.getTitle())
		.setParameter(3, entity.getDescription())
		.setParameter(4, ts)
		.setParameter(5, entity.getTaskId())
		.setParameter(6, entity.isTask())
		.getSingleResult();
		
		int id = tempId.intValue();
		
		for(DocumentProgram program : entity.getPrograms()) {
			program.setDocId(id);
			em.persist(program);
		}
		
		for(DocumentStudentOutcome outcome : entity.getOutcomes()) {
			outcome.setDocId(id);
			em.persist(outcome);
		}
		
		for(DocumentTag tag : entity.getTags()) {
			em.createNativeQuery("INSERT INTO document_tag (doc_id, tag) "
					+ "VALUES (?, ?)")
				.setParameter(1, id)
				.setParameter(2, tag.getName())
				.executeUpdate();
		}
		
		em.persist(entity.getFile());
		em.flush();
		
		em.createNativeQuery("INSERT INTO document_file (file_id, doc_id) "
				+ "VALUES (?, ?)")
			.setParameter(1, entity.getFile().getId())
			.setParameter(2, id)
			.executeUpdate();
		
		return entity;
	}
	
	// Inserts a document into the data-source
	@Override
	public void createDocument(Document document) {
		/*
		 * DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		 * def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		 * TransactionStatus status = transactionManager.getTransaction(def);
		 * 
		 * try { String SQL = "INSERT INTO document (author, title, description, " +
		 * "submit_date, task_id, task) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
		 * 
		 * Instant instant = Instant.now(); Timestamp ts = instant != null ?
		 * Timestamp.from(instant) : null;
		 * 
		 * document.setId(jdbcTemplate.query(SQL, new IdMapper(), document.getAuthor(),
		 * document.getTitle(), document.getDescription(), ts.toLocalDateTime(),
		 * document.getTaskId(), document.isTask()).get(0));
		 * 
		 * insertRelations(document, insertFile(document));
		 * 
		 * transactionManager.commit(status); } catch (Exception e) {
		 * System.out.println("Error in creating task record, rolling back");
		 * transactionManager.rollback(status);
		 * 
		 * throw e; }
		 */
	}

	// Inserts the file portion of the document
	private int insertFile(Document document) {
		/*
		 * DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		 * def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		 * TransactionStatus status = transactionManager.getTransaction(def); try {
		 * String SQL =
		 * "INSERT INTO file (file_name, file_type, file_size, author, data)" +
		 * " VALUES (?, ?, ?, ?, ?) RETURNING id"; File file = document.getFile(); int
		 * fileId = jdbcTemplate.query(SQL, new IdMapper(), file.getFileName(),
		 * file.getFileType(), file.getFileSize(), file.getAuthor(),
		 * file.getData()).get(0);
		 * 
		 * return fileId; } catch (Exception e) {
		 * System.out.println("Error in updating task file record, rolling back");
		 * transactionManager.rollback(status); throw e; }
		 */
		return 0;
	}

	// Inserts into the relation tables that, when combined, make up a document
	private void insertRelations(Document document, int fileId) {
		/*
		 * DefaultTransactionDefinition def = new DefaultTransactionDefinition();
		 * def.setIsolationLevel(TransactionDefinition.ISOLATION_REPEATABLE_READ);
		 * TransactionStatus status = transactionManager.getTransaction(def);
		 * 
		 * try { String SQL = "INSERT INTO document_program (doc_id, program_id, name) "
		 * + "VALUES (?, ?, ?)"; for (Program program : document.getPrograms()) {
		 * jdbcTemplate.update(SQL, document.getId(), program.getId(),
		 * program.getName()); }
		 * 
		 * SQL =
		 * "INSERT INTO document_outcome (doc_id, outcome_id, name) VALUES (?, ?, ?)";
		 * for (StudentOutcome outcome : document.getOutcomes()) {
		 * jdbcTemplate.update(SQL, document.getId(), outcome.getId(),
		 * outcome.getName()); }
		 * 
		 * SQL = "INSERT INTO document_tag (doc_id, tag) VALUES (?, ?)"; for(String tag
		 * : document.getTags()) { jdbcTemplate.update(SQL, document.getId(), tag); }
		 * 
		 * SQL = "INSERT INTO document_file (file_id, doc_id) VALUES (?, ?)";
		 * jdbcTemplate.update(SQL, fileId, document.getId());
		 * 
		 * } catch (Exception e) {
		 * System.out.println("Error in updating document record, rolling back");
		 * transactionManager.rollback(status); throw e; }
		 */
	}

	@Override
	public List<Document> getAll() {
		// TODO Auto-generated method stub
		return null;
	}

	// Given search criteria, build a query to get documents that satisfy them
	@Override
	public List<Document> getBySearch(DocumentSearch search) {
		QDocument document = QDocument.document;
		QDocumentProgram documentProgram = QDocumentProgram.documentProgram;
		QDocumentTag documentTag = QDocumentTag.documentTag;
		
		JPAQuery<Document> query = queryFactory.selectFrom(document)
			.innerJoin(document.programs, documentProgram)
			.on(document.id.eq(documentProgram.docId))
			.innerJoin(document.tags, documentTag)
			.on(document.id.eq(documentTag.docId));
		
		if(search.getToDate() != null) {
			Timestamp ts = new Timestamp(search.getToDate().getTime());
			query.where(document.submitDate.loe(ts));
		}
		
		if(search.getFromDate() != null) {
			Timestamp ts = new Timestamp(search.getFromDate().getTime());
			query.where(document.submitDate.goe(ts));
		}
		
		BooleanBuilder predicate = new BooleanBuilder();
		for(int progId : search.getPrograms()) {
			predicate.or(documentProgram.programId.eq(progId));
		}
		
		for(String tag : search.getTags()) {
			predicate.or(documentTag.name.eq(tag));
		}
		
		return query.where(predicate)
				.limit(search.getSearchCount())
				.distinct()
				.fetch();
	}
	
	@Override
	public Optional<Document> findById(Long id) {
		int fileId = (int) em.createNativeQuery("SELECT file_id FROM document_file"
				+ " WHERE doc_id=:id")
				.setParameter("id", id.intValue())
				.getSingleResult();
		File file = new File();
		file.setId(fileId);
		
		TypedQuery<Document> query = em.createQuery("SELECT d FROM Document d "
				+ "WHERE d.id=:id", Document.class)
				.setParameter("id", id.intValue());
			
		Document document = query.getSingleResult();
		document.setFile(file);
		
		return Optional.ofNullable(document);
	}


	@Override
	public List<Document> getRecentDocuments(int amount) {
		QDocument document = QDocument.document;
		List<Document> documents = queryFactory.selectFrom(document)
				.orderBy(document.submitDate.desc())
				.limit(amount)
				.fetch();
		
		return documents;
	}

	@Override
	public List<Document> getDocsForTask(int taskId) {
		/*
		 * ArrayList<Document> docs = new ArrayList<>(); try { String SQL =
		 * "SELECT * FROM document WHERE task_id = ?"; docs = (ArrayList<Document>)
		 * jdbcTemplate.query(SQL, new MediumDocMapper(), taskId);
		 * 
		 * return docs; } catch (EmptyResultDataAccessException e) { return null; }
		 */
		return null;
	}

	@Override
	public List<String> getAllTags() {
		Query query = em.createQuery("SELECT name FROM Tag", String.class);
		List<String> tags = (List<String>) query.getResultList();
		
		return tags;
	}

	// A simple mapper that gets only the document info stored in the document table
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
	
	@Override
	public <S extends Document> Iterable<S> saveAll(Iterable<S> entities) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean existsById(Long id) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public Iterable<Document> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Iterable<Document> findAllById(Iterable<Long> ids) {
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
	public void delete(Document entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAllById(Iterable<? extends Long> ids) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll(Iterable<? extends Document> entities) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deleteAll() {
		// TODO Auto-generated method stub
		
	}
}
