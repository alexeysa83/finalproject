<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
    <title>Main page</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<h2>Main page</h2>
<c:if test="${requestScope.get('error') != null}">
    <h2 style="color: firebrick">${requestScope.get('error')}</h2>
</c:if>

<c:if test="${sessionScope.get('authUser') != null}">
    <a href="${pageContext.request.contextPath}/addnews.jsp">Add news</a>
</c:if>
<!-- just for check -->
<c:if test="${requestScope.get('newsList') !=null}">
    <c:forEach items="${requestScope.newsList}" var="news">
        <h2>${news.id}</h2>
        <h2 style="color: #2bb239"><a href="${pageContext.request.contextPath}/newsview.jsp?newsId=${news.id}">
                ${news.title}</a></h2>
        <h2>${news.content}</h2>
        <h3>Author: <a href="${pageContext.request.contextPath}/userpage?userPage=${news.authorLogin}">
                ${news.authorLogin}</a></h3>
        <h3>Created: ${news.creationTime}</h3>
    </c:forEach>
</c:if>

<a href="${pageContext.request.contextPath}/userpage"
   onclick="
   <c:set var="userinfo" scope="request" value="12"/> ">User page</a>
<a href="${pageContext.request.contextPath}/adminpage">Admin page</a>
</body>
</html>
