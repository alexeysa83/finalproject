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

<sec:authorize access="hasRole('ADMIN')" var="isAdmin"/>
<sec:authorize access="isAuthenticated()" var="isLoggedIn">
    <sec:authentication property="principal" var="userInSession"/>
</sec:authorize>

<h2><span align="center" style='color: blue;'>${news.title}</span></h2>
<hr/>
<h5>${news.content}</h5>
<hr/>
<h5><spring:message code="author"/>:
    <a href="${pageContext.request.contextPath}/user_infos/${news.authId}">
        ${news.authorNews}</a>
    <spring:message code="created"/>: ${news.creationTime}</h5>

<h5>
    <spring:message code="comments"/>: <span class="rating">${news.comments.size()} </span>
    <spring:message code="rating"/>:

    <c:if test="${!isLoggedIn  || news.authorNews == userInSession.login}">
    <span class="rating" style='color: ${news.ratingColour}'> ${news.ratingTotal}</c:if>

        <c:if test="${isLoggedIn && !(news.authorNews == userInSession.login)}">
    <c:choose>
        <c:when test="${news.userInSessionRateOnThisNews == null}">
            <a style="color: black"
               href="${pageContext.request.contextPath}/news/${news.id}/add_rating/${userInSession.id}?rate=1&authorId=${news.authId}">+</a>
            <span class="rating" style='color: ${news.ratingColour}'> ${news.ratingTotal} </span>
            <a style="color: black"
               href="${pageContext.request.contextPath}/news/${news.id}/add_rating/${userInSession.id}?rate=-1&authorId=${news.authId}">-</a>
        </c:when>
        <c:when test="${news.userInSessionRateOnThisNews == 1}">
            <a style="color: black"
               href="${pageContext.request.contextPath}/news/${news.id}/delete_rating/${userInSession.id}?authorId=${news.authId}">x</a>
            <span class="rating" style='color: ${news.ratingColour}'> ${news.ratingTotal} </span>
        </c:when>
        <c:when test="${news.userInSessionRateOnThisNews == -1}">
            <span class="rating" style='color: ${news.ratingColour}'> ${news.ratingTotal} </span>
            <a style="color: black"
               href="${pageContext.request.contextPath}/news/${news.id}/delete_rating/${userInSession.id}?authorId=${news.authId}">x</a>
        </c:when>
    </c:choose>
    </c:if>
</h5>

<c:if test="${news.authorNews == userInSession.login || isAdmin}">
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
    <hr/>
    <hr/>
</c:if>
<sec:authorize access="isAuthenticated()">
    <c:forEach items="${requestScope.commentList}" var="comment" varStatus="loop">
        <h5 style="color: #b04db2">${loop.index+1}) ${comment.content}</h5>
        <h6><spring:message code="author"/>:
            <a href="${pageContext.request.contextPath}/user_infos/${comment.authId}">${comment.authorComment}</a>

            <spring:message code="created"/>: ${comment.creationTime}

            <c:if test="${comment.authorComment == userInSession.login}">
            <span class="rating" style='color: ${comment.ratingColour}'> ${comment.ratingTotal}</c:if>

            <c:if test="${!(comment.authorComment == userInSession.login)}">
                <c:choose>
                    <c:when test="${comment.userInSessionRateOnThisComment == null}">
                        <a style="color: black"
                           href="${pageContext.request.contextPath}/comments/${comment.id}/add_rating/${userInSession.id}?rate=1&authorId=${comment.authId}&newsId=${comment.newsId}">+</a>
                                   <span class="rating"
                                         style='color: ${comment.ratingColour}'> ${comment.ratingTotal} </span>
                        <a style="color: black"
                           href="${pageContext.request.contextPath}/comments/${comment.id}/add_rating/${userInSession.id}?rate=-1&authorId=${comment.authId}&newsId=${comment.newsId}">-</a>
                    </c:when>
                    <c:when test="${comment.userInSessionRateOnThisComment == 1}">
                        <a style="color: black"
                           href="${pageContext.request.contextPath}/comments/${comment.id}/delete_rating/${userInSession.id}?authorId=${comment.authId}&newsId=${comment.newsId}">x</a>
                        <span class="rating" style='color: ${comment.ratingColour}'> ${comment.ratingTotal} </span>
                    </c:when>
                    <c:when test="${comment.userInSessionRateOnThisComment == -1}">
                        <span class="rating" style='color: ${comment.ratingColour}'> ${comment.ratingTotal} </span>
                        <a style="color: black"
                           href="${pageContext.request.contextPath}/comments/${comment.id}/delete_rating/${userInSession.id}?authorId=${comment.authId}&newsId=${comment.newsId}">x</a>
                    </c:when>
                </c:choose>
            </c:if>
        </h6>
        <hr/>

        <c:if test="${comment.authorComment == userInSession.login || isAdmin}">
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

