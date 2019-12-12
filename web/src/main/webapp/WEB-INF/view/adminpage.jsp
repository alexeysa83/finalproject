<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="messages" var="msgs"/>
<fmt:setBundle basename="interface" var="intr"/>
<html>
<head>
    <title>Admin page</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<c:if test="${requestScope.get('message') != null}">
    <h2 style="color: firebrick">
        <fmt:message key="${requestScope.get('message')}" bundle="${msgs}"/></h2>
</c:if>
<p><span style='color: blue;'><fmt:message key="badges" bundle="${intr}"/></span></p>

<c:forEach items="${requestScope.badgesDB}" var="badge" varStatus="loop">
    <h4>${loop.index+1}) ${badge.badgeName}</h4>
    <c:choose>
        <c:when test="${badge.id == badgeToUpdateId}">
            <form action="${pageContext.request.contextPath}/badges/${badge.id}/update" method="POST">
                <input class="form-control" type="text" name="badgeName"
                       value="${badge.badgeName}">
                <input type="submit" value="<fmt:message key="update.badge" bundle="${intr}"/>"/>
            </form>
        </c:when>
        <c:otherwise>
            <form action="${pageContext.request.contextPath}/badges/${badge.id}" method="GET">
                <input type="submit" value="<fmt:message key="update.badge" bundle="${intr}"/>"/>
            </form>
        </c:otherwise>
    </c:choose>
    <form action="${pageContext.request.contextPath}/badges/${badge.id}/delete" method="POST">
        <input type="submit" value="<fmt:message key="delete.badge" bundle="${intr}"/>"/>
    </form>
</c:forEach>
<form action="${pageContext.request.contextPath}/badges/" method="POST">
    <input class="form-control" type="text" name="badgeName"
           placeholder="<fmt:message key="name.badge" bundle="${intr}"/>">
    <input type="submit" value="<fmt:message key="add.badge" bundle="${intr}"/>"/>
</form>
</body>
</html>
