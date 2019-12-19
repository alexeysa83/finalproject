<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<html>
<head>
    <title>Update news</title>
</head>
<body>

<form action="${pageContext.request.contextPath}/news/${news.id}/update" method="POST">
    <label for="title"><strong><spring:message code="title"/></strong></label>
    <input id="title" type="text" name="title" value="${news.title}">
    <br/>
    <label for="content"><strong><spring:message code="content"/></strong></label>
    <textarea id="content" name="content" rows="10">${news.content}</textarea>
    <br/>
    <input type="reset" value="<spring:message code="reset"/>">
    <button type="submit" class="color-square"><spring:message code="update.news"/></button>
    <label>
        <input hidden="hidden" type="text" name="authorId" value="${news.authId}">
    </label>
</form>
</body>
</html>
