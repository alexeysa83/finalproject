<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
    <title>User page</title>
</head>
<body>

<c:if test="${user != null}">
    <h2><span style='color: blue;'>${user.userLogin}</span>
        <span class="rating" style='color: ${user.ratingColour}'> ${user.userRating} </span></h2>
    <hr/>
    <h2 style="color: #2bb239"><spring:message code="first"/>: ${user.firstName}</h2>
    <h2 style="color: #2bb239"><spring:message code="last"/>: ${user.lastName}</h2>
    <h2 style="color: #b2a44f"><spring:message code="email"/>: ${user.email}</h2>
    <h2 style="color: #b2a44f"><spring:message code="phone"/>: ${user.phone}</h2>

    <h2><spring:message code="registered"/>: ${user.registrationTime}</h2>

    <sec:authorize access="isAuthenticated()">
        <sec:authentication property="principal" var="userInSession"/>
    </sec:authorize>

    <c:if test="${user.userLogin == userInSession.login || userInSession.role == 'ADMIN'}">
        <form action="${pageContext.request.contextPath}/user_infos/${user.authId}" method="GET">
            <input type="submit" value="<spring:message code="update.user"/>"/>
            <label>
                <input hidden="hidden" type="text" name="returnPage" value="user_update">
            </label>
        </form>
        <form action="${pageContext.request.contextPath}/auth_users/${user.authId}/delete" method="POST">
            <input type="submit" value="<spring:message code="delete.user"/>"/>
        </form>
    </c:if>
    <hr/>
    <h4><span style='color: blue;'><spring:message code="badges"/></span></h4>
    <hr/>
    <c:forEach items="${requestScope.userBadges}" var="badge">
        <h4><span class="badge badge-secondary" style="color: #2bb239">${badge.badgeName}</span></h4>
    </c:forEach>
    <sec:authorize access="hasRole('ADMIN')">
        <div class="dropdown">
            <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuAddBadge"
                    data-toggle="dropdown"
                    aria-haspopup="true" aria-expanded="false">
                <spring:message code="add.badge"/>
            </button>
            <div class="dropdown-menu" aria-labelledby="dropdownMenuAddBadge">
                <c:forEach items="${requestScope.badgesDB}" var="badge">
                    <a class="dropdown-item"
                       href="${pageContext.request.contextPath}/user_infos/${user.authId}/add/${badge.id}">
                            ${badge.badgeName}</a>
                </c:forEach>
            </div>
        </div>
        <div class="dropdown">
            <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuDeleteBadge"
                    data-toggle="dropdown"
                    aria-haspopup="true" aria-expanded="false">
                <spring:message code="delete.badge"/>
            </button>
            <div class="dropdown-menu" aria-labelledby="dropdownMenuDeleteBadge">
                <c:forEach items="${requestScope.userBadges}" var="badge">
                    <a class="dropdown-item"
                       href="${pageContext.request.contextPath}/user_infos/${user.authId}/delete/${badge.id}">
                            ${badge.badgeName}</a>
                </c:forEach>
            </div>
        </div>
    </sec:authorize>
</c:if>
</body>
</html>
