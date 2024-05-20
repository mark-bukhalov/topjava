<%--
  Created by IntelliJ IDEA.
  User: mbuhalov
  Date: 20.05.2024
  Time: 11:45
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit</title>
    <p><a href="/topjava">Home</a></p>
</head>
<body>
<form method="POST" action='meals' name="frmAddMeal">
    Meal ID : <input type="number" readonly="readonly" name="id"
                     value="<c:out value="${meal.id}" />"/> <br/>
    DateTime : <input
        type="datetime-local" name="dateTime"
        value="<c:out value="${meal.dateTime}" />"/> <br/>
    Description : <input
        type="text" name="description" size="100"
        value="<c:out value="${meal.description}" />"/> <br/>
    Calories : <input
        type="number" name="calories"
        value="<c:out value="${meal.calories}" />"/> <br/>
    <input type="submit" value="Submit"/>
    <button onclick="window.history.back()" type="button">Cancel</button>
</form>
</body>
</html>
