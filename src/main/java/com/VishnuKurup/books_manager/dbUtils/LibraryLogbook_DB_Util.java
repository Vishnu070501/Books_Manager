package com.VishnuKurup.books_manager.dbUtils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.VishnuKurup.books_manager.containers.Book;
import com.VishnuKurup.books_manager.containers.LibraryLogbook_Entry;

public class LibraryLogbook_DB_Util {
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

			public int checkMyTotalBooks(String username) {
				int totalBooks = 0;
				try {
				//creating the connection
				Class.forName("org.postgresql.Driver");
				myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
				           "postgres", "webstudent");
				myPStmt = myConn.prepareStatement("SELECT *\r\n"
						+ "	FROM public.\"libraryLogbook\"\r\n"
						+ "	where \"username\"=?;");
						
				myPStmt.setString(1, username);
				myRs = myPStmt.executeQuery();
				
				while(myRs.next()) {
					totalBooks+=1;
				}
				}catch(Exception e) {
					e.printStackTrace();
				}
				finally {
					close(myConn,myStmt,myRs,myPStmt);
				}
				// TODO Auto-generated method stub
				return totalBooks;
			}

			//enters an entry to the logBook ussually checkout
			public void entry(LibraryLogbook_Entry libraryLogbook_Entry) {
				
				try {
					//creating the connection
					Class.forName("org.postgresql.Driver");
					myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
					           "postgres", "webstudent");
					myPStmt = myConn.prepareStatement("INSERT INTO public.\"libraryLogbook\"(\r\n"
							+ "	username, title, \"action\", \"dueDate\", \"renewalCount\", \"reservedFor\", \"reservedFrom\")\r\n"
							+ "	VALUES (?, ?, ?, ?, ?, ?, ?);");
					myPStmt.setString(1, libraryLogbook_Entry.getUsername());
					myPStmt.setString(2, libraryLogbook_Entry.getTitle());
					myPStmt.setString(3, libraryLogbook_Entry.getAction());
					myPStmt.setDate(4, new java.sql.Date(libraryLogbook_Entry.getDueDate().getTime()));
					myPStmt.setInt(5, 0);
					myPStmt.setString(6, libraryLogbook_Entry.getReservedFor());
					myPStmt.setString(7, libraryLogbook_Entry.getReservedFrom());
					
					myPStmt.execute();
				}catch(Exception e) {
					e.printStackTrace();
				}
				finally {
					close(myConn,myStmt,myRs,myPStmt);
				}
				
			}

			public boolean checkForBook(String username, String title) {
				boolean retval =true;
				try {
					//creating the connection
					Class.forName("org.postgresql.Driver");
					myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
					           "postgres", "webstudent");
					myStmt = myConn.createStatement();
					
					myRs = myStmt.executeQuery("SELECT * FROM public.\"libraryLogbook\"\r\n"
							+ "ORDER BY username ASC, title ASC ");
					while(myRs.next()) {
						//returns false for a duplicate entry
						if (myRs.getString("username").equals(username) && myRs.getString("title").equals(title)) {
							retval=false;
							break;
						}
							
					}
				}catch (Exception e) {
					e.printStackTrace();
				}
				finally {
					close(myConn,myStmt,myRs,myPStmt);
				}
				//else returns true
				return retval;
			}

			public ArrayList<LibraryLogbook_Entry> getEntriesofUsername(String username) {

				ArrayList<LibraryLogbook_Entry> myEntries = new ArrayList<>();
				try {
					
					//creating the connection
					Class.forName("org.postgresql.Driver");
					myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
					           "postgres", "webstudent");
					myPStmt = myConn.prepareStatement("SELECT *\r\n"
							+ "	FROM public.\"libraryLogbook\"\r\n"
							+ "	where \"username\"=?;");
					myPStmt.setString(1, username);
					
					myRs = myPStmt.executeQuery();
					
					
					while(myRs.next()) {
						myEntries.add(new LibraryLogbook_Entry(myRs.getString(1),myRs.getString(2),
								myRs.getString(3),myRs.getDate(4),myRs.getInt(5),myRs.getString(6),myRs.getString(7)));
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				finally {
					close(myConn,myStmt,myRs,myPStmt);
				}
				return myEntries;
			}

			public void deleteEntry(String username, String title) {


				try {
					//creating the connection
					Class.forName("org.postgresql.Driver");
					myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
					           "postgres", "webstudent");
					myPStmt = myConn.prepareStatement("DELETE FROM public.\"libraryLogbook\"\r\n"
							+ "	WHERE \"username\"=? and \"title\"=?");
					myPStmt.setString(1, username);
					myPStmt.setString(2, title);
					myPStmt.execute();
					
				}catch(Exception e) {
					e.printStackTrace();
				}
				finally {
					close(myConn,myStmt,myRs,myPStmt);				}	
			}

			public LibraryLogbook_Entry getEntry(String username, String title) {
				LibraryLogbook_Entry myEntry = null;
				try {
					//creating the connection
					Class.forName("org.postgresql.Driver");
					myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
					           "postgres", "webstudent");
					myPStmt = myConn.prepareStatement("SELECT *"
							+ "	FROM public.\"libraryLogbook\" where username = ? and title = ?");
					myPStmt.setString(1, username);
					myPStmt.setString(2, title);
					
					myRs = myPStmt.executeQuery();
					
					while(myRs.next()) {
						myEntry = new LibraryLogbook_Entry(myRs.getString(1),myRs.getString(2),
								myRs.getString(3),myRs.getDate(4),myRs.getInt(5),myRs.getString(6),myRs.getString(7));
					}

				}catch(Exception e) {
					e.printStackTrace();
				}
				finally {
					close(myConn,myStmt,myRs,myPStmt);				
				}
				return myEntry;
			}

			public void updateEntry(String username, String title, LibraryLogbook_Entry theEntry) {
				try {
					
					//creating the connection
					Class.forName("org.postgresql.Driver");
					myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
					           "postgres", "webstudent");
					
					myPStmt = myConn.prepareStatement("UPDATE public.\"libraryLogbook\"\r\n"
							+ "	SET username=?, title=?, action=?, \"dueDate\"=?, \"renewalCount\"=?, \"reservedFor\"=?, \"reservedFrom\"=?\r\n"
							+ "	WHERE username=? and title=?");
					
					myPStmt.setString(1, theEntry.getUsername());
					myPStmt.setString(2, theEntry.getTitle());
					myPStmt.setString(3, theEntry.getAction());
					myPStmt.setDate(4, new java.sql.Date(theEntry.getDueDate().getTime()));
					myPStmt.setInt(5, theEntry.getRenewalCount());
					myPStmt.setString(6, theEntry.getReservedFor());
					myPStmt.setString(7, theEntry.getReservedFrom());
					myPStmt.setString(8, username);
					myPStmt.setString(9, title);
					
					myPStmt.execute();
					
				}catch(Exception e){
					e.printStackTrace();
				}
				finally {
					close(myConn,myStmt,myRs,myPStmt);				

				}
				
			}

			public LibraryLogbook_Entry getTheBookEarliestAvail(String title) {

				java.util.Date earliest = new java.util.Date();
				LibraryLogbook_Entry theEntry = null;
				try {
					
					
					//creating the connection
					Class.forName("org.postgresql.Driver");
					myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
					           "postgres", "webstudent");
					myPStmt = myConn.prepareStatement("SELECT * \r\n"
							+ "	FROM public.\"libraryLogbook\"\r\n"
							+ "	where title=? and \"action\" = ?");
					myPStmt.setString(1, title);
					myPStmt.setString(2, "checkout");
					
					myRs = myPStmt.executeQuery();
					
					int LoopCount=0;
					
					while(myRs.next()) {
						
						if(myRs.getString("reservedFor").equals("NA")) {
							if (LoopCount==0) {
					
								earliest = myRs.getDate("dueDate");
								theEntry = new LibraryLogbook_Entry(myRs.getString(1),myRs.getString(2),myRs.getString(3),myRs.getDate(4),
										myRs.getInt(5),myRs.getString(6),myRs.getString(7));
								
							}
							else if(earliest.compareTo(myRs.getDate("dueDate"))>0) {
								earliest = myRs.getDate("dueDate");
								theEntry = new LibraryLogbook_Entry(myRs.getString(1),myRs.getString(2),myRs.getString(3),myRs.getDate(4),
										myRs.getInt(5),myRs.getString(6),myRs.getString(7));
							}
							LoopCount++;
						}
					}
					
					
				}catch(Exception e) {
					e.printStackTrace();
				}
				finally {
					close(myConn,myStmt,myRs,myPStmt);	
				}
				return theEntry;
			}

			public boolean check_for_unreserve(String title) {
				boolean retval=false;
				try {
					//creating the connection
					Class.forName("org.postgresql.Driver");
					myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
					           "postgres", "webstudent");
					myPStmt = myConn.prepareStatement("SELECT *\r\n"
							+ "	FROM public.\"libraryLogbook\"\r\n"
							+ "	where title = ? and \"action\" = 'checkout'");
					myPStmt.setString(1, title);
					myRs = myPStmt.executeQuery();
					while(myRs.next()) {
						if(myRs.getString("reservedFor").equals("NA")) {
							retval = true;
							break;
						}
					}
				}catch(Exception e) {
					e.printStackTrace();
				}
				finally {
					close(myConn,myStmt,myRs,myPStmt);	
				}
				return retval;
			}

			public Set<LibraryLogbook_Entry> searchEntry(String searchText, String[] searchBy) {

				Set<LibraryLogbook_Entry> searchedEntries = new HashSet<>();
				
				try {
					
					//create the connection
					Class.forName("org.postgresql.Driver");
			         myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
			            "postgres", "webstudent");
			        // creating the queries with different collumn names
			         ArrayList<String> sql = new ArrayList<String>();
			         
			         String[] searchBy2 = {"username","title"};
			         if(null != searchBy) {
				         for (int i = 0 ;i< searchBy.length; i++) {
				        	 sql.add("SELECT * FROM public.\"libraryLogbook\" where \""+searchBy[i]+"\" ilike ?;");
				         }
			         }else {
			        	 for (int i = 0 ;i< searchBy2.length; i++) {
				        	 sql.add("SELECT * FROM public.\"libraryLogbook\" where \""+searchBy2[i]+"\" ilike ?;");
				         }
			         }

			         for(int i = 0;i<sql.size();i++) {
			        	 myPStmt = myConn.prepareStatement(sql.get(i));
			        	// myPStmt.setString(1, temp);
			        	 myPStmt.setString(1, '%'+searchText+'%');
			        	 myRs = myPStmt.executeQuery();
//			        	 System.out.println(myPStmt);
			        	 while(myRs.next()) {
			        		 boolean add =true;
			        		 LibraryLogbook_Entry myLibraryLogbook_Entry = new LibraryLogbook_Entry(myRs.getString(1),myRs.getString(2),
			        				 myRs.getString(3),myRs.getDate(4),myRs.getInt(5),
			        				 myRs.getString(6), myRs.getString(7));
			        		 
			        		 for (LibraryLogbook_Entry temp : searchedEntries) {
			        			 if(temp.getUsername().equals(myLibraryLogbook_Entry.getUsername())) {
			        				 add=false;
			        				 break;
			        			 }
			        		 }
			        		 
			        		 if(add){	
			        			 searchedEntries.add(myLibraryLogbook_Entry);
			        		 }
			        			 
			        	 }
			        	 
			         }
				}catch(Exception e) {

					e.printStackTrace();
				}
				finally {
					close(myConn,myStmt,myRs,myPStmt);				
				}
				
				return searchedEntries;			}

			public void updateLogbook() {

				java.util.Date today = new java.util.Date();
				
				List<LibraryLogbook_Entry> reservedMembers = new ArrayList<LibraryLogbook_Entry>();
 				
				//updates a logbook checks if someone who has reserved the book has'nt checked it out will remove his entry and 
				//makes the book available for checkout
				
				
				try {
					
					//create the connection
					Class.forName("org.postgresql.Driver");
			         myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
			            "postgres", "webstudent");
			         
			         myPStmt = myConn.prepareStatement("SELECT *\r\n"
			         		+ "	FROM public.\"libraryLogbook\" where \"action\"='reserve';");
			         
			         myRs = myPStmt.executeQuery();
			         while(myRs.next()) {

			        	 if(today.compareTo(myRs.getDate(4))>0) {
					         

			        		 reservedMembers.add(new LibraryLogbook_Entry(myRs.getString(1),myRs.getString(2),myRs.getString(3),myRs.getDate(4),
				        			 myRs.getInt(5),myRs.getString(6),myRs.getString(7)));
			        	 }
			        	
			         }
			         
			         for(LibraryLogbook_Entry temp : reservedMembers) {
			        	 
			        	 //getting the person reserved from 
			        	 LibraryLogbook_Entry reservedFrom = getEntry(temp.getReservedFrom(),temp.getTitle());
			        	 
			        	 //we have to delete there entry and the person reserving for them too 
				         deleteEntry(temp.getUsername(),temp.getTitle());
				         deleteEntry(reservedFrom.getUsername(),reservedFrom.getTitle());
				         
				         // also we have to increase the numeber of bboks inside the library
				         Books_DB_Util books_DB = new Books_DB_Util();
				         Book newBook = null;
				         newBook = books_DB.get_book_whose_title(reservedFrom.getTitle());
				         newBook.setCopiesAvail(newBook.getCopiesAvail()+1);
				         books_DB.updateBook(newBook, newBook.getTitle());
				         
				         
				         
				         
			         }
				}catch(Exception e) {
					e.printStackTrace();
				}
				finally {
					close(myConn,myStmt,myRs,myPStmt);
				}
				
			}

			public List<LibraryLogbook_Entry> getEntriesofTheBook(String title) {
				ArrayList<LibraryLogbook_Entry> myEntries = new ArrayList<>();
				try {
					//creating the connection
					Class.forName("org.postgresql.Driver");
					myConn = DriverManager.getConnection("jdbc:postgresql://localhost:5432/books_manager",
					           "postgres", "webstudent");
					myPStmt = myConn.prepareStatement("SELECT *\r\n"
							+ "	FROM public.\"libraryLogbook\"\r\n"
							+ "	where \"title\"=?;");
					myPStmt.setString(1, title);
					
					myRs = myPStmt.executeQuery();

					while(myRs.next()) {
						
						myEntries.add(new LibraryLogbook_Entry(myRs.getString(1),myRs.getString(2),
								myRs.getString(3),myRs.getDate(4),myRs.getInt(5),myRs.getString(6),myRs.getString(7)));
					}
				}
				catch(Exception e) {
					e.printStackTrace();
				}
				finally {
					close(myConn,myStmt,myRs,myPStmt);
				}
				return myEntries;
			}
}
