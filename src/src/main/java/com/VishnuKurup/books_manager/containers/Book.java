package com.VishnuKurup.books_manager.containers;

import java.util.Date;

public class Book {
	private String title;
	private String author;
	private String subject;
	private java.util.Date publication_date;
	private int copiesAvail;
	

	public int getCopiesAvail() {
		return copiesAvail;
	}

	public void setCopiesAvail(int copiesAvail) {
		this.copiesAvail = copiesAvail;
	}

	public Book(String title, String author, String subject, Date publication_date, int copiesAvail) {
		super();
		this.title = title;
		this.author = author;
		this.subject = subject;
		this.publication_date = publication_date;
		this.copiesAvail = copiesAvail;
	}

	//getters and setters 
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public Date getPublication_date() {
		return publication_date;
	}

	public void setPublication_date(Date publication_date) {
		this.publication_date = publication_date;
	}

	
	@Override
	public String toString() {
		return "Book [title=" + title + ", author=" + author + ", subject=" + subject + ", publication_date="
				+ publication_date + ", copiesAvail=" + copiesAvail + "]";
	}
	
	
	
}
