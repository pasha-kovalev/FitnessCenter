<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Login</title>
    <link href="style/login.css" type="text/css" rel="stylesheet">
</head>
<body>
<div class="background">
    <form name="login-form" action="${pageContext.request.contextPath}/controller?command=login" method="post">
        <h3>Login Here</h3>
        <p id="incorrect">${sessionScope.errorLoginMsg}</p>
        <label for="email">Email</label>
        <input id="email" type="text" name="login" placeholder="Email" >
        <label for="password">Password</label>
        <input id="password" type="password" name="password" placeholder="Password" >
        <input id="login-button" type="submit" value="Log in" />
    </form>
</div>
</body>
</html>