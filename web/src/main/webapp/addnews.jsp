<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Add news</title>
</head>
<body>
<jsp:include page="header.jsp"/>

<h2 style="color: firebrick">${requestScope.get('message')}</h2>

<form action="${pageContext.request.contextPath}/restricted/news/add" method="POST">
    <fieldset>
        <legend>News title</legend>
        <label for="title"><strong>News title</strong></label>
        <input id="title" type="text" name="title">
    </fieldset>
    <fieldset>
        <legend>Content</legend>
        <label for="content"><strong>Content</strong></label>
        <input id="content" type="text" name="content">
        <button type="submit" class="color-square">Add news</button>
    </fieldset>
</form>
</body>
</html>