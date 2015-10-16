package com.aggrepoint.doc.demo.comment;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Id;

import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 留言实体对象
 * 
 * @author jiangmingyang
 */
@Entity
public class Comment {
	static int nextId = 0;

	@Id
	private int id;

	@NotEmpty
	@Email
	private String email;

	@NotEmpty
	private String content;

	@DateTimeFormat(pattern = "yyyy年M月d日 HH:mm:ss")
	private Date createTime = new Date();

	/**
	 * 分配一个新的ID
	 */
	public Comment assignId() {
		id = ++nextId;
		return this;
	}

	public void copy(Comment comment) {
		this.email = comment.email;
		this.content = comment.content;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public Date getCreateTime() {
		return createTime;
	}

	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
}
