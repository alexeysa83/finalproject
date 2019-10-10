<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html>
<head>
    <meta charset="utf-8">
    <title>Login form</title>
</head>
<body>
<jsp:include page="header.jsp"/>

    <h2 style="color: firebrick">${requestScope.get('message')}</h2>

<form action="${pageContext.request.contextPath}/auth/login" method="POST">
    <fieldset>
        <legend>Login form</legend>

        <label for="login"><strong>Login:</strong></label>
        <input id="login" type="text" name="login" required>

        <label for="password"><strong>Password:</strong></label>
        <input id="password" type="password" name="password" required>
        <input type="submit" value="Submit"/>
    </fieldset>
</form>

</body>
</html>
