<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1" isELIgnored="false"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="ISO-8859-1">
<title>Insert title here</title>
</head>
<head>

<link href="${pageContext.request.contextPath}/css/bootstrap.css"
	rel="stylesheet" />
</head>


<body>
	<div class="container">
		<div class="row">
			<h1>Reports</h1>
		</div>
		<div class="row">
			<h3>Reports For ${prod}</h3>
		</div>
	</div>
	<form:form id="formSubmit" method="POST" action=""
		modelAttribute="report">
		<table>
			<tr>
				<td><form:select class='form-control' path="prodName">
						<form:option value="Slonkit">Slonkit</form:option>
						<form:option value="Moneykit">Moneykit</form:option>
					</form:select></td>
				<td><form:input class='form-control' type="date" placeholder="From date"
						path="fromDate" /></td>
				<td><form:input class='form-control' type="date" placeholder="To date" path="toDate" /></td>
				<td><input id="viewPDF" type="button" class="btn btn-primary" value="View" /></td>
				<td><input id="getPDF" type="button" class="btn btn-primary" value="Get PDF" /></td>
				<td><input id="getExcel" type="button" class="btn btn-primary" value="Get Excel" /></td>
			</tr>
		</table>

	</form:form>
	<%-- <table class="table">
		<caption>
			<h3>Product Detail</h3>
		</caption>
		<thead>
			<tr>
				<th><B>Name</B></th>
				<th><B>Price</B></th>
			</tr>
		</thead>
		<tbody>
			<c:forEach var="product" items="${ls}">
				<tr>
					<td>${product.name}</td>
					<td>${product.price}</td>
				</tr>
			</c:forEach>
		</tbody>
	</table>--%>
	<br>
	<a href="/logout">
		<button type="button" class="btn btn-primary">Logout</button>
	</a>
	<div>
		<div>
			<script
				src="https://ajax.googleapis.com/ajax/libs/jquery/1.12.4/jquery.min.js"></script>
			<script
				src="${pageContext.request.contextPath}/js/jquery.serializejson.js"></script>
			<script src="${pageContext.request.contextPath}/js/bootstrap.js"></script>
		</div>
	</div>
	</div>
	<div>
		<script>
			$(function() {
				$("#viewPDF").click(function() {
					$('#formSubmit').attr('action', 'viewPDF')
					$('#formSubmit').submit();
					console.log($('#formSubmit'))
				});
				$("#getPDF").click(function() {
					$('#formSubmit').attr('action', 'getPDF')
					$('#formSubmit').submit();
					console.log($('#formSubmit'))
				});
				$("#getExcel").click(function() {
					$('#formSubmit').attr('action', 'reportExcel')
					$('#formSubmit').submit();
					console.log($('#formSubmit'))
				});
			});
		</script>
	</div>

</body>
</html>


