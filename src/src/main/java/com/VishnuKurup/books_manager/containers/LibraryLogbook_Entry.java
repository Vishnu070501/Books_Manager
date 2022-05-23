package com.VishnuKurup.books_manager.containers;

import java.util.*;

public class LibraryLogbook_Entry {
	
	private String username;
	private String title;
	private String action;
	private Date dueDate;
	private int renewalCount;
	private String reservedFor;
	private String reservedFrom;
	public LibraryLogbook_Entry(String username, String title, String action, Date dueDate) {
		super();
		this.username = username;
		this.title = title;
		this.action = action;
		this.dueDate = dueDate;
	}
	public LibraryLogbook_Entry(String username, String title, String action, Date dueDate, int renewalCount) {
		super();
		this.username = username;
		this.title = title;
		this.action = action;
		this.dueDate = dueDate;
		this.renewalCount = renewalCount;
	}
	public int getRenewalCount() {
		return renewalCount;
	}
	public String getReservedFor() {
		return reservedFor;
	}
	public void setReservedFor(String reservedFor) {
		this.reservedFor = reservedFor;
	}
	public String getReservedFrom() {
		return reservedFrom;
	}
	public void setReservedFrom(String reservedFrom) {
		this.reservedFrom = reservedFrom;
	}
	public LibraryLogbook_Entry(String username, String title, String action, Date dueDate, int renewalCount,
			String reservedFor, String reservedFrom) {
		super();
		this.username = username;
		this.title = title;
		this.action = action;
		this.dueDate = dueDate;
		this.renewalCount = renewalCount;
		this.reservedFor = reservedFor;
		this.reservedFrom = reservedFrom;
	}
	public void setRenewalCount(int renewalCount) {
		this.renewalCount = renewalCount;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public Date getDueDate() {
		return dueDate;
	}
	public void setDueDate(Date dueDate) {
		this.dueDate = dueDate;
	}
	@Override
	public String toString() {
		return "LibraryLogbook_Entry [username=" + username + ", title=" + title + ", action=" + action + ", dueDate="
				+ dueDate + ", renewalCount=" + renewalCount + ", reservedFor=" + reservedFor + ", reservedFrom="
				+ reservedFrom + "]";
	}
	
	
}
