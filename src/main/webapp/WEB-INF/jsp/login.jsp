<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Login</title>
    <link href="style/style.css" type="text/css" rel="stylesheet">
</head>
<body>
<div class="container" id="container">
    <div class="form-container sign-in-container">
        <form name="login-form" action="/controller?command=login" method="post">
            <h1 id="log-in-text">Log In</h1>
            <p id="incorrect">${errorLoginMsg}</p>
            <input id="login-input" type="text" name="login" placeholder="Username"/>
            <input id="password-input" type="password" name="pass" placeholder="Password" />
            <input type="submit" name="login" value="Log in" class="login-button"/>
        </form>
    </div>
</div>
</body>
</html>
