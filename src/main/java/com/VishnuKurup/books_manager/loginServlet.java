package com.VishnuKurup.books_manager;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.VishnuKurup.books_manager.containers.Account;
import com.VishnuKurup.books_manager.dbUtils.Accounts_DB_Util;

/**
 * Servlet implementation class loginServlet
 */
@WebServlet("/loginServlet")
public class loginServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	
	//Accessor of the accounts datasource
		private Accounts_DB_Util Accounts_DB = new Accounts_DB_Util();
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public loginServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {


		String cmd = request.getParameter("command");
		
		if(null==cmd) {
			cmd = "default";
		}
		
		switch(cmd) {
		
		case "LOG_CHECK":
			//checking credentials
			logCheck(request,response);
			break;
			
		default:
			//takes to the login Page
			RequestDispatcher dispatcher2 = request.getRequestDispatcher("login_page.jsp");
			dispatcher2.forward(request, response);
			break;
		}
	}
	
	private void logCheck(HttpServletRequest request, HttpServletResponse response) {
		
		Account theAcct = new Account(request.getParameter("username"),
				request.getParameter("password"),null,null,null,null);//creating an account instance to store credentials
		
		//calling the account checking function in accounts util database 
		if(Accounts_DB.logCheck(theAcct)) {
			
			//updates the logbook on a daily bases 
			
			Thread dailyUpdate = new LogBookDailyUpdate();
			dailyUpdate.start();
			
			theAcct =Accounts_DB.getUserWhoseUsername(request.getParameter("username"));
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
