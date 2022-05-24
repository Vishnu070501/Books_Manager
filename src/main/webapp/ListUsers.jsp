<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!DOCTYPE html>
<html>
<head>
<title>Users Registered</title>
<link type="text/css" rel="stylesheet" href="css/style.css"/>
</head>
<body>


<div id = "Wrapper">

<div id = "Header">
Users    

<form action = "manageUsersServlet" method="POST">
<input type="hidden" name="command" value="SEARCHUSER"/>
Search Users:<input type="text" name="searchText" placeholder="enter search text"/>
<input type="submit" value="search"/>
<label>username<input type="checkbox" name="searchBy" value="username"/></label>
<label>name<input type="checkbox" name="searchBy" value="name"/></label>
<label>email<input type="checkbox" name="searchBy" value="email"/></label>
<label>qualification<input type="checkbox" name="searchBy" value="qualification"/></label>
</form>
</div>

</div>

<br/>
<br/>

<div id ="Container">
<div id ="Content">
<input type="button" value="Add User"
onclick="window.location.href='addUserForm.jsp'"
class="add-student-button"/>
<Table Border="1">
<tr>
<th>Username</th><th>Name</th><th>Email</th><th>Educational Qualification</th><th>Action</th>
</tr>
<c:forEach var="temp" items="${myUsers }">
<!-- Update links for each of the fields -->
<!-- this is the tag that allows you to create you automatic links that go to a certain place with the parameter set -->
<c:url var="templink" value="manageUsersServlet">
<c:param name="command" value="LOADUSER"/>
<c:param name="username" value="${temp.username}"/>
</c:url>
<!-- Delete links for each of the fields -->
<c:url var="dellink" value="manageUsersServlet">
<c:param name="command" value="DELETEUSER"/>
<c:param name="username" value="${temp.username}"/>
</c:url>
<tr>
<td>${temp.username}</td>
<td>${temp.name}</td>
<td>${temp.email}</td>
<td>${temp.qualification}</td>
<td>
<form action="manageUsersServlet" method="POST">
<input type="hidden" name="command" value="LOADUSER"/>
<input type="hidden" name="username" value="${temp.username}"/>
<input type="submit" value="update" onclick="return confirm('Do you want to update the details of the user ${temp.username}?')"/> 
</form>
<form action="manageUsersServlet" method="POST">
<input type="hidden" name="command" value="DELETEUSER"/>
<input type="hidden" name="username" value="${temp.username}"/>
<input type="submit" value="delete" onclick="return confirm('Do you want to delete the user ${temp.username}?')"/> 
</form>

</tr>
</c:forEach>
</Table>

<br/>

<form action="ControllerServlet" method="POST">
<input type="hidden" name="command" value="LIBRARIANPAGE"/>
<input type="submit" value="click here to go back to mainPage"/>
</form>

<form action="manageUsersServlet" method="POST">
<input type="hidden" name="command" value="VIEWUSER"/>
<input type="submit" value="click here to view all users"/>
</form>
<br/>
</div>
</div>
</body>
</html>