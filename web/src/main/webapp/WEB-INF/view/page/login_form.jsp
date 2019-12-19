<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<html>
<head>
    <meta charset="utf-8">
    <title>Login</title>
</head>
<body>

<form action="${pageContext.request.contextPath}/login" method="POST">
    <fieldset>
        <legend><spring:message code="form.login"/></legend>

        <label for="login"><strong><spring:message code="login"/></strong></label>
        <input id="login" type="text" name="login" required>

        <label for="password"><strong><spring:message code="password"/></strong></label>
        <input id="password" type="password" name="password" required>
        <input type="submit" value="<spring:message code="submit"/>"/>
    </fieldset>
</form>

</body>
</html>
