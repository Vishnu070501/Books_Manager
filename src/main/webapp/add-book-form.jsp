<!DOCTYPE html>
<html>

<head>
	<title>Add Student</title>

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
		
		<form action="ControllerServlet" method="GET">
		
			<input type="hidden" name="command" value="ADD" />
			<!-- this hidden command allows us to tell the controller servlet and tell
			what to do-->
			
			<table>
				<tbody>
					<tr>
						<td><label>Title:</label></td>
						<td><input type="text" name="title" /></td>
					</tr>

					<tr>
						<td><label>Author Name:</label></td>
						<td><input type="text" name="author" /></td>
					</tr>

					<tr>
						<td><label>Subject:</label></td>
						<td><input type="text" name="subject" /></td>
					</tr>
					
					<tr>
						<td><label>Publication Date:</label></td>
						<td><input type="text" name="publication_date" /></td>
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











