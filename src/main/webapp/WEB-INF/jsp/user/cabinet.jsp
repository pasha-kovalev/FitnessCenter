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
    <script src="https://code.jquery.com/jquery-latest.min.js"></script>
    <style>
        .custom-table {
            min-width: 1100px;
            margin: 0 auto;
            color: #212529;
            font-family: "Roboto",-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,"Noto Sans",sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji";
            border-collapse: collapse;
        }

        .custom-table thead tr, .custom-table thead th {
            padding-bottom: 30px;
            border-top: none;
            border-bottom: none!important;
            color: #F5F5F5;
            font-size: 16px;
            text-transform: uppercase;
            letter-spacing: .2rem;
        }
        .custom-table thead th {
            vertical-align: bottom;
            border-bottom: 2px solid #dee2e6;
        }
        .custom-table th, .custom-table td {
            padding: .75rem;
            vertical-align: top;
            border-top: 1px solid #dee2e6;
        }
        th {
            text-align: inherit;
        }

        .custom-table tbody tr:nth-of-type(odd) {
            background-color: rgba(0,0,0,.2);
        }
        .custom-table tbody tr {
            -webkit-transition: .3s all ease;
            -o-transition: .3s all ease;
            transition: .3s all ease;
        }
        .custom-table tbody th, .custom-table tbody td {
            color: #DCDCDC;
            font-weight: 400;
            padding-bottom: 15px;
            padding-top: 15px;
            font-weight: 300;
            border: none;
            -webkit-transition: .3s all ease;
            -o-transition: .3s all ease;
            transition: .3s all ease;
        }
        .custom-table th, .custom-table td {
            padding: .75rem;
            vertical-align: top;
            border-top: 1px solid #dee2e6;
        }
    </style>
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
        <button class="w3-bar-item w3-button w3-padding" onclick="showActiveOrders()">
            <i class="fa fa-bullseye fa-fw"></i> ${currentOrders}</button>
        <a href="#" class="w3-bar-item w3-button w3-padding"><i class="fa fa-diamond fa-fw"></i>${personalTrainer}</a>
        <a href="#" class="w3-bar-item w3-button w3-padding"><i class="fa fa-history fa-fw"></i>${orders}</a>
        <a href="#" class="w3-bar-item w3-button w3-padding"><i class="fa fa-cog fa-fw"></i>${settings}</a><br><br>
    </div>
</nav>
<div class="w3-main" style="margin-left:300px;padding-top:90px; height: 95%">
    <div id="mainData"></div>
</div>
<script>
    var mySidebar = document.getElementById("mySidebar");
    var overlayBg = document.getElementById("myOverlay");
    var mainDataLoaded = false;

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

    function showActiveOrders() {
        if(mainDataLoaded) {
            document.getElementById("mainData").innerHTML = "";
            mainDataLoaded = false;
        }

        jQuery.ajax({
            type : 'GET',
            url : '${pageContext.request.contextPath}/controller?command=show_user_active_orders',
            success : function(responseJson) {
                var $table = $("<table class=\"custom-table\">").appendTo($("#mainData"));
                $("<thead>").appendTo($table)
                    .append($("<tr><th>Дата заказа</th><th>Программа</th><th>Тренер</th><th>Цена</th>" +
                        "<th>Статус</th></tr>"));
                    $.each(responseJson, function(index, order) {
                        $("<tr>").appendTo($table)
                            .append($("<td>").text(order.creationDate.date['day'] + '-' +
                                                   order.creationDate.date['month'] + '-' +
                                                   order.creationDate.date['year']))
                            .append($("<td>").text(order.item['name']))
                            .append($("<td>").text(order.trainerName))
                            .append($("<td>").text(order.price))
                            .append($("<td>").text(order.orderStatus));
                    });
            }
        });
        mainDataLoaded = true;
    }

</script>
</body>
</html>