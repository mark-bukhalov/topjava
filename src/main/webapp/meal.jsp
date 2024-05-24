<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Edit</title>
    <h3><a href="index.html">Home</a></h3>
    <h3>${ param.action == "edit" ? 'Edit meal' : 'Add meal'}</h3>
</head>
<body>

<jsp:useBean id="meal" type="ru.javawebinar.topjava.model.Meal" scope="request"/>
<form method="POST" action='meals' name="frmAddMeal">
        <input type="hidden" name="id" value="${meal.id}"/>
    DateTime : <input
        type="datetime-local" name="dateTime"
        value="${meal.dateTime}"/> <br/>
    Description : <input
        type="text" name="description" size="100"
        value="${meal.description}"/> <br/>
    Calories : <input
        type="number" name="calories"
        value="${meal.calories}"/> <br/>
    <input type="submit" value="Submit"/>
    <button onclick="window.history.back()" type="button">Cancel</button>
</form>
</body>
</html>
