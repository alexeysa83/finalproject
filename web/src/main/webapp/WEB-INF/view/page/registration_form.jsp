<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<html>
<head>
    <meta charset="utf-8">
    <title>Registration</title>
</head>
<body>
<form action="${pageContext.request.contextPath}/auth_users/registration" method="POST">
    <fieldset>
        <legend><spring:message code="form.registration"/></legend>
        <label for="login"><strong><spring:message code="choose.login"/>:</strong></label>
        <input id="login" type="text" name="login" required
               placeholder="<spring:message code="login.placeholder"/>">

        <label for="password"><strong><spring:message code="choose.pass"/>:</strong></label>
        <input id="password" type="password" name="password" required
               placeholder="<spring:message code="pass.placeholder"/>">

        <label for="passwordRepeat"><strong><spring:message code="repeat.pass"/>:</strong></label>
        <input id="passwordRepeat" type="password" name="repeatPassword" required
               placeholder="<spring:message code="passrepeat.placeholder"/>">
        <input type="submit" value="<spring:message code="submit"/>"/>
    </fieldset>
</form>

</body>
</html>
