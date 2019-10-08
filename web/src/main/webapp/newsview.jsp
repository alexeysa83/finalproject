<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>News view</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<c:if test="${requestScope.get('message') != null}">
    <h2 style="color: firebrick">${requestScope.get('message')}</h2>
</c:if>
<h2><span style='color: blue;'>${news.title}</span></h2>

<h2 style="color: #2bb239">Author: <a href="${pageContext.request.contextPath}/userpage?userPage=${news.authorNews}">
    ${news.authorNews}</a> Created: ${news.creationTime}</h2>
<h2>${news.content}</h2>

<c:if test="${authUser.login == news.authorNews || authUser.role == 'ADMIN'}">
    <form action="${pageContext.request.contextPath}/updatenews?newsId=${news.id}" method="GET">
        <input type="submit" value="Update"/>
        <label>
            <input hidden="hidden" type="text" name="newsId" value="${news.id}">
        </label>
    </form>
    <form action="${pageContext.request.contextPath}/deletenews?newsId=${news.id}" method="GET">
        <input type="submit" value="Delete"/>
        <input hidden="hidden" type="text" name="newsId" value="${news.id}">
    </form>
</c:if>
</body>
</html>

<%-- <jsp:useBean id="news" scope="request" type="com.github.alexeysa83.finalproject.model.News"/>--%>
<%--<jsp:useBean id="authUser" scope="session" type="com.github.alexeysa83.finalproject.model.AuthUser"/> --%>
