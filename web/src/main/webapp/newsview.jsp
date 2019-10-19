<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value= "${locale}"/>
<fmt:setBundle basename = "messages" var = "msgs"/>
<fmt:setBundle basename = "interface" var = "intr"/>
<html>
<head>
    <title>News view</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<c:if test="${requestScope.get('message') != null}">
    <h2 style="color: firebrick">
        <fmt:message key="${requestScope.get('message')}" bundle="${msgs}"/></h2>
</c:if>

<h2><span style='color: blue;'>${news.title}</span></h2>

<h2 style="color: #2bb239"><fmt:message key="author" bundle="${intr}"/>:
    <a href="${pageContext.request.contextPath}/auth/user/view?authId=${news.authId}">
    ${news.authorNews}</a>
    <fmt:message key="created" bundle="${intr}"/>: ${news.creationTime}</h2>
<h2>${news.content}</h2>

<c:if test="${authUser.login == news.authorNews || authUser.role == 'ADMIN'}">
    <form action="${pageContext.request.contextPath}/auth/news/update" method="GET">
        <input type="submit" value="<fmt:message key="update.news" bundle="${intr}"/>"/>
        <label>
            <input hidden="hidden" type="text" name="newsId" value="${news.id}">
        </label>
    </form>
    <form action="${pageContext.request.contextPath}/auth/news/delete" method="GET">
        <input type="submit" value="<fmt:message key="delete.news" bundle="${intr}"/>"/>
        <label>
            <input hidden="hidden" type="text" name="newsId" value="${news.id}">
        </label>
    </form>
</c:if>
</body>
</html>

<%-- <jsp:useBean id="news" scope="request" type="com.github.alexeysa83.finalproject.model.News"/>--%>
<%--<jsp:useBean id="authUser" scope="session" type="com.github.alexeysa83.finalproject.model.AuthUser"/> --%>
