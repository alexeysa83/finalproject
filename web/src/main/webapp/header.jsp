<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="interface" var="intr"/>
<html>
<body>
<a href="${pageContext.request.contextPath}/index.jsp"><fmt:message key="main" bundle="${intr}"/></a>
<c:choose>
    <c:when test="${sessionScope.get('authUser') != null}">
        <a href="${pageContext.request.contextPath}/restricted/user/profile?authId=${authUser.id}">${authUser.login}</a>
        <a href="${pageContext.request.contextPath}/auth/logout"><fmt:message key="logout" bundle="${intr}"/></a>
    </c:when>
    <c:otherwise>
        <a href="${pageContext.request.contextPath}/auth/login"><fmt:message key="login" bundle="${intr}"/></a>
        <a href="${pageContext.request.contextPath}/auth/registration">
            <fmt:message key="registration" bundle="${intr}"/></a>
    </c:otherwise>
</c:choose>
<hr/>
</body>
</html>
