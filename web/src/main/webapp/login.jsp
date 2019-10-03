<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="utf-8">
    <title>Login form</title>
</head>
<body>
<c:if test="${requestScope.get('error') ne null}">
    <h2 style="color: firebrick">${requestScope.get('error')}</h2>
</c:if>
<form action="${pageContext.request.contextPath}/login" method="POST">
    <fieldset>
        <legend>Login form</legend>

        <label for="login"><strong>Login:</strong></label>
        <input id="login" type="text" name="login" required>

        <label for="password"><strong>Password:</strong></label>
        <input id="password" type="password" name="password" required>
        <input type="submit" value="Submit"/>
    </fieldset>
</form>

<form action="${pageContext.request.contextPath}/registration" method="GET">
    <input type="submit" value="Create user"/>
</form>
</body>
</html>
