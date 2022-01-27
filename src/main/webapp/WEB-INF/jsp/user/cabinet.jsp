<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.epam.jwd.fitness_center.model.entity.OrderStatus" %>
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
        #mainData h3 {
            margin-top: 16px;
            margin-bottom: 2px;
        }
        #mainData p {
            margin-block-start: 0;
            margin-block-end: 0;
            margin-inline-start: 0;
            margin-inline-end: 0;
        }

        footer {
            position: relative;
        }
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
        #send-button {
            background-color: #ffffffff;
            padding: 4px;
            border: 2px solid #000000;
            cursor: pointer;
        }
    </style>
</head>
<body onload="showOrders(true)">
<jsp:include page="../component/header.jsp" flush="true"/>
<nav class="w3-sidebar w3-collapse w3-white w3-animate-left" style="z-index:3;width:300px;margin-top: 46px" id="mySidebar"><br>
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
        <button class="w3-bar-item w3-button w3-padding" onclick="showOrders(true)">
            <i class="fa fa-bullseye fa-fw"></i> ${currentOrders}</button>
        <button class="w3-bar-item w3-button w3-padding" onclick="showOrders(false)">
            <i class="fa fa-history fa-fw"></i>${orders}</button>
    </div>
</nav>
<div class="w3-main" style="margin-left:300px;padding-top:90px; height: auto; padding-bottom: 50px; min-height: 95%">
    <div id="mainData"></div>
    <form id="reviewForm" name="review-form" method="post" style="visibility: hidden; position: absolute">
        <div>

            <textarea id="review" name="review" rows="2" cols="33" maxlength="1000" required
                      oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')"></textarea>
        </div>
        <div style="overflow:auto;">
            <div style="float:right;">
            <input id="send-button" type="submit" value="Отправить" />
            </div>
        </div>
    </form>
</div>
<div>
<jsp:include page="../component/footer.jsp" flush="true"/>
</div>
<script>
    var mySidebar = document.getElementById("mySidebar");
    var overlayBg = document.getElementById("myOverlay");
    var mainDataLoaded = false;
    var reviewLoaded = false;

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

    function showOrders(isActive) {
        document.getElementById("mainData").innerHTML = "";
        var isCurrentOrdersStr = isActive ? "true" : "false";
        jQuery.ajax({
            type : 'GET',
            url : '${pageContext.request.contextPath}/controller?command=show_user_orders&current=' + isCurrentOrdersStr,
            success : function(responseJson) {
                var $table = $("<table class=\"custom-table\">").appendTo($("#mainData"));
                $("<thead>").appendTo($table)
                    .append($("<tr><th>Дата заказа</th><th>Программа</th><th>Тренер</th><th>Цена</th>" +
                        "<th>Статус</th></tr>"));
                    $.each(responseJson, function(index, order) {
                        var lastTd = "";
                        switch (order.orderStatus) {
                            case "${OrderStatus.PAYMENT_AWAITING.name()}":
                                lastTd = "<a " +
                                    "href=\"${pageContext.request.contextPath}/controller?command=show_payment&orderId="
                                    + order.id +"\" class=\"btn btn-success\">Pay</a>" + `<a style="margin-left: 16px"` +
                                    "href=\"${pageContext.request.contextPath}/controller?command=cancel_order&orderId="
                                    + order.id +"\" class=\"btn btn-danger\">Отказаться</a>";
                                break;
                            case "${OrderStatus.PENDING_CLIENT.name()}":
                                lastTd = "<a " +
                                    "href=\"${pageContext.request.contextPath}/controller?command=show_program&orderId="
                                    + order.id +"\" class=\"btn btn-warning\">View</a>";
                                break;
                            case "${OrderStatus.ACTIVE.name()}":
                                lastTd = "<button class=\"btn btn-success\" onclick='showProgram(this)'>Open</button>";
                                break;
                            case "${OrderStatus.COMPLETED.name()}":
                                if(order.review == null) {
                                    lastTd = "<button class=\"btn btn-warning\" " +
                                        "onclick='showReview(this)'>Оставить отзыв</button>";
                                } else {
                                    lastTd = "";
                                }
                                break;
                            default:
                                lastTd = "";
                                break;
                        }
                        $("<tr>").appendTo($table)
                            .append($("<td>").text(order.creationDate.date['day'] + '-' +
                                                   order.creationDate.date['month'] + '-' +
                                                   order.creationDate.date['year']))
                            .append($("<td>").text(order.item['name']))
                            .append($("<td>").text(order.trainerName))
                            .append($("<td>").text(order.price))
                            .append($("<td>").text(order.orderStatus))
                            .append($(`<td class="orderIdTd" hidden>`).text(order.id))
                            .append($("<td>").append(lastTd));
                    });
            }
        })
    }

    function showProgram(el) {
        var orderId = $(el).closest("tr")
            .find(".orderIdTd")
            .text();
        document.getElementById("mainData").innerHTML = "";
        jQuery.ajax({
            type : 'GET',
            url : '${pageContext.request.contextPath}/controller?command=show_program_details&orderId=' + orderId,
            success : function(program) {
                $("#mainData").append($("<h1>").text("Программа"));
                var $div = $("<div style='padding-left: 100px'>").appendTo($("#mainData"));
                $div.append($("<p>").append("<span style='font-weight: bold'>Начало: </span>"
                                            + program.startsAt.date['day'] + '-' +
                                            program.startsAt.date['month'] + '-' +
                                            program.startsAt.date['year']))
                    .append($("<p>").append("<span style='font-weight: bold'>Окончание: </span>"
                        + program.endsAt.date['day'] + '-' +
                        program.endsAt.date['month'] + '-' +
                        program.endsAt.date['year']))
                    .append($("<h3>").text("ГРАФИК"))
                    .append($(`<p style="white-space: pre-line">`).text(program.schedule))
                    .append($("<h3>").text("ИНТЕНСИВНОСТЬ"))
                    .append($(`<p style="white-space: pre-line">`).text(program.intensity))
                    .append($("<h3>").text("СНАРЯЖЕНИЕ"))
                    .append($(`<p style="white-space: pre-line">`).text(program.equipment))
                    .append($("<h3>").text("УПРАЖНЕНИЯ"))
                    .append($(`<p style="white-space: pre-line">`).text(program.exercises))
                    .append($("<h3>").text("ДИЕТА"))
                    .append($(`<p style="white-space: pre-line">`).text(program.diet));

            }
        })
    }

    function showReview(el) {
        var form = document.getElementById("reviewForm");
        if(reviewLoaded) {
            form.style.visibility = 'hidden';
            reviewLoaded = false;
        } else {
            var buttonTd = $(el).closest("td");
            var orderId = $(el).closest("tr")
                .find(".orderIdTd")
                .text();
            form.action = '${pageContext.request.contextPath}/controller?command=send_review&orderId=' + orderId;
            var top = $(buttonTd).position().top + $(buttonTd).outerHeight();
            var left = $(el).position().left;
            $("#reviewForm").css({top: top, left: left});
            form.style.visibility = 'visible';
            reviewLoaded = true;
        }
    }
</script>
</body>
</html>