<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Main page</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<h2>Main page</h2>
<c:if test="${requestScope.get('message') != null}">
    <h2 style="color: firebrick">${requestScope.get('message')}</h2>
</c:if>

<c:if test="${sessionScope.get('authUser') != null}">
    <a href="${pageContext.request.contextPath}/restricted/news/add">Add news</a>
</c:if>
<!-- just for check -->
<c:if test="${requestScope.get('newsList') !=null}">
    <c:forEach items="${requestScope.newsList}" var="news">
        <h2 style="color: #2bb239"><a href="${pageContext.request.contextPath}/news/view?newsId=${news.id}">
                ${news.title}</a></h2>
        <h2>${news.content}</h2>
        <h3>Author: <a href="${pageContext.request.contextPath}/restricted/user/profile?authId=${news.authId}">
                ${news.authorNews}</a></h3>
        <h3>Created: ${news.creationTime}</h3>
        <hr/>
    </c:forEach>
</c:if>

<%--<a href="${pageContext.request.contextPath}/userpage">User page</a>--%>
<%--<a href="${pageContext.request.contextPath}/adminpage">Admin page</a>--%>
</body>
</html>
