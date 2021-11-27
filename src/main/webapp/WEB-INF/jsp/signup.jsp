<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link href="style/signup.css" type="text/css" rel="stylesheet">
</head>
<body>
<div class="background">
    <form name="signup-form" action="${pageContext.request.contextPath}/controller?command=signup" method="post">
        <h3>Sign Up</h3>
        <p id="incorrect">${sessionScope.errorSignupMsg}</p>
        <label for="email">Email</label>
        <input id="email" type="email" name="login" placeholder="Email" pattern="[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,4}$">
        <label for="password1">Password</label>
        <input type="password" name="password" id="password1" autocomplete="new-password"
               required pattern="[0-9a-zA-Z]{6,20}" title="Enter a password consisting of 6-20 letters/digits">
        <label for="password2">Repeat password</label>
        <input type="password" name="password_repeat" id="password2" required pattern="[0-9a-zA-Z]{6,20}">
        <span id="mismatch"></span>
        <label for="firstname">First name:</label>
        <input id="firstname" type="text" name="firstname" required pattern="([А-Яа-я]{1,20})|([A-Z][a-z]{1,20})">
        <label for="lastname">Last name:</label>
        <input id="lastname" type="text" name="lastname" required pattern="([А-Яа-я]{1,20})|([A-Z][a-z]{1,20})">
        <input id="signup-button" type="submit" value="Sign Up" />
    </form>
</div>
<script src="/script/signup.js"></script>

</body>
</html>