<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: mbuhalov
  Date: 17.05.2024
  Time: 7:13
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@taglib uri="http://example.com/functions" prefix="f" %>
<html>
<head>
    <h3>Meals</h3>
</head>
<body>
<table border="1">
    <tr>
        <th>Дата</th>
        <th>Описание</th>
        <th>Калории</th>
    </tr>
    <c:forEach var="meals" items="${meals}">
        <tr
                <c:choose>
                    <c:when test="${meals.excess eq true}">
                        bgcolor="red"
                    </c:when>
                    <c:otherwise>
                        bgcolor="green"
                    </c:otherwise>
                </c:choose>
        >
            <td>${f:formatLocalDateTime(meals.dateTime, 'dd.MM.yyyy')}</td>
            <td>${meals.description}</td>
            <td>${meals.calories}</td>
        </tr>
    </c:forEach>
</table>
</body>
</html>
