<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="utf-8">
    <title>Create new user</title>
</head>
<body>
<c:if test="${requestScope.get('error') ne null}">
    <h2 style="color: firebrick">${requestScope.get('error')}</h2>
</c:if>
<form action="${pageContext.request.contextPath}/registration" method="POST">
    <p><strong>Choose login:</strong>
        <input maxlength="25" size="40" name="login" required></p>
    <p><strong>Choose password:</strong>
        <input type="password" maxlength="25" size="40" name="password" required></p>
    <p><strong>Repeat password:</strong>
        <input type="password" maxlength="25" size="40" name="repeatpassword" required></p>
    <input type="submit" value="Submit"/>
</form>
</body>
</html>
