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
		
		<form action="ControllerServlet" method="GET">
		
			<input type="hidden" name="command" value="UPDATE" />
			
			<input type="hidden" name="oldtitle" value="${ theBook.title}" />
			
			
			<!-- this hidden command allows us to tell the controller servlet and tell
			what to do-->
			
			<table>
				<tbody>
					<tr>
						<td><label>Title:</label></td>
						<td><input type="text" name="title" value="${theBook.title }"/></td>
					</tr>

					<tr>
						<td><label>Author:</label></td>
						<td><input type="text" name="author" value="${theBook.author }"/></td>
					</tr>

					<tr>
						<td><label>Subject:</label></td>
						<td><input type="text" name="subject" value="${theBook.subject }"/></td>
					</tr>
					
					<tr>
						<td><label>Publication Date:</label></td>
						<td><input type="text" name="publication_date" value="${theBook.publication_date }"/></td>
					</tr>
					
					<tr>
						<td><label></label></td>
						<td><input type="submit" value="Save" class="save" /></td>
					</tr>
					
				</tbody>
			</table>
		</form>
		
		<div style="clear: both;"></div>
		
		<p>
			<a href="ControllerServlet">Back to List</a>
		</p>
	</div>
</body>

</html>











