package com.VishnuKurup.books_manager;

import java.io.IOException;
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
		
		switch (cmd) {
			
			
		
			
		case "USERPAGE":
			RequestDispatcher userDispatcher = request.getRequestDispatcher("userPage.jsp");
			userDispatcher.forward(request, response);
			break;
			
		case "LIBRARIANPAGE":
			RequestDispatcher librarianDispatcher = request.getRequestDispatcher("librarianPage.jsp");
			librarianDispatcher.forward(request, response);
			break;
			
		case "LOGOUT":
			request.getSession().invalidate();
			RequestDispatcher login = request.getRequestDispatcher("login_page.jsp");
			login.forward(request, response);
			break;
		
		default:
			//takes to the login Page
			response.sendRedirect("login_page.jsp");
			break;
		
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
