<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="locale" />
<fmt:message var="title" key="user.cabinet.title"/>
<fmt:message var="welcome" key="user.cabinet.welcome"/>
<fmt:message var="currentOrders" key="user.cabinet.currentOrders"/>
<fmt:message var="personalTrainer" key="user.cabinet.personalTrainer"/>
<fmt:message var="orders" key="user.cabinet.orders"/>
<fmt:message var="settings" key="user.cabinet.settings"/>

<html>
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link href="../../style/style.css" type="text/css" rel="stylesheet">
</head>
<body>
<nav class="w3-sidebar w3-collapse w3-white w3-animate-left" style="z-index:3;width:300px;" id="mySidebar"><br>
    <div class="w3-container w3-row">
        <div class="w3-col s2">
            <i class="fa fa-user-circle fa-2x w3-margin-right" style="width:46px"></i>
        </div>
        <div class="w3-col s8 w3-bar">
            <span>${welcome} <strong>${sessionScope.user.firstName}</strong></span><br>
        </div>
    </div>
    <hr>
    <div class="w3-bar-block">
        <a href="${pageContext.request.contextPath}/controller?command=show_user_active_orders"
           class="w3-bar-item w3-button w3-padding">
            <i class="fa fa-bullseye fa-fw"></i> ${currentOrders}</a>
        <a href="#" class="w3-bar-item w3-button w3-padding"><i class="fa fa-diamond fa-fw"></i>${personalTrainer}</a>
        <a href="#" class="w3-bar-item w3-button w3-padding"><i class="fa fa-history fa-fw"></i>${orders}</a>
        <a href="#" class="w3-bar-item w3-button w3-padding"><i class="fa fa-cog fa-fw"></i>${settings}</a><br><br>
    </div>
</nav>
<script>
    var mySidebar = document.getElementById("mySidebar");
    var overlayBg = document.getElementById("myOverlay");
    function w3_open() {
        if (mySidebar.style.display === 'block') {
            mySidebar.style.display = 'none';
            overlayBg.style.display = "none";
        } else {
            mySidebar.style.display = 'block';
            overlayBg.style.display = "block";
        }
    }

    function w3_close() {
        mySidebar.style.display = "none";
        overlayBg.style.display = "none";
    }
</script>
</body>
</html>