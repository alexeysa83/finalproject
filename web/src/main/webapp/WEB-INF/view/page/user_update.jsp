<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
    <title>Update user</title>
</head>
<body>

<sec:authorize access="isAuthenticated()">
    <sec:authentication property="principal" var="userInSession"/>
</sec:authorize>

<c:if test="${user.userLogin == userInSession.login || userInSession.role == 'ADMIN'}">

    <form action="${pageContext.request.contextPath}/user_infos/${user.authId}/update" method="POST">
        <h2><span style='color: blue;'>${user.userLogin}</span></h2>
        <h2><spring:message code="update.settings"/>:</h2><br/>

        <br/> <spring:message code="first"/>: <br/>
        <input type="text" name="firstName" value="${user.firstName}">

        <br/><spring:message code="last"/>:<br/>
        <input type="text" name="lastName" value="${user.lastName}">

        <br/><spring:message code="email"/>:<br/>
        <input type="text" name="email" value="${user.email}">

        <br/><spring:message code="phone"/>:<br/>
        <input type="text" name="phone" value="${user.phone}">

        <input type="reset" value="<spring:message code="reset"/>">
        <button type="submit" class="color-square"><spring:message code="update.user"/></button>
    </form>

    <c:if test="${user.userLogin == userInSession.login}">
        <h2><spring:message code="update.security"/>:</h2><br/>
        <form action="${pageContext.request.contextPath}/auth_users/${user.authId}/update_login" method="POST">
            <br/><spring:message code="login"/>:<br/>
            <input type="text" name="login" value="${user.userLogin}" required>
            <button type="submit" class="color-square"><spring:message code="change.login"/></button>
        </form>

        <form action="${pageContext.request.contextPath}/auth_users/${user.authId}/update_password" method="POST">
            <br/><spring:message code="current.pass"/>:<br/>
            <input type="password" name="passwordCurrent" required>

            <br/><spring:message code="new.pass"/>:<br/>
            <input type="password" name="password" required>

            <br/><spring:message code="repeat.pass"/>:<br/>
            <input type="password" name="repeatPassword" required>

            <button type="submit" class="color-square"><spring:message code="change.pass"/></button>
        </form>
    </c:if>

    <sec:authorize access="hasRole('ADMIN')">
            <form action="${pageContext.request.contextPath}/auth_users/${user.authId}/update_role" method="POST">
            <br/><spring:message code="role"/>:<br/>
            <input type="radio" name="role" value="USER" required> <spring:message code="user"/>
            <input type="radio" name="role" value="ADMIN" required> <spring:message code="admin"/>
            <button type="submit" class="color-square"><spring:message code="change.role"/></button>
        </form>
    </sec:authorize>

    <form action="${pageContext.request.contextPath}/auth_users/${user.authId}/delete" method="POST">
        <input type="submit" value="<spring:message code="delete.user"/>"/>
        <label>
            <input hidden="hidden" type="text" name="login" value="${user.userLogin}">
        </label>
    </form>
</c:if>
</body>
</html>
