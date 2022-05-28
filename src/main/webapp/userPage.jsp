<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- This is a no permissions page for a normal user where he cannot perform anything just can 
look at the books that are available  -->
<html>

<head>
<title>User Page</title>
<link type="text/css" rel="stylesheet" href="css/style.css"/>
</head>

<body>

<%
if ((String)session.getAttribute("username") == null || (String)session.getAttribute("userType") == null){
	response.sendRedirect("login_page.jsp");
}
%>

<%
if(session.getAttribute("userType")!=null){
	if (session.getAttribute("userType").equals("librarian") ){
		request.setAttribute("invalid_cred","You are not authorised to view the page");
		request.getRequestDispatcher("login_page.jsp").forward(request, response);
	}
}
%>

<div id = "Wrapper">

<div id = "Header">
User's Page
<p align = "center">${actionStatus } </p>
</div>

</div>

<div id ="Container">
<div id ="Content">

<% String username=(String)session.getAttribute("username");%>

<% String userType=(String)session.getAttribute("userType");%>

<form action="manageBooksServlet" method="GET">
<input type="hidden" name="command" value="VIEWBOOKS" />
<input type="submit" value="View Books"/>
</form>

<form action="manageEntriesServlet" method="GET">
<input type="hidden" name="command" value="MYBOOKS" />			
<input type="submit" value="My Books"/>
</form>

<form action="manageUsersServlet" method="GET">
<input type="hidden" name="command" value="LOADUSER" />
<input type="hidden" name="Username" value="${username}" />
<input type="submit" value="To Manage Your Details"/>
</form>

<form action="ControllerServlet" method="GET">
<input type="hidden" name="command" value="LOGOUT" />			
<input type="submit" value="Logout"/>
</form>
</div>

</div>

</body>

</html>