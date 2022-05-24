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
		
		if(cmd==null) {
			cmd="default";
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
			
		
		default:
			//takes to the login Page
			RequestDispatcher dispatcher2 = request.getRequestDispatcher("login_page.jsp");
			dispatcher2.forward(request, response);
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
