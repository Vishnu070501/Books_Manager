package com.VishnuKurup.books_manager.dbUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.*;

import com.VishnuKurup.books_manager.containers.*;


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
				myRs = myStmt.executeQuery("SELECT * FROM public.books\r\n"
						+ "ORDER BY title ASC ");
				
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
				
				while (myRs.next()) {
					java.util.Date publicationDate = sdf.parse(myRs.getString("publication_date"));
					Book myBook = new Book(myRs.getString("title"),myRs.getString("author"),
							myRs.getString("subject"),publicationDate,myRs.getInt("noOfCopiesAvailable"));
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
				myPStmt = myConn.prepareStatement("INSERT INTO public.books(\r\n"
						+ "	title, author, subject, \"noOfCopiesAvailable\", publication_date)\r\n"
						+ "	VALUES (?, ?, ?, ?, ?)");
				
				//fixing the parameters 
				myPStmt.setString(1, newBook.getTitle());
				myPStmt.setString(2, newBook.getAuthor());
				myPStmt.setString(3, newBook.getSubject());
				myPStmt.setInt(4, newBook.getCopiesAvail());
				myPStmt.setDate(5,new java.sql.Date(newBook.getPublication_date().getTime()));//casted to sql date
				
				//executing the the query
				myPStmt.execute();

				
			}
			catch (Exception e) {
				e.printStackTrace();
			}
			
			finally {
				close(myConn,null,null,myPStmt);
			}
			
		}


		public Book get_book_whose_title(String title) {
			Book TheBook = null;
			try {
				//create the connection
				Class.forName("org.postgresql.Driver");
		         myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
		            "postgres", "webstudent");
		        
		         //creating the statement
		         //query to get the details of a student based on the condition
		         myStmt= myConn.createStatement();
		         myRs=myStmt.executeQuery("SELECT * FROM public.books\r\n"
		         		+ "ORDER BY title ASC ");//got the student
		         
		         
		         
		         //creating the student variable to pass back to controller servlet
		         while(myRs.next()) {
		        	 
		        	 if(myRs.getString("title").equals(title)) {
			        	 TheBook =new Book(myRs.getString("title"),myRs.getString("author"),
									myRs.getString("subject"),myRs.getDate("publication_date"),myRs.getInt("noOfCopiesAvailable"));
			        	 break;
		        	 }
		         }
			}
			
			catch (Exception e) {
				e.printStackTrace();
			}
			finally {
				close(myConn,myStmt,myRs,myPStmt);
			}
			return TheBook;
		}


		public void updateBook(Book myBook,String oldtitle) {


			try {
				//create connection
				Class.forName("org.postgresql.Driver");
		         myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
		            "postgres", "webstudent");
		         
		         //creating the statement 
		         String upQr="UPDATE public.books SET \"title\"=?, \"author\"=?, \"subject\"=?, \"publication_date\"=?, \"noOfCopiesAvailable\"=? WHERE \"title\"=? ;";
		         
		         
		         //preparing the statement 
		         myPStmt = myConn.prepareStatement(upQr);
//		         java.sql.Date myDate=new java.sql.Date(myBook.getPublication_date().getTime());
		         
		         //setting up the parameters
		         myPStmt.setString(1, myBook.getTitle());
		         myPStmt.setString(2, myBook.getAuthor());
		         myPStmt.setString(3, myBook.getSubject());
		         myPStmt.setDate(4, new java.sql.Date(myBook.getPublication_date().getTime()));//casted to sql date
		         myPStmt.setInt(5, myBook.getCopiesAvail());
		         myPStmt.setString(6, oldtitle);
//		         System.out.println(new java.sql.Date(myBook.getPublication_date().getTime()));
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


		public ArrayList<Book> searchBook(String searchText, String[] searchBy) {

			ArrayList<Book> searchedBooks = new ArrayList<>();
			
			try {
				
				//create the connection
				Class.forName("org.postgresql.Driver");
		         myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
		            "postgres", "webstudent");
		        // creating the queries with different collumn names
		         ArrayList<String> sql = new ArrayList<String>();
		         
		         String[] searchBy2 = {"title","author","subject"};
		         if(null != searchBy) {
			         for (int i = 0 ;i< searchBy.length; i++) {
			        	 sql.add("SELECT * FROM public.\"books\" where \""+searchBy[i]+"\" ilike ?;");
			         }
		         }else {
		        	 for (int i = 0 ;i< searchBy2.length; i++) {
			        	 sql.add("SELECT * FROM public.\"books\" where \""+searchBy2[i]+"\" ilike ?;");
			         }
		         }

		         for(int i = 0;i<sql.size();i++) {
		        	 myPStmt = myConn.prepareStatement(sql.get(i));
		        	// myPStmt.setString(1, temp);
		        	 myPStmt.setString(1, '%'+searchText+'%');
		        	 myRs = myPStmt.executeQuery();
//		        	 System.out.println(myPStmt);
		        	 while(myRs.next()) {
		        		 boolean add =true;
		        		 SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		        		 java.util.Date publicationDate = sdf.parse(myRs.getString("publication_date"));
		        		 Book myBook = new Book(myRs.getString(1),myRs.getString(2),
		        				 myRs.getString(3),publicationDate,myRs.getInt(4));
		        		 
		        		 for (Book temp : searchedBooks) {
		        			 if(temp.getTitle().equals(myBook.getTitle())) {
		        				 add=false;
		        				 break;
		        			 }
		        		 }
		        		 if(add) {
	        				 searchedBooks.add(myBook);
	        			 } 
		        	 }
		         }
		         }catch(Exception e) {

				e.printStackTrace();
			}
			finally {
				close(myConn,myStmt,myRs,myPStmt);				
			}
			
			return searchedBooks;			
			}
		
		
}

