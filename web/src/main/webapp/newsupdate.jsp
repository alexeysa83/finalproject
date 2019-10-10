<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Update news</title>
</head>
<body>
<jsp:include page="header.jsp"/>

<h2 style="color: firebrick">${requestScope.get('message')}</h2>

<form action="${pageContext.request.contextPath}/restricted/news/update" method="POST">
    <label for="title"><strong>News title</strong></label>
    <input id="title" type="text" name="title" value="${news.title}">
    <br/>
    <label for="content"><strong>Content</strong></label>
    <textarea id="content" name="content" rows="10">${news.content}</textarea>
    <br/>
    <label>
        <input hidden="hidden" type="text" name="newsId" value="${news.id}">
    </label>
    <input type="reset" value="Reset">
    <button type="submit" class="color-square">Update news</button>
</form>
</body>
</html>
