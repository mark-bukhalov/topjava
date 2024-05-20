<%--
  Created by IntelliJ IDEA.
  User: mbuhalov
  Date: 17.05.2024
  Time: 7:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://example.com/functions" prefix="f" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
    <title>Meals</title>
    <p><a href="/topjava">Home</a></p>
    <h3>Meals</h3>
</head>
<body>
<table border="1">
    <tr>
        <th>DateTime</th>
        <th>Description</th>
        <th>Calories</th>
    </tr>
    <c:forEach var="meal" items="${meals}">
        <tr bgcolor="${meal.excess eq true} ? 'green' : 'red'}"/>
        <td>${f:formatLocalDateTime(meal.dateTime, 'dd.MM.yyyy')}</td>
        <td>${meal.description}</td>
        <td>${meal.calories}</td>
        <td><a href="meals?action=edit&id=<c:out value="${meal.id}"/>">Update</a></td>
        <td><a href="meals?action=delete&id=<c:out value="${meal.id}"/>">Delete</a></td>
        </tr>
    </c:forEach>
</table>
<p><a href="meals?action=insert">Add Meal</a></p>
</body>
</html>
