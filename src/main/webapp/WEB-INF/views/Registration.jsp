<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">

<style>
.error {
 color: #ff0000;
 font-style: italic;
	font-weight: bold;
}

</style>

</head>
      <link href="${pageContext.request.contextPath}/css/bootstrap.css"
	rel="stylesheet" />
   
<body>
<div class="container">

	<form:form class="form" action="register-user" method="post" modelAttribute="user">
	<table class="table">
		<caption><h2>Student Registration Form</h2></caption>
			
			<tr>
				<td><h4><b>First Name:</b></h4></td>
				<td><form:input class='form-control' type="text" path="firstname"/></td>
				<td><form:errors path="firstname" cssClass="error"/></td>
			</tr>
			<tr>
				<td><h4><b>Last Name:</b></h4></td>
				<td><form:input class='form-control' path="lastname"/></td>
				<td><form:errors path="lastname" cssClass="error"/></td>
			</tr>
			<tr>
				<td><h4><b>User Name:</b>	</h4></td>
				<td><form:input class='form-control' path="username" /></td>
				<td><form:errors path="username" cssClass="error"/></td>
			</tr>
			<tr>
				<td><h4><b>Password:</b></h4></td>
				<td><form:input type="password"  class='form-control' path="password"/></td>
				<td><form:errors path="password" cssClass="error"/></td>
			</tr>
			<tr>
				<td colspan="2" >
				<div style="text-align:center" >
				<input class="btn btn-primary btn-lg btn-block" type="submit" value="Submit">
				</div>
				</td>
			</tr>
		</table>
	</form:form>
</div>
	 <div>
             <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
             <script src="${pageContext.request.contextPath}/js/jquery.serializejson.js"></script>
			<script src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
        </div>

</body>
</html>