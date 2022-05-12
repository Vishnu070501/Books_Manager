<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!-- only admins have access to this page where he can perform all the CRUD operations  -->
<html>

<head>
<title>Books Available</title>
<link type="text/css" rel="stylesheet" href="css/style.css"/>
</head>

<body>


<div id = "Wrapper">

<div id = "Header">
Books

<input type="button" value="Add Book"
onclick="window.location.href='add-book-form.jsp'"
class="add-student-button"/>

</div>

</div>


<div id ="Container">
<div id ="Content">
<Table Border="1">
<tr>
<th>Title</th><th>Author</th><th>Subject</th><th>Publication Date</th><th>Action</th>
</tr>
<c:forEach var="temp" items="${myBooks}">

<!-- Update links for each of the fields -->
<c:url var="templink" value="ControllerServlet">
<c:param name="command" value="LOAD"/>
<c:param name="title" value="${temp.title}"/>
</c:url>

<!-- Delete links for each of the fields -->
<c:url var="dellink" value="ControllerServlet">
<c:param name="command" value="DELETE"/>
<c:param name="title" value="${temp.title}"/>
</c:url>


<tr>
<td>${ temp.title}</td>
<td>${ temp.author}</td>
<td>${ temp.subject}</td>
<td>${ temp.publication_date}</td>
<td><a href="${templink}">Update</a>|
<a href="${dellink}"
onclick="(!(confirm('Do you sure you want to delete this book ?'))) return false">
Delete</a> </td>
</tr>
</c:forEach>
</Table>

<br/><br/>
<a href="ControllerServlet">Log Out</a>

<br/><br/>
<a href="search_form.jsp?usertype=admin">click here to search</a>



</div>

</div>

</body>

</html>