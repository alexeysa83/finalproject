<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
    <title>Main page</title>
</head>
<body>

<sec:authorize access="isAuthenticated()">
    <a href="${pageContext.request.contextPath}/news/forward_to_add_news_form">
        <spring:message code="add.news"/></a>
</sec:authorize>

<c:forEach items="${requestScope.newsList}" var="news">
    <h2 style="color: #2bb239"><a href="${pageContext.request.contextPath}/news/${news.id}">
            ${news.title}</a></h2>
    <h6>${news.content}</h6>
    <h5><spring:message code="author"/>:
        <a href="${pageContext.request.contextPath}/user_infos/${news.authId}">
                ${news.authorNews}</a></h5>
    <h5><spring:message code="created"/>: ${news.creationTime}</h5>
    <%--    comments--%>
    <%--    comments--%>
    <h5><spring:message code="comments"/>: ${news.comments.size()}</h5>
    <hr/>
</c:forEach>

<nav aria-label="...">
    <ul class="pagination">
        <c:if test="${currentPage > 1}">
            <li class="page-item">
                <a class="page-link"
                   href="${pageContext.request.contextPath}/news/all?currentPage=${currentPage-1}"
                   aria-label="Previous">
                    <span aria-hidden="true">&laquo;</span>
                </a>
            </li>
        </c:if>
        <c:forEach begin="1" end="${totalPages}" var="page">
            <c:choose>
                <c:when test="${currentPage == page}">
                    <li class="page-item active" aria-current="page">
                        <a class="page-link">${page}<span class="sr-only">(current)</span></a>
                    </li>
                </c:when>
                <c:otherwise>
                    <li class="page-item">
                        <a class="page-link"
                           href="${pageContext.request.contextPath}/news/all?currentPage=${page}">${page}</a></li>
                </c:otherwise>
            </c:choose>
        </c:forEach>
        <c:if test="${currentPage < totalPages}">
            <li class="page-item">
                <a class="page-link"
                   href="${pageContext.request.contextPath}/news/all?currentPage=${currentPage+1}"
                   aria-label="Next">
                    <span aria-hidden="true">&raquo;</span>
                </a>
            </li>
        </c:if>
    </ul>
</nav>
</body>
</html>
