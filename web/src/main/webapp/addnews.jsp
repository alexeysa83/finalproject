<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="messages" var="msgs"/>
<fmt:setBundle basename="interface" var="intr"/>
<html>
<head>
    <title>Add news</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<c:if test="${requestScope.get('message') != null}">
    <h2 style="color: firebrick">
        <fmt:message key="${requestScope.get('message')}" bundle="${msgs}"/></h2>
</c:if>

<form action="${pageContext.request.contextPath}/auth/news/add" method="POST">
    <label for="title"><strong><fmt:message key="title" bundle="${intr}"/></strong></label>
    <input id="title" type="text" name="title">
    <br/>
    <label for="content"><strong><fmt:message key="content" bundle="${intr}"/></strong></label>
    <textarea id="content" name="content" rows="10"></textarea>
<%--    <input id="content" type="text" name="content">--%>
    <button type="submit" class="color-square"><fmt:message key="add.news" bundle="${intr}"/></button>
</form>
</body>
</html>