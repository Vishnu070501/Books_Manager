<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<!-- This is a no permissions page for a normal user where he cannot perform anything just can 
look at the books that are available  -->
<html>

<head>
<title>Books Available</title>
<link type="text/css" rel="stylesheet" href="css/style.css"/>
</head>

<body>


<div id = "Wrapper">

<div id = "Header">
Books
</div>

</div>


<div id ="Container">
<div id ="Content">
<Table Border="1">
<tr>
<th>Title</th><th>Author</th><th>Subject</th><th>Publication Date</th>
</tr>
<c:forEach var="temp" items="${myBooks}">



<tr>
<td>${ temp.title}</td>
<td>${ temp.author}</td>
<td>${ temp.subject}</td>
<td>${ temp.publication_date}</td>
</tr>
</c:forEach>
</Table>

<br/><br/>
<a href="login_page.jsp">Click Here for Admin Login</a>

<br/><br/>
<a href="search_form.jsp?usertype=normal">click here to search</a>



</div>

</div>

</body>

</html>