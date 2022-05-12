package com.VishnuKurup.books_manager;

import java.sql.*;

public class Accounts_DB_Util {
	
	//initializing all the things for a connection to the acct DB
	Connection myConn = null;
	Statement myStmt = null;
	ResultSet myRs = null;
	PreparedStatement myPStmt = null;
	

	public boolean valid_user_check(Account theacct) {
			
		boolean valid_acct = false;
			try {
				//creating connection
				Class.forName("org.postgresql.Driver");
				myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
				           "postgres", "webstudent");
				
				//preparing a statement
				myStmt = myConn.createStatement();
				//getting the result set
				myRs = myStmt.executeQuery("SELECT * FROM public.accounts\r\n"
						+ "ORDER BY \"Username\" ASC ");
						
				while (myRs.next()) {
				
					if (myRs.getString("Username").equals(theacct.getUsername())
							&& myRs.getString("Pword").equals(theacct.getPassword())) {
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
	
}
