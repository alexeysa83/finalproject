<%@ page contentType="text/html;charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Add news</title>
</head>
<body>
<jsp:include page="header.jsp"/>

<form action="${pageContext.request.contextPath}/addnews" method="POST">
    <fieldset>
        <legend>News title</legend>
        <label for="title"><strong>News title</strong></label>
        <input id="title" type="text" name="title" required>
    </fieldset>
    <fieldset>
        <legend>Content</legend>
        <label for="content"><strong>Content</strong></label>
        <input id="content" type="text" name="content" required>
        <button type="submit" class="color-square">Add news</button>
    </fieldset>
</form>
</body>
</html>