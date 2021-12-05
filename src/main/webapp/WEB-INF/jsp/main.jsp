<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="locale" />
<fmt:message var="title" key="brand.name"/>
<fmt:message var="contactTitle" key="main.contact.title"/>
<fmt:message var="location" key="main.contact.location"/>
<fmt:message var="phone" key="main.contact.phone"/>
<fmt:message var="email" key="main.contact.email"/>

<html>
<title>${title}</title>
<meta charset="UTF-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link href="../../style/style.css" type="text/css" rel="stylesheet">
<body>
<jsp:include page="component/header.jsp" flush="true"/>
<div class="w3-content" style="max-width:2000px;margin-top:46px">
    <div class="w3-display-container w3-center">
        <img src="../../images/gym2.png" style="width:100%">
    </div>

    <!-- Photo grid (modal) -->
    <div class="w3-main" style="background-color: #F5F5F5; color: black">
        <div class="w3-row-padding" style="text-align: center; padding-bottom: 60px ">
            <p style="font-size: 48px;  margin-bottom: 0; margin-top: 10px; font-weight: bold; font-family: 'PT serif';">MEET OUR PLANS</p>
            <div class="w3-half" style="font-size: 24px; font-weight: 600;font-family: 'Open Sans'; ">
                <p style="margin-bottom: 0;">PERSONAL ONLINE FITNESS</p>
                <p style="margin: 0">AND NUTRITION PLAN</p>
                <img src="../../images/crossfit.jpg" style="width:70%; padding-top: 8px">
            </div>
            <div class="w3-half" style="text-align: center; font-size: 24px; font-weight: 600; font-family: 'Open Sans';">
                <p style="margin-bottom: 0;">UNIQUE ONE-ON-ONE PROGRAM</p>
                <p style="margin: 0">CHANGE YOUR LIFE IN 90 DAYS</p>
                <img src="../../images/beach.jpg" style="width:70%; padding-top: 8px ">
            </div>
            <div class="w3-half" style="padding-top: 32px">
                <a class="w3-button w3-black w3-padding-large w3-hover-black"
                   href="${pageContext.request.contextPath}/controller?command=show_programs">See more</a>
            </div>
            <div class="w3-half" style="padding-top: 32px">
                <a class="w3-button w3-black w3-padding-large w3-hover-black"
                   href="${pageContext.request.contextPath}/controller?command=show_transform_program">See more</a>
            </div>
        </div>
    </div>
    <div class="w3-container w3-content w3-padding-32" style="max-width:800px" id="contact">
        <h2 class="w3-wide w3-center">${contactTitle}</h2>
        <div class="w3-row w3-padding-8">
            <div class="w3-col m6 w3-large w3-margin-bottom">
                <i class="fa fa-map-marker" style="width:30px"></i>${location}<br>
                <i class="fa fa-phone" style="width:30px"></i>${phone} +375 29 777 77 77<br>
                <i class="fa fa-envelope" style="width:30px"> </i>${email} fcby@mail.ru<br>
            </div>
        </div>
    </div>
</div>

<jsp:include page="component/footer.jsp" flush="true"/>
</body>
</html>
