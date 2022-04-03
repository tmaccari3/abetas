package com.maccari.abet.domain.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Transient;

/*
 * File.java 
 * Author: Thomas Maccari
 * 
 * Description: The domain object that represents a tuple from the file table.
 * 
 */

@Entity
@Table(name = "file")
public class File implements Serializable {
	@Transient
	private static final long serialVersionUID = 129348938L;
	
	@Id
    @SequenceGenerator(name = "file_id_seq",
                       sequenceName = "file_id_seq",
                       allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                    generator = "file_id_seq")
	private int id;
	
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "author")
	private String author;
	
	@Column(name = "file_type")
	private String fileType;
	
	@Column(name = "file_size")
	private long fileSize;
	
	@Column(name = "data")
	private byte[] data;
	
	public File() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public long getFileSize() {
		return fileSize;
	}

	public void setFileSize(long fileSize) {
		this.fileSize = fileSize;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
