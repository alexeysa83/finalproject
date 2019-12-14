<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css" integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>
<div class="dropdown">
    <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown"
            aria-haspopup="true" aria-expanded="false">
        <spring:message code="choose.lang"/>
            </button>
    <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
        <a class="dropdown-item" href="${pageContext.request.contextPath}/news?locale=en_US">
            <spring:message code="english"/></a>
        <a class="dropdown-item" href="${pageContext.request.contextPath}/news?locale=ru_RU">
            <spring:message code="russian"/></a>
    </div>
</div>
<c:if test="${authUser.role == 'ADMIN'}">
    <a href="${pageContext.request.contextPath}/badges/"><spring:message code="admin.page"/></a>
</c:if>
<a href="${pageContext.request.contextPath}/news/"><spring:message code="main"/></a>
<c:choose>
    <c:when test="${sessionScope.get('authUser') != null}">
        <a href="${pageContext.request.contextPath}/user_infos/${authUser.id}">${authUser.login}</a>
        <a href="${pageContext.request.contextPath}/logout"><spring:message code="logout"/></a>
    </c:when>
    <c:otherwise>
        <a href="${pageContext.request.contextPath}/login"><spring:message code="login"/></a>
        <a href="${pageContext.request.contextPath}/auth_users/">
            <spring:message code="registration"/></a>
    </c:otherwise>
</c:choose>
<hr/>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js" integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo" crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js" integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1" crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js" integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM" crossorigin="anonymous"></script>
</body>
</html>
