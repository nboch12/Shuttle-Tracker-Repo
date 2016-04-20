<!DOCTYPE html>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>

<html>
	<head>
		<title>Shuttle Tracker Input</title>
	</head>

	<body>
		
		
		<form action="${pageContext.servletContext.contextPath}/shuttleTracker" method="post">
		
			<table>
				<tr>
					<td class="label">ID:</td>
					<td><input type="text" name="id" size="12" value="${id}" /></td>
				</tr>
				<tr>
					<td class="label">MAC Address:</td>
					<td><input type="text" name="mac" size="12" value="${mac}" /></td>
				</tr>
				<tr>
					<td class="label">Latitude:</td>
					<td><input type="text" name="latitude" size="12" value="${latitude}" /></td>
				</tr>
				<tr>
					<td class="label">Longitude:</td>
					<td><input type="text" name="longitude" size="12" value="${longitude}" /></td>
				</tr>
			</table>
			<input type="Submit" name="submitData" value="Submit Data">
			
			<c:if test="${! empty message}">
				<div class="error">${message}</div>
			</c:if>
		</form>
	</body>
</html>