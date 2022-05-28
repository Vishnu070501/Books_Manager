<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!DOCTYPE html>
<html>

<head>
	<title>User Signup</title>

	<link type="text/css" rel="stylesheet" href="css/style.css">
	<link type="text/css" rel="stylesheet" href="css/add-student-style.css">	
</head>

<body>

	<div id="wrapper">
		<div id="header">
			<h2>User Signup</h2>
		</div>
	</div>
	
	<div id="container">
		<h3>User Signup</h3>
		
		<form action="manageUsersServlet" method="GET">
		
			<input type="hidden" name="command" value="ADDUSER" />
			
			<!-- this hidden command allows us to tell the controller servlet and tell
			what to do-->
			
			<table>
				<tbody>
					<tr>
						<td><label>UserName:</label></td>
						<td><input type="text" name="username" /></td>
					</tr>

					<tr>
						<td><label>Password:</label></td>
						<td><input type="password" name="password" /></td>
					</tr>

					<tr>
						<td><label>Email:</label></td>
						<td><input type="text" name="email" /></td>
					</tr>
					
					<tr>
						<td><label>Qualification:</label></td>
						<td><input type="text" name="qualification" /></td>
					</tr>
					
					<tr>
						<td><label>Name of the User:</label></td>
						<td><input type="text" name="name" /></td>
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











