<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="customtag" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale"/>
<fmt:message var="title" key="signup.title"/>
<fmt:message var="login" key="signup.login"/>
<fmt:message var="password" key="signup.password"/>
<fmt:message var="loginTitle" key="signup.email.input.title"/>
<fmt:message var="notValidTitle" key="signup.input.notValid"/>
<fmt:message var="passwordTitle" key="signup.password.input.title"/>
<fmt:message var="repeatPassword" key="signup.repeatPassword"/>
<fmt:message var="errorMismatch" key="signup.error.passwordMismatch"/>
<fmt:message var="firstname" key="signup.firstname"/>
<fmt:message var="lastname" key="signup.lastname"/>
<fmt:message var="button" key="signup.button"/>

<html>
<head>
    <title>${title}</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <link href="${pageContext.request.contextPath}/style/style.css" type="text/css" rel="stylesheet">
</head>
<body class="form1">
<jsp:include page="component/header.jsp" flush="true"/>
<div class="background ">
    <form id="signup" name="signup-form" action="${pageContext.request.contextPath}/controller?command=signup"
          method="post">
        <h3>${title}</h3>
        <p id="incorrect">
            <ct:pullSessionAttribute attribute="errorSignupMsg" name="msg"/>
            <c:if test="${not empty pageScope.msg}">
                <fmt:message key="${msg}"/>
            </c:if>
        </p>
        <label for="email">${login}</label>
        <input id="email" type="email" name="login" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$"
               oninvalid="setCustomValidity('${loginTitle}')" oninput="setCustomValidity('')">
        <label for="password1">${password}</label>
        <input type="password" name="password" id="password1" autocomplete="new-password"
               oninvalid="setCustomValidity('${passwordTitle}')" oninput="setCustomValidity('')"
               required pattern="[0-9a-zA-Z]{6,20}">
        <label for="password2">${repeatPassword}</label>
        <input type="password" name="password_repeat" id="password2" required pattern="[0-9a-zA-Z]{6,20}">
        <label for="firstname">${firstname}:</label>
        <input id="firstname" type="text" name="firstname" required pattern="([А-Я][а-я]{1,20})|([A-Z][a-z]{1,20})"
               oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">
        <label for="lastname">${lastname}:</label>
        <input id="lastname" type="text" name="lastname" required pattern="([А-Я][а-я]{1,20})|([A-Z][a-z]{1,20})"
               oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">
        <input id="signup-button" type="submit" value="${button}"/>
    </form>
</div>
<jsp:include page="component/footer.jsp" flush="true"/>
<script>
    var password1 = document.getElementById("password1");
    var password2 = document.getElementById("password2");

    password2.oninput = function (event) {
        if (password2.value != password1.value) {
            password2.setCustomValidity("${errorMismatch}");
        } else {
            password2.setCustomValidity("")
        }
    }
</script>
</body>
</html>