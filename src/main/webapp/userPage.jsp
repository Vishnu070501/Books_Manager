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

<form action="ControllerServlet" method="POST">
<input type="hidden" name="command" value="VIEWBOOKS" />
<input type="submit" value="View Books"/>
</form>

<form action="ControllerServlet" method="POST">
<input type="hidden" name="command" value="MYBOOKS" />			
<input type="submit" value="My Books"/>
</form>

<form action="ControllerServlet" method="POST">
<input type="hidden" name="command" value="LOADUSER" />
<input type="hidden" name="username" value="${username}" />
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