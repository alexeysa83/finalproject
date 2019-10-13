<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<body>
<h2>Header</h2>
<c:choose>
    <c:when test="${sessionScope.get('authUser') != null}">
        <a href="${pageContext.request.contextPath}/restricted/user/profile?authId=${authUser.id}">${authUser.login}</a>
        <a href="${pageContext.request.contextPath}/auth/logout">Logout</a>
        <a href="${pageContext.request.contextPath}/index.jsp">Main page</a>
    </c:when>
    <c:otherwise>
        <a href="${pageContext.request.contextPath}/auth/login">Login</a>
        <a href="${pageContext.request.contextPath}/auth/registration">Registration</a>
        <a href="${pageContext.request.contextPath}/index.jsp">Main page</a>
    </c:otherwise>
</c:choose>
<hr/>
</body>
</html>