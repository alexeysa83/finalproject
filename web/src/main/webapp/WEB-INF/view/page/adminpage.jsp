<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<html>
<head>
    <title>Admin page</title>
</head>
<body>
<c:if test="${requestScope.get('message') != null}">
    <h2 style="color: firebrick">
        <spring:message code="${requestScope.get('message')}"/></h2>
</c:if>
<p><span style='color: blue;'><spring:message code="badges"/></span></p>

<c:forEach items="${requestScope.badgesDB}" var="badge" varStatus="loop">
    <h4>${loop.index+1}) ${badge.badgeName}</h4>
    <c:choose>
        <c:when test="${badge.id == badgeToUpdateId}">
            <form action="${pageContext.request.contextPath}/badges/${badge.id}/update" method="POST">
                <input class="form-control" type="text" name="badgeName"
                       value="${badge.badgeName}">
                <input type="submit" value="<spring:message code="update.badge"/>"/>
            </form>
        </c:when>
        <c:otherwise>
            <form action="${pageContext.request.contextPath}/badges/${badge.id}" method="GET">
                <input type="submit" value="<spring:message code="update.badge"/>"/>
            </form>
        </c:otherwise>
    </c:choose>
    <form action="${pageContext.request.contextPath}/badges/${badge.id}/delete" method="POST">
        <input type="submit" value="<spring:message code="delete.badge"/>"/>
    </form>
</c:forEach>
<form action="${pageContext.request.contextPath}/badges/" method="POST">
    <input class="form-control" type="text" name="badgeName"
           placeholder="<spring:message code="name.badge"/>">
    <input type="submit" value="<spring:message code="add.badge"/>"/>
</form>
</body>
</html>
