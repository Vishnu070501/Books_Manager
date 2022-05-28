package com.VishnuKurup.books_manager;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.VishnuKurup.books_manager.containers.Book;
import com.VishnuKurup.books_manager.containers.LibraryLogbook_Entry;
import com.VishnuKurup.books_manager.dbUtils.Books_DB_Util;
import com.VishnuKurup.books_manager.dbUtils.LibraryLogbook_DB_Util;

/**
 * Servlet implementation class manageEntriesServlet
 */
@WebServlet("/manageEntriesServlet")
public class manageEntriesServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//To Access of the Books database
		private Books_DB_Util Books_DB = new Books_DB_Util();
		
	//to access the logbook database
		
		private LibraryLogbook_DB_Util LogBook_DB = new LibraryLogbook_DB_Util();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public manageEntriesServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	
		//getting the command request from the page accessing the servlet
		String cmd = request.getParameter("command");
		
		if(request.getSession(false)==null) {
			request.getSession().setAttribute("loginServletCommand", cmd);
			request.getRequestDispatcher("login_page.jsp");
		}
				
		if(cmd==null) {
			cmd="default";
		}
		
		else if(cmd.equals("LOG_CHECK")){
			cmd = (String)request.getSession(false).getAttribute("loginServletCommand");
		}
		
		switch(cmd) {
		
		
		case "MYBOOKS":
			//shows a book owned by the owner 
			myBooks(request,response);
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
			
		case "CHECKOUTRESERVEDBOOK":
			//checking out an unreserved Book
			checkoutReservedBook(request,response);
			break;
			
		case "SEARCHENTRY":
			//searching an entry from the logbook
			searchEntry(request,response);
			break;
			
		default:
			//takes to the login Page
			RequestDispatcher dispatcher2 = request.getRequestDispatcher("login_page.jsp");
			dispatcher2.forward(request, response);
			break;
		
		}
	
	}
	
	
	private void checkoutReservedBook(HttpServletRequest request, HttpServletResponse response) {

		//user's entry
		LibraryLogbook_Entry reservingPersonEntry = LogBook_DB.getEntry((String)request.getSession(false).getAttribute("username"), request.getParameter("title"));
		// a reserved book can be checked out only if the other person has returned the book
		LibraryLogbook_Entry reservedFromPerson = LogBook_DB.getEntry(reservingPersonEntry.getReservedFrom(), request.getParameter("title"));
		
		
		if(reservedFromPerson.getAction().equals("returned")) {
			
			//today's date
			java.util.Date today = new java.util.Date();
			
			//if the person has returned the book delete his entry 
			LogBook_DB.deleteEntry(reservedFromPerson.getUsername(), reservedFromPerson.getTitle());
			
			//change reserving person's reserved from to NA
			reservingPersonEntry.setReservedFrom("NA");
			
			//and change his action to checkout
			reservingPersonEntry.setAction("checkout");
			
			//also setting his due date for 5 days
			Calendar cal = Calendar.getInstance();
			
			cal.setTime(today);
			
			cal.add(Calendar.DAY_OF_MONTH, 5);
			
			reservingPersonEntry.setDueDate(cal.getTime());
			
			//updating his entry
			LogBook_DB.updateEntry(reservingPersonEntry.getUsername(), reservingPersonEntry.getTitle(), reservingPersonEntry);
			
			request.setAttribute("actionStatus", "The book "+request.getParameter("title")+"You reserved from "+reservedFromPerson.getUsername()+" has been returned and you have successfully checked it out.");
			
			myBooks(request,response);
		}
		else {
			request.setAttribute("actionStatus", "The book "+request.getParameter("title")+"You reserved from "+reservedFromPerson.getUsername()+" has'nt been returned so You can't checkout.");
			
			myBooks(request,response);
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
		
		
		LibraryLogbook_Entry userEntry = LogBook_DB.getEntry((String)request.getSession(false).getAttribute("username"), request.getParameter("title")); 
		LibraryLogbook_Entry reservedFromEntry = LogBook_DB.getEntry((userEntry.getReservedFrom()),request.getParameter("title"));
		
		//if the person has checkout out that book 
		if(reservedFromEntry.getAction().equals("checkout")) {
			reservedFromEntry.setReservedFor("NA");
			reservedFromEntry.setReservedFrom("NA");
			
			
			//removing the name of the user from the person he has reserved
			LogBook_DB.updateEntry(userEntry.getReservedFrom(), request.getParameter("title"), reservedFromEntry);
			
			//deleting the reserving persons entry
			LogBook_DB.deleteEntry((String)request.getSession(false).getAttribute("username"), request.getParameter("title"));
			
			
		}
		
		//the person has returned the book and the book is waiting to be checked out by the reserver but he unreserves
		else {
			
			//just delete both the people's entry and return the book to the library
			//deleting the reserving persons entry
			LogBook_DB.deleteEntry((String)request.getSession(false).getAttribute("username"), request.getParameter("title"));
			//deleting the reserver's Entry
			LogBook_DB.deleteEntry(reservedFromEntry.getUsername(), reservedFromEntry.getTitle());
			
			//incrementing the book count
			Book reservedBook = Books_DB.get_book_whose_title(request.getParameter("title"));
			reservedBook.setCopiesAvail(reservedBook.getCopiesAvail()+1);
			Books_DB.updateBook(reservedBook, reservedBook.getTitle());
			
		}
		
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
				LogBook_DB.checkMyTotalBooks((String)request.getSession(false).getAttribute("username"))<5 && 
				LogBook_DB.checkForBook((String)request.getSession(false).getAttribute("username"),request.getParameter("title"))
				) {
			//getting the book that is earliest date available
			LibraryLogbook_Entry earliestEntry = LogBook_DB.getTheBookEarliestAvail(request.getParameter("title"));
			
			java.util.Date dueDate = null;
			
			Calendar cal = Calendar.getInstance();
			
			cal.setTime(earliestEntry.getDueDate());
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			cal.add(Calendar.DAY_OF_MONTH, 1);
			
			dueDate = cal.getTime();
			
			LibraryLogbook_Entry newEntry = new LibraryLogbook_Entry((String)request.getSession(false).getAttribute("username"),
					request.getParameter("title"),"reserve",dueDate,0,"NA",earliestEntry.getUsername());
			//entry into logbook
			LogBook_DB.entry(newEntry);
			
			earliestEntry.setReservedFor((String)request.getSession(false).getAttribute("username"));
			
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
		
		else if(LogBook_DB.checkMyTotalBooks((String)request.getSession(false).getAttribute("username"))==5){
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
		
		
		LibraryLogbook_Entry theEntry = LogBook_DB.getEntry((String)request.getSession(false).getAttribute("username"),request.getParameter("title")); 

		//if somebody has reserved this book for we have to set the user's action as returned
		if(!theEntry.getReservedFor().equals("NA")) {
			//book returned but reserved
			theEntry.setAction("returned");
			//updating his entry
			LogBook_DB.updateEntry((String)request.getSession(false).getAttribute("username"), request.getParameter("title"), theEntry);
		}
		
		//if no one has reserved that Book
		else if(theEntry.getReservedFor().equals("NA")) {
						
			Set<LibraryLogbook_Entry> reservedEntries = LogBook_DB.searchEntry("reserve", new String[] {"action"});
			
			//getting only the reserved book having the returned title
			for (LibraryLogbook_Entry temp : reservedEntries ) {
				if(!temp.getTitle().equals(theEntry.getTitle())) {
					reservedEntries.remove(temp);
				}
			}
			
			//if anyone has reserved the book under the same title 
			if (reservedEntries.size()!=0) {
				java.util.Date earliestDueDate = new java.util.Date();
				LibraryLogbook_Entry earliestEntry = null;
				int loopCount = 1 ;
				for (LibraryLogbook_Entry temp : reservedEntries) {
					if(loopCount == 1){
						earliestDueDate = temp.getDueDate();
						earliestEntry = temp;
						
					}
					else if(earliestDueDate.after(temp.getDueDate())) {
						earliestDueDate = temp.getDueDate();
						earliestEntry = temp;
						
					}
				}
				
				//setting the returning person's action as returned and his reserved for as the reserving person's username
				theEntry.setAction("returned");
				theEntry.setReservedFor(earliestEntry.getUsername());
				LogBook_DB.updateEntry(theEntry.getUsername(), theEntry.getTitle(), theEntry);
				
				//and breaking his connection with the old reserved from
				LibraryLogbook_Entry oldReservedFrom = LogBook_DB.getEntry(earliestEntry.getReservedFrom(), earliestEntry.getTitle());
				oldReservedFrom.setReservedFor("NA");
				LogBook_DB.updateEntry(oldReservedFrom.getUsername(), oldReservedFrom.getTitle(), oldReservedFrom);
				
				//setting the reserving persons reserved from as the returning person(person who is returning his book earlier) 
				earliestEntry.setReservedFrom(theEntry.getUsername());
				//setting his due date for 2 days from the day the book was returned
				java.util.Date today = new java.util.Date();
				
				Calendar cal = Calendar.getInstance();
				
				cal.setTime(today);
				cal.add(Calendar.DAY_OF_MONTH, 2);
				earliestEntry.setDueDate(cal.getTime());
				
				LogBook_DB.updateEntry(earliestEntry.getUsername(), earliestEntry.getTitle(), earliestEntry);
			}
			
			//if no one has reserved the returning book
			else {
				Book updatedBook = Books_DB.get_book_whose_title(request.getParameter("title"));
				updatedBook.setCopiesAvail(updatedBook.getCopiesAvail()+1);
				Books_DB.updateBook(updatedBook,updatedBook.getTitle());
				
				//deleting the entry
				LogBook_DB.deleteEntry((String)request.getSession(false).getAttribute("username"),request.getParameter("title"));
			}
			
		}
		
		request.setAttribute("actionStatus", "The Book"+request.getParameter("title")+" has been returned successfully.");
		
		myBooks(request,response);
		
		
	}

	//gets all the books owned and reserved by user 
	private void myBooks(HttpServletRequest request, HttpServletResponse response) {

		Set<LibraryLogbook_Entry> entries = new HashSet<LibraryLogbook_Entry>();
		entries = LogBook_DB.searchEntry((String)request.getSession(false).getAttribute("username"), new String[] {"username"});
		SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd");
		
		ArrayList<String> dueDates = new ArrayList<>();
		
		for(LibraryLogbook_Entry temp : entries ) {
			dueDates.add(sdf.format(temp.getDueDate()));
		}
		
		RequestDispatcher dispatcher = request.getRequestDispatcher("ListmyBooks.jsp");
		//checking if the person can renew his books
		ArrayList<Boolean> canRenew = canAct(entries);
		request.setAttribute("entries", entries);
		request.setAttribute("dueDates", dueDates);
		request.setAttribute("canRenew", canRenew);
		
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

	private ArrayList<Boolean> canAct(Set<LibraryLogbook_Entry> entries) {
		
		ArrayList<Boolean>	can = new ArrayList<Boolean>();
		java.util.Date today = new java.util.Date();
		
		for(LibraryLogbook_Entry temp : entries) {
			long dueDateHowFarAway = temp.getDueDate().getTime() - today.getTime();
			if (dueDateHowFarAway<=(24*60*60*1000)) {
				can.add(true);
			}
			else
				can.add(false);
		}
		return can;
	}

	private void checkoutBook(HttpServletRequest request, HttpServletResponse response) {
		
		//checking if the book is available 
		Book theBook = Books_DB.get_book_whose_title(request.getParameter("title"));
		
		//checking if the number of books owned by a user is less than 5
		int totalBooks=LogBook_DB.checkMyTotalBooks((String)request.getSession(false).getAttribute("username"));
		//checking if user has got that book already
		
		if(totalBooks<5 && theBook.getCopiesAvail()>0 &&  
				LogBook_DB.checkForBook((String)request.getSession(false).getAttribute("username"),request.getParameter("title"))) {
			
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
			LogBook_DB.entry(new LibraryLogbook_Entry((String)request.getSession(false).getAttribute("username"),
					request.getParameter("title"),"checkout",date,0,"NA","NA"));
			
			request.setAttribute("actionStatus",(String)"the Book "+request.getParameter("title")+" has been Checked out successfully");
						
			//calling the view books function
			viewBooks(request,response);
			
		}	
		else if(!LogBook_DB.checkForBook((String)request.getSession(false).getAttribute("username"),request.getParameter("title"))) {
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
				//checking user's authority to checkout and reserve a book
				List<Boolean> canAct =  doesPossess(books,request,response);
				request.setAttribute("canAct", canAct);
				
				//dispatching the request and response 
				try {
					dispatcher.forward(request, response);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
		
	}
	
	//method to check if the user has the credibility to checkout or reserve the book
		private ArrayList<Boolean> doesPossess(ArrayList<Book> books, HttpServletRequest request, HttpServletResponse response) {
			ArrayList<Boolean> canAct = new ArrayList<Boolean>();
			
			for(Book temp : books) {
				if(LogBook_DB.getEntry((String)request.getSession(false).getAttribute("username"),temp.getTitle()) == null) {
					canAct.add(true);
				}else
					canAct.add(false);
			}

			return canAct;
		}
	

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
