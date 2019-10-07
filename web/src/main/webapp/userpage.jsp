<%--
  userinfo page
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Userpage</title>
</head>
<body>
<jsp:include page="header.jsp"/>
<p><span style='color: blue;'>Hello, it is a restricted zone for authorized users!</span></p>
<h2>${userPage}</h2>
</body>
</html>
