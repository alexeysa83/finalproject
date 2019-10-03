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
    <fieldset>
        <legend>Registration form</legend>
        <label for="login"><strong>Choose login:</strong></label>
        <input id="login" type="text" name="login" required>

        <label for="password"><strong>Choose password:</strong></label>
        <input id="password" type="password" name="password" required>

        <!--<label for="repeatpassword"><strong>Repeat password:</strong></label>
        <input id="repeatpassword" type="password" name="repeatpassword" required>-->
        <input type="submit" value="Submit"/>
    </fieldset>
</form>
</body>
</html>
