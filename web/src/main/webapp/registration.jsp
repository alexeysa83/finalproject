<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <meta charset="utf-8">
    <title>Create new user</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<c:if test="${requestScope.get('message') ne null}">
    <h2 style="color: firebrick">${requestScope.get('message')}</h2>
</c:if>
<form action="${pageContext.request.contextPath}/auth/registration" method="POST">
    <fieldset>
        <legend>Registration form</legend>
        <label for="login"><strong>Choose login:</strong></label>
        <input id="login" type="text" name="login" required>

        <label for="password"><strong>Choose password:</strong></label>
        <input id="password" type="password" name="password" required>

        <label for="passwordRepeat"><strong>Repeat password:</strong></label>
        <input id="passwordRepeat" type="password" name="passwordRepeat" required>
        <input type="submit" value="Submit"/>
    </fieldset>
</form>

</body>
</html>
