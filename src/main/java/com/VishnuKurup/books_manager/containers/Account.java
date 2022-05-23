package com.VishnuKurup.books_manager.containers;

public class Account {
	
	//class helping us store user credentials
	private String username;
	private String password;
	private String accountType;
	private String name;
	private String qualification;
	private String email;
	
	

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getQualification() {
		return qualification;
	}

	public void setQualification(String qualification) {
		this.qualification = qualification;
	}

	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Account(String username, String password, String accountType, String name, String qualification,
			String email) {
		super();
		this.username = username;
		this.password = password;
		this.accountType = accountType;
		this.name = name;
		this.qualification = qualification;
		this.email = email;
	}

	public String getAccountType() {
		return accountType;
	}

	public void setAccountType(String accountType) {
		this.accountType = accountType;
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

	@Override
	public String toString() {
		return "Account [username=" + username + ", password=" + password + ", accountType=" + accountType + ", name="
				+ name + ", qualification=" + qualification + ", email=" + email + "]";
	}
	
	

}
