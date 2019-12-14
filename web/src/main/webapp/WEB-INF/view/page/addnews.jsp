<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<html>
<head>
    <title>Add news</title>
</head>
<body>
<c:if test="${requestScope.get('message') != null}">
    <h2 style="color: firebrick">
        <spring:message code="${requestScope.get('message')}"/></h2>
</c:if>

<form action="${pageContext.request.contextPath}/news" method="POST">
    <label for="title"><strong><spring:message code="title"/></strong></label>
    <input id="title" type="text" name="title">
    <br/>
    <label for="content"><strong><spring:message code="content"/></strong></label>
    <textarea id="content" name="content" rows="10"></textarea>
    <button type="submit" class="color-square"><spring:message code="add.news"/></button>
</form>
</body>
</html>