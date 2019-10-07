
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>News view</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<p><span style='color: blue;'>${news.id}</span></p>
<h2 style="color: #2bb239">Author: <a href="${pageContext.request.contextPath}/userpage?userPage=${news.authorLogin}">
    ${news.authorLogin}</a> Created: ${news.creationTime}</h2>
<h2>${news.content}</h2>
</body>
</html>
