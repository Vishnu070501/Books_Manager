package com.VishnuKurup.books_manager;

public class Book {
	private String title;
	private String author;
	private String subject;
	private String publication_date;
	
	//constructor
	public Book(String title, String author, String subject, String publication_date) {
		super();
		this.title = title;
		this.author = author;
		this.subject = subject;
		this.publication_date = publication_date;
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

	public String getPublication_date() {
		return publication_date;
	}

	public void setPublication_date(String publication_date) {
		this.publication_date = publication_date;
	}

	
	//the to String method
	@Override
	public String toString() {
		return "Book [title=" + title + ", author=" + author + ", subject=" + subject + ", publication_date="
				+ publication_date + "]";
	}
	
	
	
}
