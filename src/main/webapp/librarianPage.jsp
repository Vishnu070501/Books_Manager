<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- This is a no permissions page for a normal user where he cannot perform anything just can 
look at the books that are available  -->
<html>

<head>
<title>Librarian Page</title>
<link type="text/css" rel="stylesheet" href="css/style.css"/>
</head>

<body>


<div id = "Wrapper">

<div id = "Header">
Librarian's Page
<p align = "center">${actionStatus } </p>
</div>

</div>

<div id ="Container">
<div id ="Content">

<form action="manageUsersServlet" method="POST">
<input type="hidden" name="command" value="VIEWUSER"/>
<input type="submit" value="View and Manage Users"/>
</form>

<form action="manageBooksServlet" method="POST">
<input type="hidden" name="command" value="VIEWBOOKS"/>
<input type="submit" value="View and Manage Books"/>
</form>
<% String username=(String)session.getAttribute("username");%>
<form action="manageUsersServlet" method="POST">
<input type="hidden" name="command" value="LOADUSER"/>
<input type="hidden" name="username" value="${username}"/>
<input type="submit" value="To Manage Your Details"/>
</form>

<form action="ControllerServlet" method="POST">
<input type="hidden" name="command" value="LOGOUT" />			
<input type="submit" value="Logout"/>
</form>
</div>

</div>

</body>

</html>