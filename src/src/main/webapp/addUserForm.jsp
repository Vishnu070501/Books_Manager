<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!DOCTYPE html>
<html>

<head>
	<title>Add User</title>

	<link type="text/css" rel="stylesheet" href="css/style.css">
	<link type="text/css" rel="stylesheet" href="css/add-student-style.css">	
</head>

<body>
<% String userType = (String)session.getAttribute("userType"); %>
	<div id="wrapper">
		<div id="header">
			<c:if test="${userType==\"librarian\" }"><h2>Add User</h2></c:if>
			<c:if test="${ param.user == \"yes\" }"><h2>User Signup</h2></c:if>
		</div>
	</div>
	
	<div id="container">
		<c:if test="${userType==\"librarian\" }"><h3>Add User</h3></c:if>
		<c:if test="${ param.user== \"yes\" }"><h3>User Signup</h3></c:if>
		
		<form action="ControllerServlet" method="POST">
		
			<input type="hidden" name="command" value="ADDUSER" />
			<c:if test="${userType!=\"librarian\"}">
						<%session.setAttribute("userType", "user"); %>
			</c:if>
			
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











