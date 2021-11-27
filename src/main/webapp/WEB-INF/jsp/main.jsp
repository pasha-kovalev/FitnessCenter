<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.epam.jwd.fitness_center.model.entity.UserRole" %>

<html lang="en">
<title>Fitness Center</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="stylesheet" href="https://www.w3schools.com/w3css/4/w3.css">
<link rel="stylesheet" href="https://fonts.googleapis.com/css?family=Lato">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<style>
    body {font-family: "Lato", sans-serif}
    .mySlides {display: none}
</style>
<body>

<!-- Navbar -->
<div class="w3-top">
    <div class="w3-bar w3-black w3-card">
        <a href="#" class="w3-bar-item w3-button w3-padding-large">HOME</a>
        <c:if test="${not empty sessionScope.user && sessionScope.user.role eq UserRole.ADMIN}">

        </c:if>
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/controller?command=log_out"
                   class="w3-padding-large w3-hover-red w3-hide-small w3-right">Log out</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/controller?command=show_login"
                   class="w3-padding-large w3-hover-red w3-hide-small w3-right">Log in</a>
                <a href="${pageContext.request.contextPath}/controller?command=show_signup" class="w3-padding-large w3-hover-red w3-hide-small w3-right">Sign Up</a>
            </c:otherwise>
        </c:choose>

    </div>
</div>

<!-- Page content -->
<div class="w3-content" style="max-width:2000px;margin-top:46px">
    <!-- Automatic Slideshow Images -->
    <div class="mySlides w3-display-container w3-center">
        <img src="images/gym2.png" style="width:100%">
    </div>
<%--    <div class="mySlides w3-display-container w3-center">
        <img src="images/gym2.png" style="width:100%">
    </div>
    <div class="mySlides w3-display-container w3-center">
        <img src="images/gym3.jpg" style="width:100%">--%>
    </div>

    <!-- The Contact Section -->
    <div class="w3-container w3-content w3-padding-32" style="max-width:800px" id="contact">
        <h2 class="w3-wide w3-center">CONTACT</h2>
        <div class="w3-row w3-padding-8">
            <div class="w3-col m6 w3-large w3-margin-bottom">
                <i class="fa fa-map-marker" style="width:30px"></i> Minsk, BY<br>
                <i class="fa fa-phone" style="width:30px"></i> Phone: +375 29 777 77 77<br>
                <i class="fa fa-envelope" style="width:30px"> </i> Email: fcby@mail.ru<br>
            </div>
        </div>
    </div>

    <!-- End Page Content -->
</div>

<!-- Footer -->
<footer class="w3-container w3-padding-64 w3-center w3-opacity w3-light-grey w3-xlarge">
    <p class="w3-medium">By Pavel Kovalev</p>
</footer>

<script>
    // Automatic Slideshow - change image every 4 seconds
    var myIndex = 0;
    carousel();

    function carousel() {
        var i;
        var x = document.getElementsByClassName("mySlides");
        for (i = 0; i < x.length; i++) {
            x[i].style.display = "none";
        }
        myIndex++;
        if (myIndex > x.length) {myIndex = 1}
        x[myIndex-1].style.display = "block";
        setTimeout(carousel, 4000);
    }
</script>

</body>
</html>


<%--<html>
<head>
    <title>Main Page</title>
</head>
<body>
<h3>Fitness Center</h3>
<a href="/controller?command=show_users">users page</a>
<br/>
<a href="/controller?command=show_login">Login</a>
</body>
</html>--%>
