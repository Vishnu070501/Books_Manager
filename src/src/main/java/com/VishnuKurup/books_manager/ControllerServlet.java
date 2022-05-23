package com.VishnuKurup.books_manager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.VishnuKurup.books_manager.containers.*;
import com.VishnuKurup.books_manager.dbUtils.*;



/**
 * Servlet implementation class ControllerServlet
 */
@WebServlet("/ControllerServlet")
public class ControllerServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//Accessor of the accounts datasource
	private Accounts_DB_Util Accounts_DB = new Accounts_DB_Util();
	//To Accessor of the Books database
	private Books_DB_Util Books_DB = new Books_DB_Util();
	
	private LibraryLogbook_DB_Util LogBook_DB = new LibraryLogbook_DB_Util();
	

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//getting the command request from the page accessing the servlet
		String cmd = request.getParameter("command");
		
		if(cmd==null) {
			cmd="default";
		}
		
		switch (cmd) {
		case "LOG_CHECK":
			//checking credentials
			logCheck(request,response);
			break;
			
		case "ADDUSER":
			//to add a user
			addUser(request,response);
			break;
			
		case "ADDBOOK":
			//new book add
			addBook(request,response);
			break;
		
		case "LOADBOOK":
			//to get information on the book to update and show those information in the
			//update form as default values
			getBook(request,response);
			break;
			
		case "UPDATEBOOK":
			//updates an existing book
			updateBook(request,response);
			break;
			
		case "DELETEBOOK":
			//deletes a record
			deleteBook(request,response);
			break;
			
		case "MYBOOKS":
			//shows a book owned by the owner 
			myBooks(request,response);
			break;
				
		case "SEARCHBOOKS":
			//search for books
			searchBooks(request,response);
			break;
			
		case "VIEWUSER":
			viewUsers(request,response);
			break;
			
		case "SEARCHUSER":			//to get information on the user to update and show those information in the
			//update form as default values
			searchUser(request,response);
			break;
			
		case "LOADUSER":
			//to load user into the update form 
			getUser(request,response);
			break;
			
		case "UPDATEUSER":
			//to update a user
			updateUser(request,response);
			break;
			
		case "DELETEUSER":
			//to delete a user 
			deleteUser(request,response);
			break;
			
		case "VIEWBOOKS":
			//shows the books for the user
			viewBooks(request,response);
			break;
			
		case "CHECKOUTBOOK":
			//checks out a vook for a user 
			checkoutBook(request,response);
			break;
			
		case "RETURNBOOK" :
			//returns a given Book
			returnBook(request,response);
			break;
			
		case "RENEWBOOK" :
			//extends the  due date of a book by 5 days
			renewBook(request,response);
			break;
		
		case "RESERVEBOOK":
			//allows a person to reserve a book 
			reserveBook(request,response);
			break;
			
		case "UNRESERVEBOOK":
			//allows to unreserve an already reserved book
			unreserveBook(request,response);
			break;
			
		case "LOGOUT":
			//discarding the session
			request.getSession().invalidate();
			//takes to the login Page
			RequestDispatcher dispatcher = request.getRequestDispatcher("login_page.jsp");
			dispatcher.forward(request, response);
			break;
			
		case "SEARCHENTRY":
			//searching an entry from the logbook
			searchEntry(request,response);
			break;
			
		case "USERPAGE":
			RequestDispatcher userDispatcher = request.getRequestDispatcher("userPage.jsp");
			userDispatcher.forward(request, response);
			break;
			
		case "LIBRARIANPAGE":
			RequestDispatcher librarianDispatcher = request.getRequestDispatcher("librarianPage.jsp");
			librarianDispatcher.forward(request, response);
			break;
			
		
		default:
			//takes to the login Page
			RequestDispatcher dispatcher2 = request.getRequestDispatcher("login_page.jsp");
			dispatcher2.forward(request, response);
			break;
		
		}
	}
	private void searchEntry(HttpServletRequest request, HttpServletResponse response) {


		Set<LibraryLogbook_Entry> theEntries = null;
		//get the information on that Book from the db using the book db util
		theEntries = LogBook_DB.searchEntry(request.getParameter("searchText"),request.getParameterValues("searchBy"));
		
		request.setAttribute("entries", theEntries);
		
		//viewing the users
		RequestDispatcher dispatcher = request.getRequestDispatcher("ListmyBooks.jsp");
		try {
			dispatcher.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	//removing the username name from the person he has reserved the book
	//just simple deleting the entry in user's name and the book
	private void unreserveBook(HttpServletRequest request, HttpServletResponse response) {
		
		LibraryLogbook_Entry userEntry = LogBook_DB.getEntry((String)request.getSession().getAttribute("username"), request.getParameter("title")); 
		LibraryLogbook_Entry reservedFromEntry = LogBook_DB.getEntry((userEntry.getReservedFrom()),request.getParameter("title"));
		reservedFromEntry.setReservedFor("NA");
		reservedFromEntry.setReservedFrom("NA");
		
		
		//removing the name of the user from the person he has reserved
		LogBook_DB.updateEntry(userEntry.getReservedFrom(), request.getParameter("title"), reservedFromEntry);
		
		//deleting the reserving persons entry
		LogBook_DB.deleteEntry((String)request.getSession().getAttribute("username"), request.getParameter("title"));
		
		myBooks(request,response);
		
		
	}
	////////////////////////////
	private void reserveBook(HttpServletRequest request, HttpServletResponse response) {

		//a book can only be reserved in the following conditions 
		
		//if the book is'nt available 
		
		//user does'nt have the book and the number of books owned by the user <5
		
		//and if all the books have'nt been reserved 
		Book theBook = Books_DB.get_book_whose_title(request.getParameter("title"));
	
		if(theBook.getCopiesAvail()==0 && LogBook_DB.check_for_unreserve(request.getParameter("title")) && 
				LogBook_DB.checkMyTotalBooks((String)request.getSession().getAttribute("username"))<5 && 
				LogBook_DB.checkForBook((String)request.getSession(false).getAttribute("username"),request.getParameter("title"))
				) {
			//getting the book that is earliest date available
			LibraryLogbook_Entry earliestEntry = LogBook_DB.getTheBookEarliestAvail(request.getParameter("title"));
			
			java.util.Date dueDate = null;
			
			Calendar cal = Calendar.getInstance();
			
			cal.setTime(earliestEntry.getDueDate());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			cal.add(Calendar.DAY_OF_MONTH, 5);
			
			dueDate = cal.getTime();
			
			LibraryLogbook_Entry newEntry = new LibraryLogbook_Entry((String)request.getSession().getAttribute("username"),
					request.getParameter("title"),"reserve",dueDate,0,"NA",earliestEntry.getUsername());
			//entry into logbook
			LogBook_DB.entry(newEntry);
			
			earliestEntry.setReservedFor((String)request.getSession().getAttribute("username"));
			
			LogBook_DB.updateEntry(earliestEntry.getUsername(), earliestEntry.getTitle(), earliestEntry);
			
			request.setAttribute("actionStatus", "The book "+request.getParameter("title")+
					" has been reserved for you,and you will get the Book on the date (yyyy-mm-dd)"+sdf.format(earliestEntry.getDueDate())+" from "+earliestEntry.getUsername());
			
			viewBooks(request,response);
			
			
			
		}
		else if(theBook.getCopiesAvail()!=0) {
			
			request.setAttribute("actionStatus", "The book"+request.getParameter("title")+
					" is available to checkout");
			viewBooks(request,response);
		}
		
		else if(!LogBook_DB.check_for_unreserve(request.getParameter("title"))) {
			
			request.setAttribute("actionStatus", "The book"+request.getParameter("title")+
					"is not available to reserve.");
			viewBooks(request,response);
		}
		
		else if(LogBook_DB.checkMyTotalBooks((String)request.getSession().getAttribute("username"))==5){
			request.setAttribute("actionStatus", "The book"+request.getParameter("title")+
					"cannot be reserved by you as you own 5 books already return some Books to proceed");
			
			viewBooks(request,response);

		}
		
		else if(!LogBook_DB.checkForBook((String)request.getSession(false).getAttribute("username"),
				request.getParameter("title"))) {
			
			request.setAttribute("actionStatus", "The book"+request.getParameter("title")+
					"cannot be reserved by you as you already posess this Book.");

			viewBooks(request,response);
		}
				
	}

	private void renewBook(HttpServletRequest request, HttpServletResponse response) {


		java.util.Date today = new java.util.Date();
		
		LibraryLogbook_Entry theEntry = LogBook_DB.getEntry((String)request.getSession(false).getAttribute("username"),request.getParameter("title")); 
		
		//renewal will be done checking 2 conditions:
		//1.is todays date a day before due date 
		
		long dueDateHowFarAway = theEntry.getDueDate().getTime() - today.getTime();
		//is the renewal count <=2
						//day in milliseconds
		if(dueDateHowFarAway<=(24*60*60*1000) && theEntry.getRenewalCount()<2) {
			
			//updating the renewal count 
			theEntry.setRenewalCount(theEntry.getRenewalCount()+1);
			
			//setting the due date to 5 days extra 
			java.util.Date dueDate = theEntry.getDueDate();
			
			Calendar cal = Calendar.getInstance();
			
			cal.setTime(dueDate);
			cal.add(Calendar.DAY_OF_MONTH, 5);
			
			dueDate = cal.getTime();
			
			theEntry.setDueDate(dueDate);
			
			//his entry has been updated
			LogBook_DB.updateEntry((String)request.getSession(false).getAttribute("username"),request.getParameter("title"),theEntry);
			
			//also the book he is using if it is reserved by someone changes have to be made accordingly
			if(!theEntry.getReservedFor().equals("NA")) {
				
				
				//getting the reserved person's entry
				LibraryLogbook_Entry resEntry = LogBook_DB.getEntry(theEntry.getReservedFor(),request.getParameter("title"));
				
				
				//also setting his reserved for as NA temporarily as it 
				//allows us to check the earliest entry for the reserving person
				theEntry.setReservedFor("NA");
				LogBook_DB.updateEntry((String)request.getSession(false).getAttribute("username"),request.getParameter("title"),theEntry);

				//finding the earliest book for him as the renewed person's date has been extended 
				LibraryLogbook_Entry earliestEntry =LogBook_DB.getTheBookEarliestAvail(request.getParameter("title"));
				
				if(earliestEntry.getUsername().equals((String)request.getSession(false).getAttribute("username"))) {
					//if the earliest book is the same renewing person 
					//just extend the date of the reserving person
					//extending his due date by 5 days
					Calendar cal2 = Calendar.getInstance();
					cal2.setTime(resEntry.getDueDate());
					cal2.add(Calendar.DAY_OF_MONTH, 5);
					
					//also setting the renewing person reserved for back to reserved person
					theEntry.setReservedFor(resEntry.getUsername());
					//his entry has been updated
					LogBook_DB.updateEntry((String)request.getSession(false).getAttribute("username"),request.getParameter("title"),theEntry);

					
					resEntry.setDueDate(cal2.getTime());
				}
				else {
					//if the earliest book aint the renewing person change his reserved for to NA
					theEntry.setReservedFor("NA");
					//updating his entry
					LogBook_DB.updateEntry((String)request.getSession(false).getAttribute("username"),request.getParameter("title"),theEntry);
					
					//setting the earliest entry person's reserved for to the reserving person
					earliestEntry.setReservedFor(resEntry.getUsername());
					//updating earliest person's Entry
					LogBook_DB.updateEntry(earliestEntry.getUsername(), earliestEntry.getTitle(), earliestEntry);
					
					//setting the reserving person's new due date to earliest person's due date +5
					Calendar cal3 = Calendar.getInstance();
					cal3.setTime(earliestEntry.getDueDate());
					cal3.add(Calendar.DAY_OF_MONTH, 5);
					resEntry.setDueDate(cal3.getTime());
					
					//setting his reserved from to the new earliest person
					resEntry.setReservedFrom(earliestEntry.getUsername());

				}
				
				//updating his entry
				LogBook_DB.updateEntry(resEntry.getUsername(), request.getParameter("title"), resEntry);
			}
			
			
			
			request.setAttribute("actionStatus", "The due date for the book "+ request.getParameter("title")+" has been renewed"); 
			
			myBooks(request,response);
		}
		else if(dueDateHowFarAway>(24*60*60*1000)) {
			request.setAttribute("actionStatus", "The due date for the book "+ request.getParameter("title")+" can only be been renewed within one day of the due date. "); 
			
			myBooks(request,response);		
			}
		
		else if(theEntry.getRenewalCount()>=2) {
			request.setAttribute("actionStatus", "The due date for the book "+ request.getParameter("title")+" cannot  be renewed as you have reached the maximum renewal count for a book. "); 
			
			myBooks(request,response);		
			}
		
	}

	//returns a book
	private void returnBook(HttpServletRequest request, HttpServletResponse response) {
		
		java.util.Date today = new java.util.Date();
		
		LibraryLogbook_Entry theEntry = LogBook_DB.getEntry((String)request.getSession(false).getAttribute("username"),request.getParameter("title")); 

		//if somebody has reserved this book for and his due date is updated and his action is set to checkout and his reserved from field as NA
		if(!theEntry.getReservedFor().equals("NA")) {
			//getting the reserved person's entry
			LibraryLogbook_Entry resEntry = LogBook_DB.getEntry(theEntry.getReservedFor(),request.getParameter("title"));
			
			//extending his due date by 5 days
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(today);
			cal2.add(Calendar.DAY_OF_MONTH, 5);
			
			resEntry.setDueDate(cal2.getTime());
			resEntry.setAction("checkout");
			resEntry.setReservedFrom("NA");
			
			//updating his entry
			LogBook_DB.updateEntry(theEntry.getReservedFor(), request.getParameter("title"), resEntry);
		}

		//deleting the entry
		LogBook_DB.deleteEntry((String)request.getSession().getAttribute("username"),request.getParameter("title"));
		
		//returning the book to the library if no one has reserved
		if(theEntry.getReservedFor().equals("NA")) {
			Book updatedBook = Books_DB.get_book_whose_title(request.getParameter("title"));
			updatedBook.setCopiesAvail(updatedBook.getCopiesAvail()+1);
			Books_DB.updateBook(updatedBook,updatedBook.getTitle());
		}
		
		request.setAttribute("actionStatus", "The Book"+request.getParameter("title")+" has been returned successfully.");
		
		myBooks(request,response);
		
		
	}

	//gets all the books owned and reserved by user 
	private void myBooks(HttpServletRequest request, HttpServletResponse response) {

		ArrayList<LibraryLogbook_Entry> entries = new ArrayList<LibraryLogbook_Entry>();
		entries = LogBook_DB.getEntriesofUsername((String)request.getSession().getAttribute("username"));
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");

		
		ArrayList<String> dueDates = new ArrayList<>();
		
		for(LibraryLogbook_Entry temp : entries ) {
			dueDates.add(sdf.format(temp.getDueDate()));
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("ListmyBooks.jsp");
		
		request.setAttribute("entries", entries);
		request.setAttribute("dueDates", dueDates);
		
		try {
			dispatcher.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void checkoutBook(HttpServletRequest request, HttpServletResponse response) {
		
		//checking if the book is available 
		Book theBook = Books_DB.get_book_whose_title(request.getParameter("title"));
		
		//checking if the number of books owned by a user is less than 5
		int totalBooks=LogBook_DB.checkMyTotalBooks((String)request.getSession().getAttribute("username"));
		//checking if user has got that book already
		
		if(totalBooks<5 && theBook.getCopiesAvail()>0 &&  
				LogBook_DB.checkForBook((String)request.getSession().getAttribute("username"),request.getParameter("title"))) {
			
			//now updating the logbook and the book database
			theBook.setCopiesAvail(theBook.getCopiesAvail()-1);
			Books_DB.updateBook(theBook, theBook.getTitle());
			
			//adding 5 days to the todays date 
			java.util.Date date = new java.util.Date();
			Calendar cal = Calendar.getInstance();
			
			cal.setTime(date);
			cal.add(Calendar.DAY_OF_MONTH, 5);
			
			date = cal.getTime();
			
			theBook.setCopiesAvail(theBook.getCopiesAvail()+1);
			LogBook_DB.entry(new LibraryLogbook_Entry((String)request.getSession().getAttribute("username"),
					request.getParameter("title"),"checkout",date,0,"NA","NA"));
			
			request.setAttribute("actionStatus",(String)"the Book "+request.getParameter("title")+" has been Checked out successfully");
						
			//calling the view books function
			viewBooks(request,response);
			
		}	
		else if(!LogBook_DB.checkForBook((String)request.getSession().getAttribute("username"),request.getParameter("title"))) {
			request.setAttribute("actionStatus",(String)"Sorry the Book "+request.getParameter("title")+" cannot be checked out by You as you already possess this book.");

			//calling the view books function
			viewBooks(request,response);
		}
		
		else if(!(totalBooks<5)) {
			request.setAttribute("actionStatus",(String)"Sorry the Book "+request.getParameter("title")+" cannot be checked out by You as you already possess 5 books.");

			//calling the view books function
			viewBooks(request,response);
		}
		else if(!(theBook.getCopiesAvail()>0)) {
			request.setAttribute("actionStatus",(String)"Sorry the Book "+request.getParameter("title")+" cannot be checked out by You as the libarary is running short of the book.");

			//calling the view books function
			viewBooks(request,response);
		}
	}

	private void viewBooks(HttpServletRequest request, HttpServletResponse response) {
		
		
		//creating the array list store store all the Books 
				ArrayList <Book> books = new ArrayList<>();
				SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
				
				//calling the method in user db util to give the array list of all users 
				books = Books_DB.ListBooks();
				
				ArrayList<String> publicationDates = new ArrayList<>();
				 for (Book temp : books ) {
					 publicationDates.add(sdf.format(temp.getPublication_date()));
				 }
	
				//getting the request Dispatcher 
				RequestDispatcher dispatcher = request.getRequestDispatcher("ListBooks.jsp");
				
				//adding the parameters to the request object
				request.setAttribute("myBooks", books);
				request.setAttribute("publicationDates",publicationDates);
				
				
				//dispatching the request and response 
				try {
					dispatcher.forward(request, response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		
	}

	//to delete user 
		private void deleteUser(HttpServletRequest request, HttpServletResponse response) {


			// deleting the required username 
			Accounts_DB.deleteUser(request.getParameter("username"));
			
			//listing the new userlist
			viewUsers(request,response);
			
		}
	
	//to update a user
		private void updateUser(HttpServletRequest request, HttpServletResponse response) {
			
			String newUsername = request.getParameter("username");
			//updating the user with the old username 
			Accounts_DB.updateUser(request.getParameter("oldUsername"),
					new Account(request.getParameter("username"),request.getParameter("password"),null,request.getParameter("name"),
							request.getParameter("qualification"),request.getParameter("email")),request.getParameter("myself"));
			//showing the new list to librarian
			if(request.getSession().getAttribute("userType").equals("librarian") && request.getParameter("myself").equals("false")) {
				viewUsers(request,response);
			}
			
			else if(request.getSession().getAttribute("userType").equals("librarian") && request.getParameter("myself").equals("true")) {
				//updating the username of the session to the new one 
				request.getSession(false).setAttribute("username", newUsername);
				RequestDispatcher dispatcher = request.getRequestDispatcher("librarianPage.jsp");
				request.setAttribute("actionStatus", "Your Information has been Updated ");
				try {
					dispatcher.forward(request, response);
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//if the user updates his username and it is done by the user it'll go to the userpage with his new username
			else {
				//updating the username of the session to the new one 
				request.getSession(false).setAttribute("username", newUsername);
				RequestDispatcher dispatcher = request.getRequestDispatcher("userPage.jsp");
				request.setAttribute("actionStatus", "Your Information has been Updated ");
				try {
					dispatcher.forward(request, response);
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	
	private void getUser(HttpServletRequest request, HttpServletResponse response) {


		//get the user from the update request
		String userName = request.getParameter("username");					
	
		//get the information on that Book from the db using the book db util
		Account theAccount= Accounts_DB.getUserWhoseUsername(userName);
		
		//attach those information to the request object and dispatching it to the admin update form if admin updates
		if(request.getSession().getAttribute("userType").equals("librarian") && !userName.equals(request.getSession().getAttribute("username"))) {
			RequestDispatcher dispatcher = request.getRequestDispatcher("updateUserFormLibrarian.jsp");
			request.setAttribute("theUser", theAccount);
			
			try {
				dispatcher.forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
				
			}
		else  {
			RequestDispatcher dispatcher = request.getRequestDispatcher("updateYourInfo.jsp");
			request.setAttribute("theUser", theAccount);
			try {
				dispatcher.forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
	}


		//to search users 
		private void searchUser(HttpServletRequest request, HttpServletResponse response) {
			
			//get the user from the update request
			Set<Account> theUserAccounts = null;
			//get the information on that Book from the db using the book db util
			theUserAccounts= Accounts_DB.searchUsers(request.getParameter("searchText"),request.getParameterValues("searchBy"));
			
			request.setAttribute("myUsers", theUserAccounts);
			
			//viewing the users
			RequestDispatcher dispatcher = request.getRequestDispatcher("ListUsers.jsp");
			try {
				dispatcher.forward(request, response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
				
			
		}
		//to add user
		private void addUser(HttpServletRequest request, HttpServletResponse response) {


			//calling the useradd function in db util with our user object 
			Accounts_DB.addUser(new Account(request.getParameter("username"),request.getParameter("password"),"user",
							request.getParameter("email"),request.getParameter("qualification"),
							request.getParameter("name")));
			
			//if the userType is a admin
			if(request.getSession(false).getAttribute("userType").equals("librarian")){
				viewUsers(request,response);
			}
			
			else {
				RequestDispatcher dispatcher = request.getRequestDispatcher("login_page.jsp");
				request.setAttribute("signedUp","User Successfully Registered");
				try {
					dispatcher.forward(request, response);
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//go to the admin list users page
			
		}
		
	//veiwing all users to admin
	private void viewUsers(HttpServletRequest request, HttpServletResponse response) {

		//creating array list to store users
		ArrayList<Account> myUsers = new ArrayList<>();
		
		myUsers = Accounts_DB.getUsers();
		
		//forwarding the request
		RequestDispatcher dispatcher = request.getRequestDispatcher("ListUsers.jsp");
		request.setAttribute("myUsers", myUsers);
		
		try {
			dispatcher.forward(request, response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void searchBooks(HttpServletRequest request, HttpServletResponse response) {
		
		Set<Book> theBooks = null;
		//get the information on that Book from the db using the book db util
		theBooks = Books_DB.searchBook(request.getParameter("searchText"),request.getParameterValues("searchBy"));
		
		request.setAttribute("myBooks", theBooks);
		
		//viewing the users
		RequestDispatcher dispatcher = request.getRequestDispatcher("ListBooks.jsp");
		try {
			dispatcher.forward(request, response);
		} catch (ServletException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void deleteBook(HttpServletRequest request, HttpServletResponse response) {
		
		//getting the title of the book to be deleted 
		String theTitle = request.getParameter("title");
				
		//calling the function in the Books db util to delete a record
		Books_DB.deleteBook(theTitle);
				
		//listing the students from the database
		try {
			viewBooks(request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void updateBook(HttpServletRequest request, HttpServletResponse response) {


		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");	
		java.util.Date publicationDate = null;
//		System.out.println(request.getParameter("publicationDate"));
		
		try {
			publicationDate = sdf.parse(request.getParameter("publicationDate"));
		}catch(Exception e) {
			e.printStackTrace();
		}
//		System.out.println(publicationDate);
		//creating a new book instance to add to the db
		Book myBook = new Book(request.getParameter("title"),request.getParameter("author"),
				request.getParameter("subject"),publicationDate,
				Integer.parseInt(request.getParameter("copiesAvail")));

				
				//pass the id to the update method in studentDB util
				Books_DB.updateBook(myBook,request.getParameter("oldTitle"));
				
				//list out the new list
				viewBooks(request,response);
	}

//	function to get the values of the Book and upload into the update book form
	private void getBook(HttpServletRequest request, HttpServletResponse response) {
		
		//get the book from the update request
				String title = request.getParameter("title");
						
			
		//get the information on that Book from the db using the book db util
				Book theBook= Books_DB.get_book_whose_title(title);
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
				String publicationDate = sdf.format(theBook.getPublication_date());
				
		//attach those information to the request object and dispatching it to the 
				RequestDispatcher dispatcher = request.getRequestDispatcher("updateBookForm.jsp");
				request.setAttribute("myBook", theBook);
				request.setAttribute("publicationDate", publicationDate);
			
				try {
					dispatcher.forward(request, response);
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		
	}

	//the method to add new book
	private void addBook(HttpServletRequest request, HttpServletResponse response) {
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");	
		java.util.Date publicationDate = null;
		
		try {
			publicationDate = sdf.parse(request.getParameter("publicationDate"));
		}catch(Exception e) {
			e.printStackTrace();
		}
		//creating a new book instance to add to the db
		Book newBook = new Book(request.getParameter("title"),request.getParameter("author"),
				request.getParameter("subject"),publicationDate,
				Integer.parseInt(request.getParameter("copiesAvail")));
		
		//adding the book the book database 
		Books_DB.add_book(newBook);
		
		//listing the new database 
		viewBooks(request,response);
	}

	private void logCheck(HttpServletRequest request, HttpServletResponse response) {
		
		Account theAcct = new Account(request.getParameter("username"),
				request.getParameter("password"),request.getParameter("userType"),null,null,null);//creating an account instance to store credentials
		
		//calling the account checking function in accounts util database 
		if(Accounts_DB.logCheck(theAcct)) {
			
			if(theAcct.getAccountType().equals("librarian")) {
				HttpSession session = request.getSession();
				session.setAttribute("username", theAcct.getUsername());
				session.setAttribute("userType",theAcct.getAccountType());
				RequestDispatcher dispatcher = request.getRequestDispatcher("librarianPage.jsp");
				try {
					dispatcher.forward(request, response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} 
			}
			else {
				HttpSession session = request.getSession();
				session.setAttribute("username", theAcct.getUsername());
				session.setAttribute("userType",theAcct.getAccountType());
				RequestDispatcher dispatcher = request.getRequestDispatcher("userPage.jsp");
				try {
					dispatcher.forward(request, response);
				} catch (ServletException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
				
			
		}
		
		else {
			
			//opening the login pasge with the wrong credentials message 
			RequestDispatcher dispatcher = request.getRequestDispatcher("login_page.jsp");
			request.setAttribute("invalid_cred","Wrong username or password");
			try {
				dispatcher.forward(request,response);
			} catch (ServletException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
