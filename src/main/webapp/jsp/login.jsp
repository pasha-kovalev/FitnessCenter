<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="customtag" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale"/>
<fmt:message var="title" key="login.title"/>
<fmt:message var="loginLabel" key="login.label.login"/>
<fmt:message var="password" key="login.label.password"/>
<fmt:message var="button" key="login.button"/>
<html>
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link href="${pageContext.request.contextPath}/style/style.css" type="text/css" rel="stylesheet">
</head>
<body class="form1">
<jsp:include page="component/header.jsp" flush="true"/>
<div class="background">
    <form name="login-form" action="${pageContext.request.contextPath}/controller?command=login" method="post">
        <h3>${title}</h3>

        <p id="incorrect">
            <ct:pullSessionAttribute attribute="errorLoginMsg" name="msg"/>
            <c:if test="${not empty pageScope.msg}">
                <fmt:message key="${msg}"/>
            </c:if>
        </p>
        <label for="email">${loginLabel}</label>
        <input id="email" type="text" name="login" required>
        <label for="password">${password}</label>
        <input id="password" type="password" name="password" required>
        <input id="login-button" type="submit" value="${button}"/>
    </form>
</div>
<div>
    <jsp:include page="component/footer.jsp" flush="true"/>
</div>
</body>
</html>