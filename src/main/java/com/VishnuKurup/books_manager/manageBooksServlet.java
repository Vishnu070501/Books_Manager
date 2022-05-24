package com.VishnuKurup.books_manager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.VishnuKurup.books_manager.containers.Book;
import com.VishnuKurup.books_manager.dbUtils.Books_DB_Util;

/**
 * Servlet implementation class manageBooksServlet
 */
@WebServlet("/manageBooksServlet")
public class manageBooksServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//to use the books database
	private Books_DB_Util Books_DB = new Books_DB_Util();

       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public manageBooksServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//getting the command request from the page accessing the servlet
				String cmd = request.getParameter("command");
				
				if(cmd==null) {
					cmd="default";
				}
		
				
				switch(cmd) {
				
				case "VIEWBOOKS":
					//shows the books for the user
					viewBooks(request,response);
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
					
				case "SEARCHBOOKS":
					//search for books
					searchBooks(request,response);
					break;
					
				default:
					//takes to the login Page
					RequestDispatcher dispatcher2 = request.getRequestDispatcher("login_page.jsp");
					dispatcher2.forward(request, response);
					break;

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
	
	//to search among all the books
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
	
	//to delete a book from library
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


	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}