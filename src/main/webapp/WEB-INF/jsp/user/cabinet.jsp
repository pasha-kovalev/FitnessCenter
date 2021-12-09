<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>Кабинет</title>
    <link href="../../style/style.css" type="text/css" rel="stylesheet">
</head>
<body>
<jsp:include page="user_header.jsp" flush="true"/>
<nav class="w3-sidebar w3-collapse w3-white w3-animate-left" style="z-index:3;width:300px;" id="mySidebar"><br>
    <div class="w3-container w3-row">
        <div class="w3-col s2">
            <i class="fa fa-user-circle fa-2x w3-margin-right" style="width:46px"></i>
        </div>
        <div class="w3-col s8 w3-bar">
            <span>Добро пожаловать, <strong>${sessionScope.user.firstName}</strong></span><br>
        </div>
    </div>
    <hr>
    <div class="w3-bar-block">
        <a href="#" class="w3-bar-item w3-button w3-padding"><i class="fa fa-bullseye fa-fw"></i> Текущие заказы</a>
        <a href="#" class="w3-bar-item w3-button w3-padding"><i class="fa fa-diamond fa-fw"></i>Личный тренер</a>
        <a href="#" class="w3-bar-item w3-button w3-padding"><i class="fa fa-history fa-fw"></i>Заказы</a>
        <a href="#" class="w3-bar-item w3-button w3-padding"><i class="fa fa-cog fa-fw"></i>Настройки</a><br><br>
    </div>
</nav>

<div class="w3-main" style="margin-left:300px;margin-top:43px; height: 95%"> </div>
<jsp:include page="../component/footer.jsp" flush="true"/>
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