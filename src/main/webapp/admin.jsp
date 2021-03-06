<%@ taglib prefix="spring" uri="http://www.springframework.org/tags" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:set var="contextPath" value="${pageContext.request.contextPath}"/>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>Create an account</title>

    <link href="${contextPath}/resources/css/bootstrap.min.css" rel="stylesheet">
    <link href="${contextPath}/resources/css/common.css" rel="stylesheet">

    <!-- HTML5 shim and Respond.js for IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
    <script src="https://oss.maxcdn.com/html5shiv/3.7.2/html5shiv.min.js"></script>
    <script src="https://oss.maxcdn.com/respond/1.4.2/respond.min.js"></script>
    <![endif]-->
</head>
<body>
<div>
	<h2 align="center" width="100%"><c:out value="${message}"></c:out></h2>
</div>
<div class="container">

    <c:if test="${pageContext.request.userPrincipal.name != null}">
        <form id="logoutForm" method="POST" action="${contextPath}/logout">
            <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}"/>
        </form>

        <h2>Welcome ${pageContext.request.userPrincipal.name} | <a onclick="document.forms['logoutForm'].submit()">Logout</a> | <a href="${contextPath}/changepassword">Change password</a></h2>
    </c:if>
    
    
    <table border="1" class="table">
    	<col width="150">
  		<col width="200">
  		<col width="200">
    	<thead>
    		<tr align="center">
	    		<td>User</td>
	    		<td>Password Reset</td>
	    		<td> Unlock Account</td>
    		</tr>
    	</thead>
    	<tbody >
	     <c:forEach items="${users}" var="user">
	    	<tr align="center">
	          <td class="table_data" ><c:out value="${user.username}" /></td>
	          <td><a href="${contextPath}/resetpassword/${user.username}" >reset </a> </td>
	          <td><a href="${contextPath}/accountunlock/${user.username}" >unlock</a> </td>
	        </tr>
	    </c:forEach>
	    </tbody> 
	</table>
</div>
<!-- /container -->
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="${contextPath}/resources/js/bootstrap.min.js"></script>
</body>
</html>
