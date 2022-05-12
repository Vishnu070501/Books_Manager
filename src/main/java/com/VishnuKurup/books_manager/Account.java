package com.VishnuKurup.books_manager;

public class Account {
	
	//class helping us store user credentials
	private String username;
	private String password;
	
	//constructor
	public Account(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	//getters and setters ********
	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}
	//********

	//returns the creds as a string
	@Override
	public String toString() {
		return "Account [username=" + username + ", password=" + password + "]";
	}
	
	

}
