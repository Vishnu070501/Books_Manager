package com.VishnuKurup.books_manager;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.VishnuKurup.books_manager.containers.Account;
import com.VishnuKurup.books_manager.containers.LibraryLogbook_Entry;
import com.VishnuKurup.books_manager.dbUtils.Accounts_DB_Util;
import com.VishnuKurup.books_manager.dbUtils.LibraryLogbook_DB_Util;

/**
 * Servlet implementation class manageUsersServlet
 */
@WebServlet("/manageUsersServlet")
public class manageUsersServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//Accessor of the accounts datasource
		private Accounts_DB_Util Accounts_DB = new Accounts_DB_Util();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public manageUsersServlet() {
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
					
				case "ADDUSER":
					//to add a user
					addUser(request,response);
					break;
					
				default:
					//takes to the login Page
					RequestDispatcher dispatcher2 = request.getRequestDispatcher("login_page.jsp");
					dispatcher2.forward(request, response);
					break;
					
				}
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
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
		
		//also all his entries has to be updated
		LibraryLogbook_DB_Util logbook = new LibraryLogbook_DB_Util();
		
		Set<LibraryLogbook_Entry> entries = logbook.searchEntry(request.getParameter("oldUsername"),new String[]{"username"});

		for(LibraryLogbook_Entry temp : entries) {
	
			temp.setUsername(request.getParameter("username"));
			logbook.updateEntry(request.getParameter("oldUsername"), temp.getTitle(), temp);
		}
		
		//changing the usernames in the reserved for column
		Set<LibraryLogbook_Entry> entriesByReservedFor = logbook.searchEntry(request.getParameter("oldUsername"),new String[]{"reservedFor"});
		
		for(LibraryLogbook_Entry temp : entriesByReservedFor) {
			temp.setReservedFor(request.getParameter("username"));
			logbook.updateEntry(temp.getUsername(), temp.getTitle(), temp);
		}
		
		//changing the usernames in the reserved from collum
		Set<LibraryLogbook_Entry> entriesByReservedFrom = logbook.searchEntry(request.getParameter("oldUsername"), new String[]{"reservedFrom"});
		
		for(LibraryLogbook_Entry temp : entriesByReservedFrom) {
			temp.setReservedFrom(request.getParameter("username"));
			logbook.updateEntry(temp.getUsername(), temp.getTitle(), temp);
		}		
		
		//showing the new list to librarian
		if(request.getSession(false).getAttribute("userType").equals("librarian") && request.getParameter("myself").equals("false")) {
			viewUsers(request,response);
		}
		
		else if(request.getSession(false).getAttribute("userType").equals("librarian") && request.getParameter("myself").equals("true")) {
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
		String userName = request.getParameter("Username");		
		 if(userName == null) {
			 userName = (String)request.getSession(false).getAttribute("loginServletUsername");
		 }
	
		//get the information on that Book from the db using the book db util
		Account theAccount= Accounts_DB.getUserWhoseUsername(userName);
		
		if( !(null ==request.getSession(false)) ) {
			//attach those information to the request object and dispatching it to the admin update form if admin updates
			if(request.getSession(false).getAttribute("userType").equals("librarian") && !userName.equals(request.getSession(false).getAttribute("username"))) {
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
		//if someone  is illegally acessing the page
		else {
			HttpSession session = request.getSession();
			session.setAttribute("servletName", "/manageUsersServlet");
			session.setAttribute("loginServletCommand" , "LOADUSER");
			session.setAttribute("loginServletUsername", userName);
			try {
				request.getRequestDispatcher("login_page.jsp").forward(request, response);
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
