<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
    <link rel="stylesheet" href="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/css/bootstrap.min.css"
          integrity="sha384-ggOyR0iXCbMQv3Xipma34MD+dH/1fQ784/j6cY/iJTQUOhcWr7x9JvoRxT2MZw1T" crossorigin="anonymous">
</head>
<body>
<div class="dropdown">
    <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton" data-toggle="dropdown"
            aria-haspopup="true" aria-expanded="false">
        <spring:message code="choose.lang"/>
    </button>
    <div class="dropdown-menu" aria-labelledby="dropdownMenuButton">
        <a class="dropdown-item" href="${requestScope['javax.servlet.forward.request_uri']}?locale=en_US">
            <spring:message code="english"/></a>
        <a class="dropdown-item" href="${requestScope['javax.servlet.forward.request_uri']}?locale=ru_RU">
            <spring:message code="russian"/></a>
    </div>
</div>
<sec:authorize access="hasRole('ADMIN')">
    <a href="${pageContext.request.contextPath}/badges/all"><spring:message code="admin.page"/></a>
</sec:authorize>

<a href="${pageContext.request.contextPath}/news/all"><spring:message code="main"/></a>

<sec:authorize access="isAuthenticated()">
    <a href="${pageContext.request.contextPath}/user_infos/<sec:authentication property="principal.id"/>">
        <sec:authentication property="principal.login"/></a>
    <a href="${pageContext.request.contextPath}/logout_custom"><spring:message code="logout"/></a>
</sec:authorize>

<sec:authorize access="!isAuthenticated()">
    <a href="${pageContext.request.contextPath}/login"><spring:message code="login"/></a>
    <a href="${pageContext.request.contextPath}/auth_users/forward_to_registration_form"><spring:message code="registration"/></a>
</sec:authorize>
<hr/>
<c:if test="${requestScope.get('message') != null}">
    <h4 style="color: #18b239">
        <spring:message code="${requestScope.get('message')}"/></h4>
</c:if>
<c:if test="${requestScope.get('error') != null}">
    <h4 style="color: firebrick">
        <spring:message code="${requestScope.get('error')}"/></h4>
</c:if>
<script src="https://code.jquery.com/jquery-3.3.1.slim.min.js"
        integrity="sha384-q8i/X+965DzO0rT7abK41JStQIAqVgRVzpbzo5smXKp4YfRvH+8abtTE1Pi6jizo"
        crossorigin="anonymous"></script>
<script src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.14.7/umd/popper.min.js"
        integrity="sha384-UO2eT0CpHqdSJQ6hJty5KVphtPhzWj9WO1clHTMGa3JDZwrnQq4sF86dIHNDz0W1"
        crossorigin="anonymous"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.3.1/js/bootstrap.min.js"
        integrity="sha384-JjSmVgyd0p3pXB1rRibZUAYoIIy6OrQ6VrjIEaFf/nJGzIxFDsf4x0xIM+B07jRM"
        crossorigin="anonymous"></script>
</body>
</html>
