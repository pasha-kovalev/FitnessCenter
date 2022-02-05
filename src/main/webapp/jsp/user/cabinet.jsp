<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.epam.jwd.fitness_center.model.entity.OrderStatus" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale"/>
<fmt:message var="title" key="user.cabinet.title"/>
<fmt:message var="welcome" key="user.cabinet.welcome"/>
<fmt:message var="currentOrders" key="user.cabinet.currentOrders"/>
<fmt:message var="personalTrainer" key="user.cabinet.personalTrainer"/>
<fmt:message var="orders" key="user.cabinet.orders"/>
<fmt:message var="settings" key="user.cabinet.settings"/>
<fmt:message var="notValidTitle" key="payment.input.notValid"/>
<fmt:message var="date" key="user.cabinet.th.date"/>
<fmt:message var="program" key="user.cabinet.th.program"/>
<fmt:message var="trainer" key="user.cabinet.th.trainer"/>
<fmt:message var="price" key="user.cabinet.th.price"/>
<fmt:message var="status" key="user.cabinet.th.status"/>
<fmt:message var="refuse" key="user.cabinet.th.refuse"/>
<fmt:message var="pay" key="user.cabinet.th.pay"/>
<fmt:message var="view" key="user.cabinet.th.view"/>
<fmt:message var="open" key="user.cabinet.th.open"/>
<fmt:message var="review" key="user.cabinet.th.review"/>
<fmt:message var="begin" key="user.cabinet.th.begin"/>
<fmt:message var="end" key="user.cabinet.th.end"/>
<fmt:message var="intensity" key="user.cabinet.h3.intensity"/>
<fmt:message var="schedule" key="user.cabinet.h3.schedule"/>
<fmt:message var="exercises" key="user.cabinet.h3.exercises"/>
<fmt:message var="diet" key="user.cabinet.h3.diet"/>
<fmt:message var="equipment" key="user.cabinet.h3.equipment"/>

<html>
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link href="${pageContext.request.contextPath}/style/style.css" type="text/css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-latest.min.js"></script>
    <style>
        footer {
            position: relative;
        }

        th {
            text-align: inherit;
        }
    </style>
</head>
<body onload="showOrders(true)">
<jsp:include page="../component/header.jsp" flush="true"/>
<nav class="w3-sidebar w3-collapse w3-white w3-animate-left" style="z-index:3;width:300px;margin-top: 46px"
     id="mySidebar"><br>
    <div class="w3-container w3-row">
        <div class="w3-col s2">
            <i class="fa fa-user-circle fa-2x w3-margin-right" style="width:46px"></i>
        </div>
        <div class="w3-col s8 w3-bar">
            <span>${welcome} <strong>${sessionScope.user.firstName}</strong></span><br>
        </div>
    </div>
    <hr>
    <div class="w3-padding">
        <span>Тренер: <strong>${sessionScope.trainerEmail}</strong></span><br>
    </div>
    <div class="w3-bar-block">
        <button class="w3-bar-item w3-button w3-padding" onclick="showOrders(true)">
            <i class="fa fa-bullseye fa-fw"></i> ${currentOrders}</button>
        <button class="w3-bar-item w3-button w3-padding" onclick="showOrders(false)">
            <i class="fa fa-history fa-fw"></i>${orders}</button>
    </div>


</nav>
<div class="w3-main" style="margin-left:300px;padding-top:90px; height: auto; padding-bottom: 50px; min-height: 95%">
    <div id="mainData" class="userData"></div>
    <form id="reviewForm" name="review-form" method="get" style="visibility: hidden; position: absolute">
        <div>

            <textarea id="review" name="review" rows="2" cols="33" maxlength="1000" required
                      oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')"></textarea>
        </div>
        <div style="overflow:auto;">
            <div style="float:right;">
                <input id="send-button" type="submit" value="Отправить"/>
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

    function showOrders(isActive) {
        document.getElementById("mainData").innerHTML = "";
        var isCurrentOrdersStr = isActive ? "true" : "false";
        jQuery.ajax({
            type: 'GET',
            url: '${pageContext.request.contextPath}/controller?command=show_user_orders&current=' + isCurrentOrdersStr,
            success: function (responseJson) {
                var $table = $("<table class=\"custom-table\">").appendTo($("#mainData"));
                $("<thead>").appendTo($table)
                    .append($("<tr><th>${date}</th><th>${program}</th><th>${trainer}</th><th>${price}</th>" +
                        "<th>${status}</th></tr>"));
                $.each(responseJson, function (index, order) {
                    var lastTd = "";
                    switch (order.orderStatus) {
                        case "${OrderStatus.PAYMENT_AWAITING.name()}":
                            lastTd = "<a " +
                                "href=\"${pageContext.request.contextPath}/controller?command=show_payment&orderId="
                                + order.id + "\" class=\"btn btn-success\">${pay}</a>" + `<a style="margin-left: 16px"` +
                                "href=\"${pageContext.request.contextPath}/controller?command=cancel_order&orderId="
                                + order.id + "\" class=\"btn btn-danger\">${refuse}</a>";
                            break;
                        case "${OrderStatus.PENDING_CLIENT.name()}":
                            lastTd = "<a " +
                                "href=\"${pageContext.request.contextPath}/controller?command=show_program&orderId="
                                + order.id + "\" class=\"btn btn-warning\">${view}</a>";
                            break;
                        case "${OrderStatus.ACTIVE.name()}":
                            lastTd = "<button class=\"btn btn-success\" onclick='showProgram(this)'>${open}</button>";
                            break;
                        case "${OrderStatus.COMPLETED.name()}":
                            if (order.review == null) {
                                lastTd = "<button class=\"btn btn-warning\" " +
                                    "onclick='showReview(this)'>${review}</button>";
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
            type: 'POST',
            url: '${pageContext.request.contextPath}/controller?command=show_program_details&orderId=' + orderId,
            success: function (program) {
                $("#mainData").append($("<h1>").text("${program}"));
                var $div = $("<div style='padding-left: 100px'>").appendTo($("#mainData"));
                $div.append($("<p>").append("<span style='font-weight: bold'>${begin}: </span>"
                    + program.startsAt.date['day'] + '-' +
                    program.startsAt.date['month'] + '-' +
                    program.startsAt.date['year']))
                    .append($("<p>").append("<span style='font-weight: bold'>${end}: </span>"
                        + program.endsAt.date['day'] + '-' +
                        program.endsAt.date['month'] + '-' +
                        program.endsAt.date['year']))
                    .append($("<h3>").text("${schedule}"))
                    .append($(`<p style="white-space: pre-line">`).text(program.schedule))
                    .append($("<h3>").text("${intensity}"))
                    .append($(`<p style="white-space: pre-line">`).text(program.intensity))
                    .append($("<h3>").text("${equipment}"))
                    .append($(`<p style="white-space: pre-line">`).text(program.equipment))
                    .append($("<h3>").text("${exercises}"))
                    .append($(`<p style="white-space: pre-line">`).text(program.exercises))
                    .append($("<h3>").text("${diet}"))
                    .append($(`<p style="white-space: pre-line">`).text(program.diet));
            }
        })
    }

    function showReview(el) {
        var form = document.getElementById("reviewForm");
        if (reviewLoaded) {
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