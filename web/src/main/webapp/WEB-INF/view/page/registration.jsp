<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value= "${locale}"/>
<fmt:setBundle basename = "messages" var = "msgs"/>
<fmt:setBundle basename = "interface" var = "intr"/>

<html>
<head>
    <meta charset="utf-8">
    <title>Create new user</title>
</head>
<body>
<%--<jsp:include page="../common/header.jsp"/>--%>
<c:if test="${requestScope.get('message') != null}">
    <h2 style="color: firebrick">
        <fmt:message key="${requestScope.get('message')}" bundle="${msgs}"/></h2>
</c:if>
<form action="${pageContext.request.contextPath}/auth_users/" method="POST">
    <fieldset>
        <legend><fmt:message key="form.registration" bundle="${intr}"/></legend>
        <label for="login"><strong><fmt:message key="choose.login" bundle="${intr}"/>:</strong></label>
        <input id="login" type="text" name="login" required>

        <label for="password"><strong><fmt:message key="choose.pass" bundle="${intr}"/>:</strong></label>
        <input id="password" type="password" name="password" required>

        <label for="passwordRepeat"><strong><fmt:message key="repeat.pass" bundle="${intr}"/>:</strong></label>
        <input id="passwordRepeat" type="password" name="passwordRepeat" required>
        <input type="submit" value="<fmt:message key="submit" bundle="${intr}"/>"/>
    </fieldset>
</form>

</body>
</html>
