<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%--<fmt:setLocale value="${locale}"/>--%>
<%--<fmt:setBundle basename="messages" var="msgs"/>--%>
<%--<fmt:setBundle basename="interface" var="intr"/>--%>
<html>
<head>
    <title>Add news</title>
</head>
<body>
<%--<jsp:include page="../common/header.jsp"/>--%>
<c:if test="${requestScope.get('message') != null}">
    <h2 style="color: firebrick">
<%--        <fmt:message key="${requestScope.get('message')}" bundle="${msgs}"/></h2>--%>
    <spring:message code="${requestScope.get('message')}"/>
</c:if>

<form action="${pageContext.request.contextPath}/news" method="POST">
    <label for="title"><strong><spring:message code="title"/></strong></label>
<%--    <fmt:message key="title" bundle="${intr}"/>--%>
    <input id="title" type="text" name="title">
    <br/>
    <label for="content"><strong><spring:message code="content"/></strong></label>
    <fmt:message key="content" bundle="${intr}"/>
    <textarea id="content" name="content" rows="10"></textarea>
<%--    <input id="content" type="text" name="content">--%>
    <button type="submit" class="color-square"><spring:message code="add.news"/></button>
<%--    <fmt:message key="add.news" bundle="${intr}"/>--%>
</form>
</body>
</html>