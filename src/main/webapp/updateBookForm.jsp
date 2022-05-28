<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page import="java.text.*,com.VishnuKurup.books_manager.containers.*" %>

<!DOCTYPE html>
<html>

<head>
	<title>Update Book</title>

	<link type="text/css" rel="stylesheet" href="css/style.css">
	<link type="text/css" rel="stylesheet" href="css/add-student-style.css">	
</head>

<body>

<% 
	Book myBook1 = (Book)request.getAttribute("myBook"); 
   String titleForUrl = String.join("+", myBook1.getTitle().split(" "));   
   session.setAttribute("currentPage", "http://localhost:8181/books_manager/manageBooksServlet?command=LOADBOOK&title="+titleForUrl);
   if ((String)session.getAttribute("username") == null || (String)session.getAttribute("userType") == null){
   	response.sendRedirect("login_page.jsp");
   }
%>

<%
if(session.getAttribute("userType")!=null){
	if (!session.getAttribute("userType").equals("librarian") ){
		request.setAttribute("invalid_cred","You are not authorised to view the page");
		request.getRequestDispatcher("login_page.jsp").forward(request, response);
	}
}
%>

	<div id="wrapper">
		<div id="header">
			<h2>Update Book</h2>
		</div>
	</div>
	
	<div id="container">
		<h3>Update Book</h3>
		
		<form action="manageBooksServlet" method="GET">
		
			<input type="hidden" name="command" value="UPDATEBOOK" />
			<input type="hidden" name="oldTitle" value="${myBook.title }"/>
						
			
			<!-- this hidden command allows us to tell the controller servlet and tell
			what to do-->
			
			<table>
				<tbody>
					<tr>
						<td><label>Title:</label></td>
						<td><input type="text" name="title" value="${myBook.title }"/></td>
					</tr>

					<tr>
						<td><label>Author:</label></td>
						<td><input type="text" name="author" value="${myBook.author }"/></td>
					</tr>

					<tr>
						<td><label>Subject:</label></td>
						<td><input type="text" name="subject" value="${myBook.subject }"/></td>
					</tr>
					
					<tr>
						<td><label>Publication Date(DD/MM/YYY):</label></td>
						<td><input type="text" name="publicationDate" value="${publicationDate }"/></td>
					</tr>
					
					<tr>
						<td><label>Copies Available:</label></td>
						<td><input type="text" name="copiesAvail" value="${myBook.copiesAvail }"/></td>
					</tr>
				
					<tr>
						<td><label></label></td>
						<td><input type="submit" value="save" class="save" /></td>
					</tr>
					
				</tbody>
			</table>
		</form>
		
		<div style="clear: both;"></div>
		
		<p>
		
		</p>
	</div>
</body>

</html>











