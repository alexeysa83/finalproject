<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value= "${locale}"/>
<fmt:setBundle basename = "messages" var = "msgs"/>
<fmt:setBundle basename = "interface" var = "intr"/>
<html>
<head>
    <title>Userpage</title>
</head>
<body>
<jsp:include page="header.jsp"/>

<%--User deleted message in JSP for deleted users + do not show delete / update buttons--%>

<c:if test="${requestScope.get('message') != null}">
    <h2 style="color: firebrick">
        <fmt:message key="${requestScope.get('message')}" bundle="${msgs}"/></h2>
</c:if>

<c:if test="${user != null}">
<h2><span style='color: blue;'>${user.userLogin}</span></h2>

<h2 style="color: #2bb239"><fmt:message key="first" bundle="${intr}"/>: ${user.firstName}</h2>
<h2 style="color: #2bb239"><fmt:message key="last" bundle="${intr}"/>: ${user.lastName}</h2>
<h2 style="color: #b2a44f"><fmt:message key="email" bundle="${intr}"/>: ${user.email}</h2>
<h2 style="color: #b2a44f"><fmt:message key="phone" bundle="${intr}"/>: ${user.phone}</h2>

<h2><fmt:message key="registered" bundle="${intr}"/>: ${user.registrationTime}</h2>

<c:if test="${authUser.login == user.userLogin || authUser.role == 'ADMIN'}">
    <form action="${pageContext.request.contextPath}/auth/user/update" method="GET">
        <input type="submit" value="<fmt:message key="update.user" bundle="${intr}"/>"/>
        <label>
            <input hidden="hidden" type="text" name="authId" value="${user.authId}">
        </label>
    </form>
    <form action="${pageContext.request.contextPath}/auth/user/delete" method="GET">
        <input type="submit" value="<fmt:message key="delete.user" bundle="${intr}"/>"/>
        <label>
            <input hidden="hidden" type="text" name="authId" value="${user.authId}">
            <input hidden="hidden" type="text" name="login" value="${user.userLogin}">
        </label>
    </form>
</c:if>
</c:if>
</body>
</html>
