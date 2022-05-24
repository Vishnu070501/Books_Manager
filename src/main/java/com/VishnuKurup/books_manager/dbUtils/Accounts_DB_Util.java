package com.VishnuKurup.books_manager.dbUtils;

import java.sql.*;
import java.util.*;

import com.VishnuKurup.books_manager.containers.*;

public class Accounts_DB_Util {
	
	//initializing all the things for a connection to the acct DB
	Connection myConn = null;
	Statement myStmt = null;
	ResultSet myRs = null;
	PreparedStatement myPStmt = null;
	


	
	//function to discard all the jdbc objects to prevent a memory leak
	private void close(Connection c2, Statement myStmt2, ResultSet myRs2,PreparedStatement myPSt2) {
		try {
			if(c2 != null)
				c2.close(); //does'nt delete the connection just puts it back in the pool
			if(myStmt2 != null) 
				myStmt2.close();
			if(myRs2 != null)
				myRs2.close();
			if(myPSt2 != null)
				myPSt2.close();
		}
		
		catch(Exception e) {
			e.printStackTrace();
			
		}
	}




	public boolean logCheck(Account theacct) {
			
			boolean valid_acct = false;
				try {
					//creating connection
					Class.forName("org.postgresql.Driver");
					myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
					           "postgres", "webstudent");
					
					//preparing a statement
					myStmt = myConn.createStatement();
					//getting the result set
					myRs = myStmt.executeQuery("SELECT \"username\", \"userPassword\", \"userType\"\r\n"
							+ "	FROM public.accounts;");
							
					while (myRs.next()) {
					
						if (myRs.getString("username").equals(theacct.getUsername())
								&& myRs.getString("userPassword").equals(theacct.getPassword())) {
								valid_acct = true;
								break;
						}
						
					}
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					close(myConn,myStmt,myRs,null);
				}
				return valid_acct;
	}




	public ArrayList<Account> getUsers() {
		ArrayList<Account> myUsers = new ArrayList<>();
		try {
			//creating connection
			Class.forName("org.postgresql.Driver");
			myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
			           "postgres", "webstudent");
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("SELECT * FROM public.accounts\r\n"
					+ "ORDER BY username ASC ");
			while(myRs.next()) {
				if(myRs.getString("userType").equals("user"))
					myUsers.add(new Account(myRs.getString("username"),null,null,myRs.getString("name"),myRs.getString("qualification"),
							myRs.getString("email")));
			}
		}
	catch(Exception e) {
		e.printStackTrace();
	}
		finally {
			close(myConn,myStmt,myRs,myPStmt);
		}
		
		return myUsers;
	
	}




	public void addUser(Account account) {
		try {
			
			//create the connection
			Class.forName("org.postgresql.Driver");
	         myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
	            "postgres", "webstudent");
	         
	         //creating the statement 
	         myPStmt = myConn.prepareStatement("INSERT INTO public.accounts(\r\n"
	         		+ "	\"username\", \"userPassword\", \"userType\", \"name\", \"qualification\", \"email\")\r\n"
	         		+ "	VALUES (?, ?, ?, ?, ?, ?);");
	         //setting parameters 
	         myPStmt.setString(1, account.getUsername());
	         myPStmt.setString(2, account.getPassword());
	         myPStmt.setString(3, account.getAccountType());
	         myPStmt.setString(4, account.getName());
	         myPStmt.setString(5, account.getQualification());
	         myPStmt.setString(6, account.getEmail());

	         //executing the statement
	         myPStmt.execute();
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			close(myConn,myStmt,myRs,myPStmt);				
			}
		
	}





	
	//to load into the update form
	public Account getUserWhoseUsername(String userName) {

		Account TheAccount = null;
		try {
			//create the connection
			Class.forName("org.postgresql.Driver");
			myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
			           "postgres", "webstudent");
			myStmt = myConn.createStatement();
			myRs = myStmt.executeQuery("SELECT * FROM public.accounts\r\n"
					+ "ORDER BY username ASC ");
	         
	         
	         //creating the UserAccount variable to pass back to controller servlet
	     
	         while(myRs.next()) {
	        	 if(myRs.getString("username").equals(userName)) {
	        		 TheAccount = new Account(myRs.getString("username"),
								myRs.getString("userpassword"),myRs.getString("userType"),myRs.getString("name"),
								myRs.getString("qualification"),myRs.getString("email"));
	        	 }
		         
	         }
	      
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		finally {
			//cleans all the jdbc objects 
        	 close(myConn,null,myRs,myPStmt);
		}
		return TheAccount;
	}
	
	//updating a user with the old username
	public void updateUser(String oldUserName, Account theAccount,String myself) {


		try {
			
			
			//create the connection
			Class.forName("org.postgresql.Driver");
	         myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
	            "postgres", "webstudent");
	         
	         //someone updating themselves
	        if (myself.equals("true")) {
	        	//creating the statement
		         //query to get the details of a user based on the condition
		         myPStmt= myConn.prepareStatement("UPDATE public.accounts\r\n"
		         		+ "	SET \"username\"=?, \"userPassword\"=?, \"name\"=?, \"qualification\"=?, \"email\"=?\r\n"
		         		+ "	WHERE \"username\"=?;");
		         
		         //setting the parameters for the prepared statement
		         myPStmt.setString(1, theAccount.getUsername());
		         myPStmt.setString(2, theAccount.getPassword());
		         myPStmt.setString(3, theAccount.getName());
		         myPStmt.setString(4, theAccount.getQualification());
		         myPStmt.setString(5, theAccount.getEmail());
		         myPStmt.setString(6, oldUserName);
		        
		         myPStmt.execute();//got the user
	        
	        }
	        else {
	        	
	        	//creating the statement
		         //query to get the details of a user based on the condition
		         myPStmt= myConn.prepareStatement("UPDATE public.accounts\r\n"
		         		+ "	SET \"username\"=?, \"name\"=?, \"qualification\"=?, \"email\"=?\r\n"
		         		+ "	WHERE \"username\"=?;");
		         
		         //setting the parameters for the prepared statement
		         myPStmt.setString(1, theAccount.getUsername());
		         myPStmt.setString(2, theAccount.getName());
		         myPStmt.setString(3, theAccount.getQualification());
		         myPStmt.setString(4, theAccount.getEmail());
		         myPStmt.setString(5, oldUserName);
		        
		         myPStmt.execute();//got the user
	        }
			
		}
		catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			close(myConn,myStmt,myRs,myPStmt);
		}
	}
	
	public void deleteUser(String username) {


		try {
			
			//create the connection
			Class.forName("org.postgresql.Driver");
	         myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
	            "postgres", "webstudent");
	         
	         //creating the statement 
	         myPStmt = myConn.prepareStatement("DELETE FROM public.accounts\r\n"
	         		+ "	WHERE \"username\"=?");
	         //setting parameters 
	         myPStmt.setString(1, username);
	         myPStmt.execute();
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			close(myConn,myStmt,myRs,myPStmt);				
			}
	}




	public Set<Account> searchUsers(String searchText, String[] searchBy) {
		Set<Account> searchedUsers = new HashSet<>();
		try {
			
			//create the connection
			Class.forName("org.postgresql.Driver");
	         myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
	            "postgres", "webstudent");
	        // creating the queries with different collumn names
	         ArrayList<String> sql = new ArrayList<String>();
	         
	         String[] searchBy2 = {"username","name","email","qualification"};
	         if(null != searchBy) {
		         for (int i = 0 ;i< searchBy.length; i++) {
		        	 sql.add("SELECT *\r\n"
		        	 		+ "	FROM public.accounts\r\n"
		        	 		+ "	where \"userType\"='user' and \""+searchBy[i]+"\" ilike ?;");
		         }
	         }else {
	        	 for (int i = 0 ;i< searchBy2.length; i++) {
		        	 sql.add("SELECT *\r\n"
		        	 		+ "	FROM public.accounts\r\n"
		        	 		+ "	where \"userType\"='user' and \""+searchBy2[i]+"\" ilike ?;");
		         }
	         }

	         for(int i = 0;i<sql.size();i++) {
	        	 myPStmt = myConn.prepareStatement(sql.get(i));
	        	// myPStmt.setString(1, temp);
	        	 myPStmt.setString(1, '%'+searchText+'%');
	        	 myRs = myPStmt.executeQuery();
//	        	 System.out.println(myPStmt);
	        	 while(myRs.next()) {
	        		 boolean add = true;
	        		 Account myAccount = new Account(myRs.getString(1),myRs.getString(2),
	        				 myRs.getString(3),myRs.getString(4),myRs.getString(5),
	        				 myRs.getString(6));
	        		 for (Account temp : searchedUsers) {
	        			 if(temp.getUsername().equals(myAccount.getUsername())) {
	        				 add=false;
	        				 break;
	        			 }
	        		 }
	        		 if(add) {
        				 searchedUsers.add(myAccount);
	        		 }
	        			 
	        	 }
	        	 
	         }
		}catch(Exception e) {
			e.printStackTrace();
		}
		finally {
			close(myConn,myStmt,myRs,myPStmt);				
		}
		
		return searchedUsers;
	}

}
