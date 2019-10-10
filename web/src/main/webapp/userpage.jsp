<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Userpage</title>
</head>
<body>
<jsp:include page="header.jsp"/>

<%--User deleted message in JSP for deleted users + do not show delete / update buttons--%>
<h2 style="color: firebrick">${requestScope.get('message')}</h2>

<h2><span style='color: blue;'>${user.userLogin}</span></h2>

<h2 style="color: #2bb239">Name: ${user.firstName}</h2>
<h2 style="color: #2bb239">Surname: ${user.lastName}</h2>
<h2 style="color: #b2a44f">Email: ${user.email}</h2>
<h2 style="color: #b2a44f">Phone: ${user.phone}</h2>

<h2>Registered: ${user.registrationTime}</h2>

<c:if test="${authUser.login == user.userLogin || authUser.role == 'ADMIN'}">
    <form action="${pageContext.request.contextPath}/restricted/user/update" method="GET">
        <input type="submit" value="Update user"/>
        <label>
            <input hidden="hidden" type="text" name="authId" value="${user.authId}">
        </label>
    </form>
    <form action="${pageContext.request.contextPath}/restricted/authuser/delete" method="GET">
        <input type="submit" value="Delete user"/>
        <label>
            <input hidden="hidden" type="text" name="authId" value="${user.authId}">
            <input hidden="hidden" type="text" name="login" value="${user.userLogin}">
        </label>
    </form>
</c:if>
</body>
</html>
