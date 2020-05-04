<!DOCTYPE html>
<html lang="en" xmlns="http://www.w3.org/1999/xhtml"
	xmlns:th="http://www.thymeleaf.org">
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<head>
<style>
.openDiv {
	display: block;
	width: 100%;
	height: 400px;
	overflow: scroll;
}
</style>
<link href="${pageContext.request.contextPath}/css/bootstrap.css"
	rel="stylesheet" />
<link href="${pageContext.request.contextPath}/css/style.css"
	rel="stylesheet" />
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.4.1/jquery.min.js"></script>
</head>
<script>

	$(function() {
	  $('#userForm').submit(function(){
		  
		console.log("Form Validation")
	    var password = $("#password").val();
		var username = $("#username").val()
	    
	    console.log(password, username)
	    
	    if (password === '' || username==='') {
	     	alert("Username or password should not be empty..!")
	        return false;
	     }
	  });       
	});
</script>
<body>
	<div class="container">

		<div class="row">
			<div class="col-sm-3"></div>
			<div class="col-sm-6 loginForm">
				<c:if test="${not empty msg }">
					<div class="alert alert-danger" role="alert">
						<c:out value="${msg}" />
					</div>
				</c:if>
				<c:if test="${not empty successMsg }">
					<div class="alert alert-success" role="alert">
						<c:out value="${successMsg}" />
					</div>
				</c:if>
				<h1>Login</h1>
				<form:form action="validate" method="post" modelAttribute="user"
					id="userForm">
					<div class="row form-group">
						<div class="col-sm-12 ">
							<label>User Name</label>
						</div>
						<div class="col-sm-12">
							<form:input class='form-control' id="username" type="text" path="username" />
							<c:if test="${not empty userNameErrorMsg}">
								<div class="alert alert-danger" role="alert">
									<c:out value="${userNameErrorMsg}" />
								</div>
							</c:if>
						</div>
					</div>
					<div class="row form-group">
						<div class="col-sm-12">
							<label>Password</label>
						</div>
						<div class="col-sm-12">
							<form:input class='form-control' id="password" type="password" path="password" />
							<c:if test="${not empty passwordMismatch}">
								<div class="alert alert-danger" role="alert">
									<c:out value="${passwordMismatch}" />
								</div>
							</c:if>
						</div>
					</div>
					<div class="row form-group">
						<div class="col-sm-12">
							<label>Select Product</label>
						</div>
						<div class="col-sm-12">
							<form:select path="product">
								<form:option value="Slonkit">Slonkit</form:option>
								<form:option value="Moneykit">Moneykit</form:option>
							</form:select>
						</div>
					</div>
					<div class="row form-group">
						<div class="col-sm-12">
							<input type="submit" class="btn btn-default" value="Login">
							<!-- <a href="getRegisterForm" class="btn btn-default" value="Registration">Sign Up</a> -->
						</div>
					</div>
				</form:form>
			</div>
		</div>
		<div></div>
</body>
</html>


