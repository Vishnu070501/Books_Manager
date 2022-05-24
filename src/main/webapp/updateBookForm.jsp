<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<%@ page import="java.text.*" %>

<!DOCTYPE html>
<html>

<head>
	<title>Update Book</title>

	<link type="text/css" rel="stylesheet" href="css/style.css">
	<link type="text/css" rel="stylesheet" href="css/add-student-style.css">	
</head>

<body>
	<div id="wrapper">
		<div id="header">
			<h2>Update Book</h2>
		</div>
	</div>
	
	<div id="container">
		<h3>Update Book</h3>
		
		<form action="manageBooksServlet" method="POST">
		
			<input type="hidden" name="command" value="UPDATEBOOK" />
			<input type="hidden" name="oldTitle" value="${myBook.title }"/>
						
			
			<!-- this hidden command allows us to tell the controller servlet and tell
			what to do-->
			
			<table>
				<tbody>
					<tr>
						<td><label>Title:</label></td>
						<td><input type="text" name="title" value="${myBook.title }"/></td>
					</tr>

					<tr>
						<td><label>Author:</label></td>
						<td><input type="text" name="author" value="${myBook.author }"/></td>
					</tr>

					<tr>
						<td><label>Subject:</label></td>
						<td><input type="text" name="subject" value="${myBook.subject }"/></td>
					</tr>
					
					<tr>
						<td><label>Publication Date(DD/MM/YYY):</label></td>
						<td><input type="text" name="publicationDate" value="${publicationDate }"/></td>
					</tr>
					
					<tr>
						<td><label>Copies Available:</label></td>
						<td><input type="text" name="copiesAvail" value="${myBook.copiesAvail }"/></td>
					</tr>
				
					<tr>
						<td><label></label></td>
						<td><input type="submit" value="save" class="save" /></td>
					</tr>
					
				</tbody>
			</table>
		</form>
		
		<div style="clear: both;"></div>
		
		<p>
		
		</p>
	</div>
</body>

</html>











