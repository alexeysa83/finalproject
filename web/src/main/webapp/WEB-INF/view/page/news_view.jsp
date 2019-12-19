<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>

<html>
<head>
    <title>News view</title>
</head>
<body>

<h2><span style='color: blue;'>${news.title}</span></h2>
<hr/>
<h5>${news.content}</h5>
<hr/>
<h5 style="color: #2bb239"><spring:message code="author"/>:
    <a href="${pageContext.request.contextPath}/user_infos/${news.authId}">
        ${news.authorNews}</a>
    <spring:message code="created"/>: ${news.creationTime}</h5>
<h5><spring:message code="comments"/>: ${requestScope.get('commentList').size()}</h5>

<sec:authorize access="hasRole('ADMIN')" var="isAdmin"/>
<sec:authorize access="isAuthenticated()">
    <sec:authentication property="principal.login" var="userInSession"/>
</sec:authorize>

<c:if test="${news.authorNews == userInSession || isAdmin}">
    <form action="${pageContext.request.contextPath}/news/${news.id}/to_news_update_form" method="GET">
        <input type="submit" value="<spring:message code="update.news"/>"/>
        <label>
            <input hidden="hidden" type="text" name="authorId" value="${news.authId}">
        </label>
    </form>
    <form action="${pageContext.request.contextPath}/news/${news.id}/delete" method="POST">
        <input type="submit" value="<spring:message code="delete.news"/>"/>
        <label>
            <input hidden="hidden" type="text" name="authorId" value="${news.authId}">
        </label>
    </form>
</c:if>
<hr/>
<hr/>
<sec:authorize access="isAuthenticated()">
    <c:forEach items="${requestScope.commentList}" var="comment" varStatus="loop">
        <h4 style="color: #b04db2">${loop.index+1}) ${comment.content}</h4>
        <h5><spring:message code="author"/>:
            <a href="${pageContext.request.contextPath}/user_infos/${comment.authId}">
                    ${comment.authorComment}</a></h5>
        <h5><spring:message code="created"/>: ${comment.creationTime}</h5>
        <hr/>

        <c:if test="${comment.authorComment == userInSession || isAdmin}">
            <c:choose>
                <c:when test="${comment.id == commentToUpdateId}">
                    <form action="${pageContext.request.contextPath}/comments/${comment.id}/update" method="POST">
                        <label for="content2"><strong><spring:message code="content"/></strong></label>
                        <textarea id="content2" name="content" rows="10">${comment.content}</textarea>
                        <input type="submit" value="<spring:message code="update.comment"/>"/>
                        <label>
                            <input hidden="hidden" type="text" name="newsId" value="${news.id}">
                            <input hidden="hidden" type="text" name="authorId" value="${comment.authId}">
                        </label>
                    </form>
                </c:when>
                <c:otherwise>
                    <form action="${pageContext.request.contextPath}/comments/${comment.id}" method="GET">
                        <input type="submit" value="<spring:message code="update.comment"/>"/>
                        <label>
                            <input hidden="hidden" type="text" name="newsId" value="${news.id}">
                            <input hidden="hidden" type="text" name="authorId" value="${comment.authId}">
                        </label>
                    </form>
                </c:otherwise>
            </c:choose>
            <form action="${pageContext.request.contextPath}/comments/${comment.id}/delete" method="POST">
                <input type="submit" value="<spring:message code="delete.comment"/>"/>
                <label>
                    <input hidden="hidden" type="text" name="newsId" value="${news.id}">
                    <input hidden="hidden" type="text" name="authorId" value="${comment.authId}">
                </label>
            </form>
        </c:if>
    </c:forEach>
    <form action="${pageContext.request.contextPath}/comments/" method="POST">
        <label for="content"><strong><spring:message code="content"/></strong></label>
        <textarea id="content" name="content" rows="10"></textarea>
        <input type="submit" value="<spring:message code="add.comment"/>"/>
        <label>
            <input hidden="hidden" type="text" name="newsId" value="${news.id}">
        </label>
    </form>
</sec:authorize>
</body>
</html>

