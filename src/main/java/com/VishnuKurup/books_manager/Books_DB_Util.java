package com.VishnuKurup.books_manager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;


public class Books_DB_Util {
	//initializing all the things for a connection to the acct DB
		Connection myConn = null;
		Statement myStmt = null;
		ResultSet myRs = null;
		PreparedStatement myPStmt = null;
		
		//function to give the array list of books present in the books database 
		public ArrayList<Book> ListBooks() {

			ArrayList<Book> myBooks = new ArrayList<>();
			try {
				
				//creating the connection
				Class.forName("org.postgresql.Driver");
				myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
				           "postgres", "webstudent");
				
				//creating statement 
				myStmt = myConn.createStatement();
				
				//getting the result set 
				myRs = myStmt.executeQuery("select * from books");
				
				while (myRs.next()) {
					
					Book myBook = new Book(myRs.getString("title"),myRs.getString("author"),
							myRs.getString("subject"),myRs.getString("publication_date"));
					myBooks.add(myBook);
				}
				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				close(myConn,myStmt,myRs,null);
			}
			return myBooks;
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


		public void add_book(Book newBook) {
			
			try {
				//creating the connection
				Class.forName("org.postgresql.Driver");
				myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
				           "postgres", "webstudent");
				
				//preparing a statement 
				myPStmt = myConn.prepareStatement("INSERT INTO public.books("
						+ "	title, author, subject, publication_date)"
						+ "	VALUES (?, ?, ?, ?);");
				
				//fixing the parameters 
				myPStmt.setString(1, newBook.getTitle());
				myPStmt.setString(2, newBook.getAuthor());
				myPStmt.setString(3, newBook.getSubject());
				myPStmt.setString(4, newBook.getPublication_date());
				
				//executing the the query
				myPStmt.execute();

				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			finally {
				close(myConn,null,null,myPStmt);
			}
			// TODO Auto-generated method stub
			
		}


		public Book get_book_whose_title(String title) {
			try {
				//create the connection
				Class.forName("org.postgresql.Driver");
		         myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
		            "postgres", "webstudent");
		        
		         //creating the statement
		         String loadQr="select * from books where title = ?";//query to get the details of a student based on the condition
		         myPStmt= myConn.prepareStatement(loadQr);
		         myPStmt.setString(1, title);
		         myRs=myPStmt.executeQuery();//got the student
		         
		         
		         //creating the student variable to pass back to controller servlet
		         if(myRs.next()) {
		        	 Book TheBook = new Book(myRs.getString("title"),myRs.getString("author"),
			        		 myRs.getString("subject"),myRs.getString("publication_date"));
		        	//cleans all the jdbc objects 
		        	 close(myConn,null,myRs,myPStmt);
			         
		        	 //return the book
			         return TheBook;
		         }
		         
		         else {
		        	 throw new Exception("Book with the title "+title+" not Found");
		         }
			}
			
			catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}


		public void updateBook(Book myBook,String oldtitle) {


			try {
				//create connection
				Class.forName("org.postgresql.Driver");
		         myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
		            "postgres", "webstudent");
		         
		         //creating the statement 
		         String upQr="UPDATE public.books SET title=?, author=?, subject=?, publication_date=? WHERE title=? ;";
		         
		         //preparing the statement 
		         myPStmt = myConn.prepareStatement(upQr);
		         
		         //setting up the parameters
		         myPStmt.setString(1, myBook.getTitle());
		         myPStmt.setString(2, myBook.getAuthor());
		         myPStmt.setString(3, myBook.getSubject());
		         myPStmt.setString(4, myBook.getPublication_date());
		         myPStmt.setString(5, oldtitle);
		         
		         //executing the statement
		         myPStmt.execute();
		         
			}
			catch(Exception e) {
				e.printStackTrace();
			}
			finally {
				close(myConn,null,null,myPStmt);
			}
			
		}


		public void deleteBook(String theTitle) {


			try {
				//create connection
				Class.forName("org.postgresql.Driver");
		         myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
		            "postgres", "webstudent");
		         
		         //creating a statement 
		         String delQr="delete from books where title =?";//query to delete
		         
		         //prepare statement
		         myPStmt = myConn.prepareStatement(delQr);
		         
		         //setting parameters 
		         myPStmt.setString(1, theTitle);
		        		 
		        //execute query
		        myPStmt.execute(); 
		        		 
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				close(myConn,null,null,myPStmt);
			}
		}


		public ArrayList<Book> searchBooks(String searchtext, String searchby) {
			
			//creating the array list to stored the searched books
			ArrayList<Book> myBooks = new ArrayList<>();
			
			try {
				//create connection
				Class.forName("org.postgresql.Driver");
		         myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
		            "postgres", "webstudent");
		         
		         //creating a statement 
		         String serQr="select * from books";//query to search
		         
		         //prepare statement
		         myStmt = myConn.createStatement();
		         		 
		        //execute query
		        myRs = myStmt.executeQuery(serQr);
		        		        
		        //getting the data from the result set
		        while(myRs.next()) {
		        	if(myRs.getString(searchby).equals(searchtext)) {
		        		//will only store in array list if the searched quantity matches 
		        		myBooks.add(new Book(myRs.getString("title"),myRs.getString("author"),
			        			myRs.getString("subject"),myRs.getString("publication_date")));
		        	}
		        }
		        		 
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				close(myConn,myStmt,myRs,null);
			}
			return myBooks;
		}
		

}
