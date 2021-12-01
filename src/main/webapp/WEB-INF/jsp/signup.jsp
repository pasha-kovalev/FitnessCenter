<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Sign Up</title>
    <link href="../../style/style.css" type="text/css" rel="stylesheet">
</head>
<body class="form1">
<jsp:include page="component/header.jsp" flush="true"/>
<div class="background ">
    <form id="signup" name="signup-form" action="${pageContext.request.contextPath}/controller?command=signup" method="post">
        <h3>Sign Up</h3>
        <p id="incorrect">${sessionScope.errorSignupMsg}</p>
        <label for="email">Email</label>
        <input id="email" type="email" name="login" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$">
        <label for="password1">Password</label>
        <input type="password" name="password" id="password1" autocomplete="new-password"
               required pattern="[0-9a-zA-Z]{6,20}" title="Enter a password consisting of 6-20 letters/digits">
        <label for="password2">Repeat password</label>
        <input type="password" name="password_repeat" id="password2" required pattern="[0-9a-zA-Z]{6,20}">
        <label for="firstname">Firstname:</label>
        <input id="firstname" type="text" name="firstname" required pattern="([А-Яа-я]{1,20})|([A-Z][a-z]{1,20})">
        <label for="lastname">Lastname:</label>
        <input id="lastname" type="text" name="lastname" required pattern="([А-Яа-я]{1,20})|([A-Z][a-z]{1,20})">
        <input id="signup-button" type="submit" value="Sign Up" />
    </form>
</div>
<jsp:include page="component/footer.jsp" flush="true"/>
<script src="../../script/signup.js"></script>
</body>
</html>