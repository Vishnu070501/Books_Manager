<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<%@ page import="java.text.*" %>
    
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!DOCTYPE html>
<html>
<head>
<title>All Books</title>
<link type="text/css" rel="stylesheet" href="css/style.css"/>
</head>
<body>


<div id = "Wrapper">

<div id = "Header">
Books    
<%
session.setAttribute("servletName", "/manageBooksServlet");
session.setAttribute("loginServletCommand" , "VIEWBOOKS");
if ((String)session.getAttribute("username") == null || (String)session.getAttribute("userType") == null){
	response.sendRedirect("login_page.jsp");
}
%>
<form action = "manageBooksServlet" method="GET">
<input type="hidden" name="command" value="SEARCHBOOKS"/>
Search Books:<input type="text" name="searchText" placeholder="enter search text"/>
<input type="submit" value="search"/>
<label>title<input type="checkbox" name="searchBy" value="title"/></label>
<label>author<input type="checkbox" name="searchBy" value="author"/></label>
<label>subject<input type="checkbox" name="searchBy" value="subject"/></label>
</form>
</div>

</div>

<br/>
<br/>

<div id ="Container">
<div id ="Content">
<c:if test="${userType.equals(\"librarian\") }">
<input type="button" value="Add Book"
onclick="window.location.href='addBookForm.jsp'"
class="add-student-button"/>
</c:if>

<Table Border="1">
<tr>
<% String userType = (String)session.getAttribute("userType"); %>

<th>Title</th><th>Author</th><th>Subject</th><th>Available Copies</th><th>Publication Date(yyyy-mm-dd)</th><th>Action</th>
<!--<c:if test="${userType.equals(\"user\") }"><th>Availablility Date</th></c:if>--></tr>
<c:forEach var="temp" items="${myBooks }" varStatus="status">
<tr>
<td>${temp.title}</td>
<td>${temp.author}</td>
<td>${temp.subject}</td>
<td>${temp.copiesAvail}</td>
<td>${publicationDates.get(status.index) }</td>

<!-- if librarian allow updates and deletes -->
<c:if test="${userType.equals(\"librarian\") }">
<td>
<form action="manageBooksServlet" method="GET">
<input type="hidden" name="command" value="LOADBOOK"/>
<input type="hidden" name="title" value="${temp.title}"/>
<input type="submit" value="update" onclick="return confirm('Do you want to update the book ${temp.title}?')"/> 
</form>
<form action="manageBooksServlet" method="GET">
<input type="hidden" name="command" value="DELETEBOOK"/>
<input type="hidden" name="title" value="${temp.title}"/>
<input type="submit" value="delete" onclick="return confirm('Do you want to delete the book ${temp.title}?')"/> 
</form>
</td>
</c:if>

<!-- if normal user allow checkouts and reserves -->
<c:if test="${userType.equals(\"user\") }">
<c:if test="${canAct.get(status.index) }">

<td>



<form action="manageEntriesServlet" method="GET">
<input type="hidden" name="command" value="CHECKOUTBOOK"/>
<input type="hidden" name="title" value="${temp.title}"/>
<input type="submit" value="checkout" onclick="return confirm('Do you want to checkout the book ${temp.title}?')"/> 
</form>


<c:if test="${temp.copiesAvail == 0 }">
<form action="manageEntriesServlet" method="GET">
<input type="hidden" name="command" value="RESERVEBOOK"/>
<input type="hidden" name="title" value="${temp.title}"/>
<input type="submit" value="reserve" onclick="return confirm('Do you want to reserve the book ${temp.title}?')"/>
</form>
</c:if>



</td>

</c:if>
</c:if>



</tr>
</c:forEach>
</Table>

<br/>
<c:if test="${userType.equals(\"librarian\") }">
<form action="ControllerServlet" method="GET">
<input type="hidden" name="command" value="LIBRARIANPAGE"/>
<input type="submit" value="click here to go back to mainPage"/>
</form>
</c:if>

<c:if test="${userType.equals(\"user\") }">
<form action="ControllerServlet" method="GET">
<input type="hidden" name="command" value="USERPAGE"/>
<input type="submit" value="click here to go back to mainPage"/>
</form>
</c:if>
<form action="manageBooksServlet" method="GET">
<input type="hidden" name="command" value="VIEWBOOKS"/>
<input type="submit" value="click here to view all books"/>
</form>

<br/>
${actionStatus}


<br/>
</div>
</div>
</body>
</html>