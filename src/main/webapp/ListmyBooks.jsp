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
<%
session.setAttribute("servletName", "/manageEntriesServlet");
session.setAttribute("loginServletCommand" , "MYBOOKS");
if (	null == (String)session.getAttribute("username")  || null == (String)session.getAttribute("userType")){
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
Books    

<form action = "manageEntriesServlet" method="GET">
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
<c:if test="${temp.action != 'returned' }">
<tr>
<td>${temp.title}</td>
<td>${temp.action}</td>
<!-- should not display all the rest data if the book has been returned -->

<td>${dueDates.get(status.index) }</td>

<!-- allow renewal and returning of checked out books -->
<c:if test="${temp.action.equals(\"checkout\") }">
<td>

<c:if test="${canRenew.get(status.index)}">
<form action="manageEntriesServlet" method="GET">
<input type="hidden" name="command" value="RENEWBOOK"/>
<input type="hidden" name="title" value="${temp.title}"/>
<input type="submit" value="renew" onclick="return confirm('Do you want to renew the due date for the book ${temp.title}?')"/> 
</form>
</c:if>

<form action="manageEntriesServlet" method="GET">
<input type="hidden" name="command" value="RETURNBOOK"/>
<input type="hidden" name="title" value="${temp.title}"/>
<input type="submit" value="return" onclick="return confirm('Do you want to return the book ${temp.title}?')"/> 
</form>
</td> 
</c:if>

<!--  allow unreserving of books if reserved Books -->
<c:if test="${temp.action.equals(\"reserve\") }">
<td>
<form action="manageEntriesServlet" method="GET">
<input type="hidden" name="command" value="UNRESERVEBOOK"/>
<input type="hidden" name="title" value="${temp.title}"/>
<input type="submit" value="unreserve" onclick="return confirm('Do you want to unreserve the book ${temp.title}?')"/> 
</form>
<form action="manageEntriesServlet" method="GET">
<input type="hidden" name="command" value="CHECKOUTRESERVEDBOOK"/>
<input type="hidden" name="title" value="${temp.title}"/>
<input type="submit" value="checkout" onclick="return confirm('Do you want to unreserve the book ${temp.title}?')"/> 
</form>
</td> 
</c:if>
<td>${temp.renewalCount }</td>
<td>${temp.reservedFor }</td>
<td>${temp.reservedFrom }</td>
</tr>
</c:if>
</c:forEach>
</Table>

<br/>


<c:if test="${userType.equals(\"user\") }">
<form action="ControllerServlet" method="GET">
<input type="hidden" name="command" value="USERPAGE"/>
<input type="submit" value="click here to go back to mainPage"/>
</form>
<form action="manageEntriesServlet" method="GET">
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