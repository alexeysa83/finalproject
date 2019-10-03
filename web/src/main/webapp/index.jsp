<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Main page</title>
</head>
<body>
<h2>Main page</h2>
<c:choose>
    <c:when test="${sessionScope.get('authUser') != null}">
        <a href="${pageContext.request.contextPath}/userpage">${authUser.login}</a>
        <a href="${pageContext.request.contextPath}/logout">Logout</a>
    </c:when>
    <c:otherwise>
        <a href="${pageContext.request.contextPath}/login.jsp">Login</a>
        <a href="${pageContext.request.contextPath}/registration.jsp">Registration</a>
    </c:otherwise>
</c:choose>

<!-- just for check -->
<a href="${pageContext.request.contextPath}/userpage">User page</a>
</body>
</html>
