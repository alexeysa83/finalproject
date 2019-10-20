<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value= "${locale}"/>
<fmt:setBundle basename = "messages" var = "msgs"/>
<fmt:setBundle basename = "interface" var = "intr"/>
<html>
<head>
    <title>Main page</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<c:if test="${requestScope.get('message') != null}">
    <h2 style="color: firebrick">
        <fmt:message key="${requestScope.get('message')}" bundle="${msgs}"/></h2>
</c:if>

<c:if test="${sessionScope.get('authUser') != null}">
    <a href="${pageContext.request.contextPath}/auth/news/add">
        <fmt:message key="add.news" bundle="${intr}"/></a>
</c:if>
<!-- just for check -->
<c:if test="${requestScope.get('newsList') !=null}">
    <c:forEach items="${requestScope.newsList}" var="news">
        <h2 style="color: #2bb239"><a href="${pageContext.request.contextPath}/news/view?newsId=${news.id}">
                ${news.title}</a></h2>
        <h2>${news.content}</h2>
        <h3><fmt:message key="author" bundle="${intr}"/>:
            <a href="${pageContext.request.contextPath}/auth/user/view?authId=${news.authId}">
                ${news.authorNews}</a></h3>
        <h3><fmt:message key="created" bundle="${intr}"/>: ${news.creationTime}</h3>
        <h4><fmt:message key="messages" bundle="${intr}"/>: 0</h4>
        <hr/>
    </c:forEach>
</c:if>
</body>
</html>
