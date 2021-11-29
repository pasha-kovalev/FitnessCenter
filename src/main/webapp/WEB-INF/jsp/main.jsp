<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.epam.jwd.fitness_center.model.entity.UserRole" %>

<html lang="en">
<title>Fitness Center</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="../../style/style.css" type="text/css" rel="stylesheet">
<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
<body>
<jsp:include page="component/header.jsp" flush="true"/>
<!-- Page content -->
<div class="w3-content" style="max-width:2000px;margin-top:46px">
    <!-- Automatic Slideshow Images -->
    <div class="w3-display-container w3-center">
        <img src="../../images/gym2.png" style="width:100%">
    </div>
<%--    <div class="mySlides w3-display-container w3-center">
        <img src="images/gym2.png" style="width:100%">
    </div>
    <div class="mySlides w3-display-container w3-center">
        <img src="images/gym3.jpg" style="width:100%">--%>
   <%-- </div>--%>

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
<jsp:include page="component/footer.jsp" flush="true"/>
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
