<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<html>
<head>
    <meta charset="utf-8">
    <title>Access denied</title>
</head>
<body>
    <h2 style="color: firebrick">
        <spring:message code="access.auth"/></h2>
</body>
</html>