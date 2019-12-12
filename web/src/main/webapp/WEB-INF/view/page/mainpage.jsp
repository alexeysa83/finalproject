<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${locale}"/>
<fmt:setBundle basename="messages" var="msgs"/>
<fmt:setBundle basename="interface" var="intr"/>
<html>
<head>
    <title>Main page</title>
</head>
<body>
<%--<jsp:include page="../common/header.jsp"/>--%>
<c:if test="${requestScope.get('message') != null}">
    <h2 style="color: firebrick">
        <fmt:message key="${requestScope.get('message')}" bundle="${msgs}"/></h2>
</c:if>

<c:if test="${sessionScope.get('authUser') != null}">
    <a href="${pageContext.request.contextPath}/news/addnewstojsp">
        <fmt:message key="add.news" bundle="${intr}"/></a>
</c:if>

<c:forEach items="${requestScope.newsList}" var="news">
    <h2 style="color: #2bb239"><a href="${pageContext.request.contextPath}/news/${news.id}">
            ${news.title}</a></h2>
    <h2>${news.content}</h2>
    <h3><fmt:message key="author" bundle="${intr}"/>:
        <a href="${pageContext.request.contextPath}/user_infos/${news.authId}">
                ${news.authorNews}</a></h3>
    <h3><fmt:message key="created" bundle="${intr}"/>: ${news.creationTime}</h3>
    <h4><fmt:message key="comments" bundle="${intr}"/>: 0</h4>
    <hr/>
</c:forEach>

<nav aria-label="...">
    <ul class="pagination">
        <c:if test="${currentPage > 1}">
            <li class="page-item">
                <a class="page-link"
                   href="${pageContext.request.contextPath}/news?currentPage=${currentPage-1}"
                   aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
        </c:if>
        <c:forEach begin="1" end="${totalPages}" var="page">
            <c:choose>
                <c:when test="${currentPage == page}">
                    <li class="page-item active" aria-current="page">
                        <a class="page-link" >${page}<span class="sr-only">(current)</span></a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/news?currentPage=${page}">${page}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:if test="${currentPage < totalPages}">
            <li class="page-item">
                <a class="page-link"
                   href="${pageContext.request.contextPath}/news?currentPage=${currentPage+1}"
                   aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        </c:if>
    </ul>
</nav>
</body>
</html>
