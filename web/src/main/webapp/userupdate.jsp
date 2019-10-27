<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="messages" var="msgs"/>
<fmt:setBundle basename="interface" var="intr"/>
<html>
<head>
    <title>Update user</title>
</head>
<body>
<jsp:include page="header.jsp"/>

<c:if test="${requestScope.get('message') != null}">
    <h2 style="color: firebrick">
        <fmt:message key="${requestScope.get('message')}" bundle="${msgs}"/></h2>
</c:if>

<c:if test="${authUser.login == user.userLogin || authUser.role == 'ADMIN'}">
    <form action="${pageContext.request.contextPath}/auth/user/update" method="POST">
        <h2><span style='color: blue;'>${user.userLogin}</span></h2>
        <h2><fmt:message key="update.settings" bundle="${intr}"/>:</h2><br/>

        <br/> <fmt:message key="first" bundle="${intr}"/>: <br/>
        <input type="text" name="firstName" value="${user.firstName}">

        <br/><fmt:message key="last" bundle="${intr}"/>:<br/>
        <input type="text" name="lastName" value="${user.lastName}">

        <br/><fmt:message key="email" bundle="${intr}"/>:<br/>
        <input type="text" name="email" value="${user.email}">

        <br/><fmt:message key="phone" bundle="${intr}"/>:<br/>
        <input type="text" name="phone" value="${user.phone}">

        <label>
            <input hidden="hidden" type="text" name="authId" value="${user.authId}">
        </label>
        <input type="reset" value="<fmt:message key="reset" bundle="${intr}"/>">
        <button type="submit" class="color-square"><fmt:message key="update.user" bundle="${intr}"/></button>
    </form>

    <c:if test="${authUser.login == user.userLogin}">
        <h2><fmt:message key="update.security" bundle="${intr}"/>:</h2><br/>
        <form action="${pageContext.request.contextPath}/auth/user/login" method="POST">
            <br/><fmt:message key="login" bundle="${intr}"/>:<br/>
            <input type="text" name="login" value="${user.userLogin}" required>
            <label>
                <input hidden="hidden" type="text" name="authId" value="${user.authId}">
            </label>
            <button type="submit" class="color-square"><fmt:message key="change.login" bundle="${intr}"/></button>
        </form>

        <form action="${pageContext.request.contextPath}/auth/user/password" method="POST">
            <br/><fmt:message key="current.pass" bundle="${intr}"/>:<br/>
            <input type="password" name="passwordBefore" required>

            <br/><fmt:message key="new.pass" bundle="${intr}"/>:<br/>
            <input type="password" name="passwordNew" required>

            <br/><fmt:message key="repeat.pass" bundle="${intr}"/>:<br/>
            <input type="password" name="passwordRepeat" required>

            <label>
                <input hidden="hidden" type="text" name="authId" value="${user.authId}">
            </label>
            <button type="submit" class="color-square"><fmt:message key="change.pass" bundle="${intr}"/></button>
        </form>
    </c:if>

    <c:if test="${authUser.role == 'ADMIN'}">
        <form action="${pageContext.request.contextPath}/admin/update/role" method="POST">
            <br/><fmt:message key="role" bundle="${intr}"/>:<br/>
            <input type="radio" name="role" value="USER" required> <fmt:message key="user" bundle="${intr}"/>
            <input type="radio" name="role" value="ADMIN" required> <fmt:message key="admin" bundle="${intr}"/>
            <label>
                <input hidden="hidden" type="text" name="authId" value="${user.authId}">
            </label>
            <button type="submit" class="color-square"><fmt:message key="change.role" bundle="${intr}"/></button>
        </form>
    </c:if>

    <form action="${pageContext.request.contextPath}/auth/user/delete" method="GET">
        <input type="submit" value="<fmt:message key="delete.user" bundle="${intr}"/>"/>
        <label>
            <input hidden="hidden" type="text" name="authId" value="${user.authId}">
            <input hidden="hidden" type="text" name="login" value="${user.userLogin}">
        </label>
    </form>
</c:if>

</body>
</html>
