<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>


<!DOCTYPE html>
<html>

<head>
	<title>Login Page</title>

	<link type="text/css" rel="stylesheet" href="css/style.css">
	<link type="text/css" rel="stylesheet" href="css/add-student-style.css">	
</head>

<body>
	<div id="wrapper">
		<div id="header">
			<h1>Welcome to Central Library</h1>
			<h2>Login Page</h2>
			<c:if test="${not empty invalid_cred}">
			Please Enter the Right Credentials
			</c:if>
			<c:if test="${not empty signedUp}">
			User has been successfully registered
			</c:if>
		</div>
	</div>
	
	<div id="container">
		<h3>Enter Your Credentials</h3>
		
		<form action="ControllerServlet" method="POST">
		
			<input type="hidden" name="command" value="LOG_CHECK" />			
			
			<table>
				<tbody>
					<tr>
						<td><label>UserName:</label></td>
						<td><input type="text" name="username" placeholder="Enter username"/></td>
					</tr>
					
					<tr>
						<td><label>Password:</label></td>
						<td><input type="password" name="password" placeholder="Enter Password" /></td>
					<tr>
						<td><label></label></td>
						<td><input type="submit" value="login" class="save" /></td>
					</tr>
					
				</tbody>
			</table>
		</form>
		<div style="clear: both;"></div>
		
		<p>
		<a href="addUserForm.jsp?user=yes">Click Here To Signup.</a>
		</p>
	</div>
</body>

</html>











