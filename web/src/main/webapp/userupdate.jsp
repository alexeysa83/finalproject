<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
    <title>Update user</title>
</head>
<body>
<jsp:include page="header.jsp"/>

<%--User deleted message in JSP for deleted users + do not show delete / update buttons--%>

<h2 style="color: firebrick">${requestScope.get('message')}</h2>

<c:if test="${authUser.login == user.userLogin || authUser.role == 'ADMIN'}">
    <form action="${pageContext.request.contextPath}/restricted/user/update" method="POST">
        <h2><span style='color: blue;'>${user.userLogin}</span></h2>
        <h2>Update user settings:</h2><br/>

        <br/> First name: <br/>
        <input type="text" name="firstName" value="${user.firstName}">

        <br/>Last name:<br/>
        <input type="text" name="lastName" value="${user.lastName}">

        <br/>Email:<br/>
        <input type="text" name="email" value="${user.email}">

        <br/>Phone:<br/>
        <input type="text" name="phone" value="${user.phone}">

        <label>
            <input hidden="hidden" type="text" name="authId" value="${user.authId}">
        </label>
        <input type="reset" value="Reset">
        <button type="submit" class="color-square">Update user</button>
    </form>

    <c:if test="${authUser.login == user.userLogin}">
        <h2>Update security settings:</h2><br/>
        <form action="${pageContext.request.contextPath}/restricted/authuseruser/update/login" method="POST">
            <br/>Login:<br/>
            <input type="text" name="login" value="${user.userLogin}" required>
            <label>
                <input hidden="hidden" type="text" name="authId" value="${user.authId}">
            </label>
            <button type="submit" class="color-square">Change login</button>
        </form>

        <form action="${pageContext.request.contextPath}/restricted/authuseruser/pass/update/password" method="POST">
            <br/>Old password:<br/>
            <input type="password" name="passwordOld" required>

            <br/>New password:<br/>
            <input type="password" name="passwordNew" required>

            <br/>Repeat new password:<br/>
            <input type="password" name="passwordRepeat" required>

            <label>
                <input hidden="hidden" type="text" name="authId" value="${user.authId}">
            </label>
            <button type="submit" class="color-square">Change password</button>
        </form>
    </c:if>

    <c:if test="${authUser.role == 'ADMIN'}">
        <form action="${pageContext.request.contextPath}/restricted/authuseruser/update/role" method="POST">
            <br/>Current role:<br/>
            <input type="radio" name="role" value="USER" required> User
            <input type="radio" name="role" value="ADMIN" required> Admin
            <label>
                <input hidden="hidden" type="text" name="authId" value="${user.authId}">
            </label>
            <button type="submit" class="color-square">Change role</button>
        </form>
    </c:if>

    <form action="${pageContext.request.contextPath}/restricted/authuser/delete" method="GET">
        <input type="submit" value="Delete user"/>
        <label>
            <input hidden="hidden" type="text" name="authId" value="${user.authId}">
            <input hidden="hidden" type="text" name="login" value="${user.userLogin}">
        </label>
    </form>
</c:if>

</body>
</html>
