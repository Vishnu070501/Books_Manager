<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!DOCTYPE html>
<html>

<head>
	<title>Add Book</title>

	<link type="text/css" rel="stylesheet" href="css/style.css">
	<link type="text/css" rel="stylesheet" href="css/add-student-style.css">	
</head>

<body>
	<div id="wrapper">
		<div id="header">
			<h2>Add Book</h2>
		</div>
	</div>
	
	<div id="container">
		<h3>Add Book</h3>
		
		<form action="manageBooksServlet" method="POST">
		
			<input type="hidden" name="command" value="ADDBOOK" />
						
			
			<!-- this hidden command allows us to tell the controller servlet and tell
			what to do-->
			
			<table>
				<tbody>
					<tr>
						<td><label>Title:</label></td>
						<td><input type="text" name="title" /></td>
					</tr>

					<tr>
						<td><label>Author:</label></td>
						<td><input type="text" name="author" /></td>
					</tr>

					<tr>
						<td><label>Subject:</label></td>
						<td><input type="text" name="subject" /></td>
					</tr>
					
					<tr>
						<td><label>Publication Date(DD/MM/YYYY):</label></td>
						<td><input type="text" name="publicationDate" /></td>
					</tr>
					
					<tr>
						<td><label>Copies Available:</label></td>
						<td><input type="text" name="copiesAvail" /></td>
					</tr>
				
					<tr>
						<td><label></label></td>
						<td><input type="submit" value="add" class="save" /></td>
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











