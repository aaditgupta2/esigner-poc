<!DOCTYPE html>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html lang="en">
<head>
<link rel="stylesheet" type="text/css"
	href="webjars/bootstrap/3.3.7/css/bootstrap.min.css" />

</head>
<body>
	<div class="container">
		<header>
			<strong>Hello ${message} <a
				href="pdf?dealGuid=iCUvgNWKFqVBVLW2">Download and E-Sign</a></strong>
		</header>
		<hr />
		<div>
			<h1>Signature List</h1>
			<table border="1">
				<tr>
					<th>Doc Name</th>
					<th>Signed Date</th>
					<th>DownLoad Signed Doc</th>
				</tr>
				<c:forEach var="signature" items="${signatures}">
					<tr>
						<td>${signature.docName}</td>
						<td>${signature.createdAt}</td>
						<td><a href="download?id=${signature.id}" target="_blank">Download</a></td>
					</tr>
				</c:forEach>
			</table>
		</div>

	</div>

	<script type="text/javascript"
		src="webjars/bootstrap/3.3.7/js/bootstrap.min.js"></script>
</body>

</html>