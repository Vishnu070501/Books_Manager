<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
	<title>Search Books</title>

	<link type="text/css" rel="stylesheet" href="css/style.css">
	<link type="text/css" rel="stylesheet" href="css/add-student-style.css">	
</head>


<body>
<form action = "ControllerServlet" method="GET">
<!-- gives the command and the user type -->
<input type="hidden" name="command" value="SEARCH"/>
<input type="hidden" name="usertype" value="${param.usertype}"/>
<label for="Search Text">
Enter Search Text:
</label>
<input type="text" id="Search Text" name="search_text"/>
<br/>
Search By:
<label for = "title">Title</label>
<input id = "title" type="radio" name="search_by" value ="title"/>
<br/>
<label for = "author">Author</label>
<input id = "author" type="radio" name="search_by" value ="author"/>
<br/>
<label for = "subject">Subject</label>
<input id = "subject" type="radio" name="search_by" value ="subject"/>
<br/>
<label for = "publication_date">Publication Date</label>
<input id = "publication_date" type="radio" name="search_by" value ="publication_date"/> 

<input type="submit" value="search"/>
</form>
</body>


</html>