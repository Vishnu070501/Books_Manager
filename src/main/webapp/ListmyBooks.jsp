<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.text.*" %>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!DOCTYPE html>
<html>
<head>
<title>My Books</title>
<link type="text/css" rel="stylesheet" href="css/style.css"/>
</head>
<body>


<div id = "Wrapper">

<div id = "Header">
Books    

<form action = "ControllerServlet" method="POST">
<input type="hidden" name="command" value="SEARCHENTRY"/>
Search Books:<input type="text" name="searchText" placeholder="enter search text"/>
<label>username<input type="checkbox" name="searchBy" value="username"/></label>
<label>book title<input type="checkbox" name="searchBy" value="title"/></label>

<input type="submit" value="search"/>
</form>
</div>

</div>

<br/>
<br/>

<div id ="Container">
<div id ="Content">
<Table Border="1">
<tr>

<th>Title</th><th>Status</th><th>Due Date</th><th>Action</th><th>Renewal Count(Max 2)</th><th>Reserved For</th><th>Reserved From</th>
</tr>
<c:forEach var="temp" items="${entries }" varStatus="status">

<tr>
<td>${temp.title}</td>
<td>${temp.action}</td>
<td>${dueDates.get(status.index) }</td>

<!-- allow renewal and returning of checked out books -->
<c:if test="${temp.action.equals(\"checkout\") }">
<td>
<form action="ControllerServlet" method="POST">
<input type="hidden" name="command" value="RENEWBOOK"/>
<input type="hidden" name="title" value="${temp.title}"/>
<input type="submit" value="renew" onclick="return confirm('Do you want to renew the due date for the book ${temp.title}?')"/> 
</form>
<form action="ControllerServlet" method="POST">
<input type="hidden" name="command" value="RETURNBOOK"/>
<input type="hidden" name="title" value="${temp.title}"/>
<input type="submit" value="return" onclick="return confirm('Do you want to return the book ${temp.title}?')"/> 
</form>
</td> 
</c:if>

<!--  allow unreserving of books if reserved Books -->
<c:if test="${temp.action.equals(\"reserve\") }">
<td>
<form action="ControllerServlet" method="POST">
<input type="hidden" name="command" value="UNRESERVEBOOK"/>
<input type="hidden" name="title" value="${temp.title}"/>
<input type="submit" value="return" onclick="return confirm('Do you want to unreserve the book ${temp.title}?')"/> 
</form>
</td> 
</c:if>
<td>${temp.renewalCount }</td>
<td>${temp.reservedFor }</td>
<td>${temp.reservedFrom }</td>
</tr>
</c:forEach>
</Table>

<br/>
<c:if test="${userType.equals(\"librarian\") }">
<form action="ControllerServlet" method="POST">
<input type="hidden" name="command" value="LIBRARIANPAGE"/>
<input type="submit" value="click here to go back to mainPage"/>
</form>
</c:if>

<c:if test="${userType.equals(\"user\") }">
<form action="ControllerServlet" method="POST">
<input type="hidden" name="command" value="USERPAGE"/>
<input type="submit" value="click here to go back to mainPage"/>
</form>
<form action="ControllerServlet" method="POST">
<input type="hidden" name="command" value="MYBOOKS" />			
<input type="submit" value="Click here to view all Books"/>
</form>
</c:if>
<br/>
${checkoutStatus}


<br/>

${actionStatus}
</div>
</div>
</body>
</html>