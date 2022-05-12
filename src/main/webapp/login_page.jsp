<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
<head>
<title>Welcome to the login page </title>
<link type="text/css" rel="stylesheet" href="css/style.css"/>
</head>
<body>

<c:if test="${not empty invalid_cred}">
Please Enter the Right Credentials
</c:if>

<form action = "ControllerServlet" method="GET">

<label for="Username">Username:</label>
<input type = "hidden" name="command" value="LOG_CHECK"/> 
<input id = "Username" type ="text" name="username" placeholder="username here"/>
<br/>
<label for = "Password">Password:</label>
<input id = "Password" type ="password" name = "password" placeholder="password here"/>

<input type ="submit" value="login"/>
<br/>
</form>

</body>
</html>