package com.VishnuKurup.books_manager;

import java.io.IOException;
import java.util.*;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



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

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		//getting the command request from the page accessing the servlet
		String cmd = request.getParameter("command");
		
		if(cmd==null)
			cmd="default";
		
		switch (cmd) {
		//checking credentials
		case "LOG_CHECK":
			valid_user_check(request,response);
			break;
			
		case "ADD":
			//new book add
			add_book(request,response);
			break;
		
		case "LOAD":
			//to get information on the book to update and show those information in the
			//update form as default values
			getBook(request,response);
			break;
			
		case "UPDATE":
			//updates an existing book
			updateBook(request,response);
			break;
			
		case "DELETE":
			//deletes a record
			deleteBook(request,response);
			break;
			
		case "SEARCH":
			//search for books
			searchBooks(request,response);
			break;
		
		default :
			//displays the books on the homepage for a normal user
			List_books_Homepage(request,response);
			break;
		}
	}
	
	private void searchBooks(HttpServletRequest request, HttpServletResponse response) {
		
		//creating an array list to store the books that are searched
		ArrayList<Book> searchedBooks = new ArrayList<>();
				
		//calling the search function from the books bd util
		searchedBooks=Books_DB.searchBooks(request.getParameter("search_text"),request.getParameter("search_by"));
		
		//getting the user type 
		String usertype = request.getParameter("usertype");
		
		//admin page
		if (usertype.equals("admin")) {
			
			//getting the request dispatcher 
			RequestDispatcher dispatcher = request.getRequestDispatcher("List_Books.jsp");
			request.setAttribute("myBooks", searchedBooks);
			try {
				//forwarding to the page 
				dispatcher.forward(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else {
			//getting the request dispatcher 
			RequestDispatcher dispatcher = request.getRequestDispatcher("Homepage.jsp");
			request.setAttribute("myBooks", searchedBooks);
			try {
				//forwarding to the page 
				dispatcher.forward(request, response);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	private void deleteBook(HttpServletRequest request, HttpServletResponse response) {
		
		//getting the title of the book to be deleted 
		String theTitle = request.getParameter("title");
				
		//calling the function in the Books db util to delete a record
		Books_DB.deleteBook(theTitle);
				
		//listing the students from the database
		try {
			List_books(request,response);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	private void updateBook(HttpServletRequest request, HttpServletResponse response) {


		//get the id from the request object and creating the student object to pass 
				// to the update function in the database util
				
				 Book myBook = new Book(request.getParameter("title"),
						request.getParameter("author"),request.getParameter("subject"),
						request.getParameter("publication_date"));

				
				//pass the id to the update method in studentDB util
				Books_DB.updateBook(myBook,request.getParameter("oldtitle"));
				
				//list out the new list
				List_books(request,response);
	}

	//function to get the values of the student 
	private void getBook(HttpServletRequest request, HttpServletResponse response) {
		
		//get the book from the update request
				String title = request.getParameter("title");
						
			
		//get the information on that Book from the db using the book db util
				Book theBook= Books_DB.get_book_whose_title(title);
				
		//attach those information to the request object and dispatching it to the 
				RequestDispatcher dispatcher = request.getRequestDispatcher("/update-book-form.jsp");
				request.setAttribute("theBook", theBook);
			
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
	private void add_book(HttpServletRequest request, HttpServletResponse response) {
		
		//creating a new book instance to add to the db
		Book newBook = new Book(request.getParameter("title"),request.getParameter("author"),
				request.getParameter("subject"),request.getParameter("publication_date"));
		
		//adding the book the book database 
		Books_DB.add_book(newBook);
		
		//listing the new database 
		List_books(request,response);
	}

	private void valid_user_check(HttpServletRequest request, HttpServletResponse response) {
		
		Account theacct = new Account(request.getParameter("username"),
				request.getParameter("password"));//creating an account instance to store credentials
		//calling the account checking function in accounts util database 
		if(Accounts_DB.valid_user_check(theacct)) {
			
			//if a valid acct then go to the books display page
			//create dispatcher
			List_books(request,response);
			
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

	//to show all the books in the db
	private void List_books(HttpServletRequest request, HttpServletResponse response) {
		
		//creating an array list to store all the books  
		ArrayList<Book> myBooks = new ArrayList<>();
		
		//calling the list Books method 
		myBooks = Books_DB.ListBooks();
		
		//setting the attribute to the request 
		request.setAttribute("myBooks", myBooks);
		//getting the request dispatcher 
		RequestDispatcher dispatcher = request.getRequestDispatcher("List_Books.jsp");
		//dispatching the request
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
	
	private void List_books_Homepage(HttpServletRequest request, HttpServletResponse response) {
		
		//creating an array list to store all the books  
		ArrayList<Book> myBooks = new ArrayList<>();
		
		//calling the list Books method 
		myBooks = Books_DB.ListBooks();
		
		//setting the attribute to the request 
		request.setAttribute("myBooks", myBooks);
		//getting the request dispatcher 
		RequestDispatcher dispatcher = request.getRequestDispatcher("Homepage.jsp");
		//dispatching the request
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
