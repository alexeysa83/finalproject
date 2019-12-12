<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="messages" var="msgs"/>
<fmt:setBundle basename="interface" var="intr"/>
<html>
<head>
    <title>User page</title>
</head>
<body>
<jsp:include page="header.jsp"/>

<c:if test="${requestScope.get('message') != null}">
    <h2 style="color: firebrick">
        <fmt:message key="${requestScope.get('message')}" bundle="${msgs}"/></h2>
</c:if>

<c:if test="${user != null}">
    <h2><span style='color: blue;'>${user.userLogin}</span></h2>
    <hr/>
    <h2 style="color: #2bb239"><fmt:message key="first" bundle="${intr}"/>: ${user.firstName}</h2>
    <h2 style="color: #2bb239"><fmt:message key="last" bundle="${intr}"/>: ${user.lastName}</h2>
    <h2 style="color: #b2a44f"><fmt:message key="email" bundle="${intr}"/>: ${user.email}</h2>
    <h2 style="color: #b2a44f"><fmt:message key="phone" bundle="${intr}"/>: ${user.phone}</h2>

    <h2><fmt:message key="registered" bundle="${intr}"/>: ${user.registrationTime}</h2>

    <c:if test="${authUser.login == user.userLogin || authUser.role == 'ADMIN'}">
        <form action="${pageContext.request.contextPath}/user_infos/${user.authId}/torequest" method="GET">
            <input type="submit" value="<fmt:message key="update.user" bundle="${intr}"/>"/>
        </form>
        <form action="${pageContext.request.contextPath}/auth_users/${user.authId}/delete" method="POST">
            <input type="submit" value="<fmt:message key="delete.user" bundle="${intr}"/>"/>
            <label>
                <input hidden="hidden" type="text" name="login" value="${user.userLogin}">
            </label>
        </form>
    </c:if>
    <hr/>
    <h4><span style='color: blue;'><fmt:message key="badges" bundle="${intr}"/></span></h4>
    <hr/>
    <c:forEach items="${requestScope.userBadges}" var="badge">
        <h4><span class="badge badge-secondary" style="color: #2bb239">${badge.badgeName}</span></h4>
    </c:forEach>
    <c:if test="${authUser.role == 'ADMIN'}">
        <div class="dropdown">
            <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuAddBadge"
                    data-toggle="dropdown"
                    aria-haspopup="true" aria-expanded="false">
                <fmt:message key="add.badge" bundle="${intr}"/>
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
                <fmt:message key="delete.badge" bundle="${intr}"/>
            </button>
            <div class="dropdown-menu" aria-labelledby="dropdownMenuDeleteBadge">
                <c:forEach items="${requestScope.userBadges}" var="badge">
                    <a class="dropdown-item"
                       href="${pageContext.request.contextPath}/user_infos/${user.authId}/delete/${badge.id}">
                            ${badge.badgeName}</a>
                </c:forEach>
            </div>
        </div>
    </c:if>
</c:if>
</body>
</html>
