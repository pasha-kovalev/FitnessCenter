<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="customtag" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale"/>
<fmt:message var="title" key="payment.title"/>
<fmt:message var="name" key="payment.label.name"/>
<fmt:message var="month01" key="payment.option.month01"/>
<fmt:message var="month02" key="payment.option.month02"/>
<fmt:message var="month03" key="payment.option.month03"/>
<fmt:message var="month04" key="payment.option.month04"/>
<fmt:message var="month05" key="payment.option.month05"/>
<fmt:message var="month06" key="payment.option.month06"/>
<fmt:message var="month07" key="payment.option.month07"/>
<fmt:message var="month08" key="payment.option.month08"/>
<fmt:message var="month09" key="payment.option.month09"/>
<fmt:message var="month10" key="payment.option.month10"/>
<fmt:message var="month11" key="payment.option.month11"/>
<fmt:message var="month12" key="payment.option.month12"/>
<fmt:message var="ccn" key="payment.label.ccn"/>
<fmt:message var="credit" key="payment.label.credit"/>
<fmt:message var="price" key="payment.info.price"/>
<fmt:message var="period" key="payment.info.period"/>
<fmt:message var="perMonth" key="payment.info.perMonth"/>
<fmt:message var="submit" key="payment.button.submit"/>
<fmt:message var="later" key="payment.button.later"/>
<fmt:message var="notValidTitle" key="payment.input.notValid"/>
<fmt:message var="month2" key="order.period.month2"/>
<fmt:message var="money" key="order.price.money"/>
<html>
<head>
    <title>${title}</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <link href="${pageContext.request.contextPath}/style/style.css" type="text/css" rel="stylesheet">
    <style>
        [type=text] {
            -webkit-appearance: button
        }

        a {
            text-decoration: none;
        }
    </style>
</head>
<body class="form2" onload="outPayment()">
<jsp:include page="../component/header.jsp" flush="true"/>
<form id="order" name="payment-form" action="${pageContext.request.contextPath}/controller?command=payment"
      method="post" style="top:45%">
    <h1>${title}</h1>
    <p id="incorrect">
        <ct:pullSessionAttribute attribute="errorPaymentMsg" name="msg"/>
        <c:if test="${not empty pageScope.msg}">
            <fmt:message key="${msg}"/>
        </c:if>
    </p>
    <div>
        <label for="ccn">${ccn}</label>
        <input id="ccn" name="cardNumber" type="tel" inputmode="numeric" pattern="[0-9]{16}"
               placeholder="1111222233334444" required
               oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">
        <label for="acceptCredit">
            <input type="checkbox" id="acceptCredit" name="acceptCredit" value="yes" onclick="outPayment()">${credit}
        </label>
        <p id="creditInfo" style="color: darkred"></p>
        <p>${price} <span id="price" style="color: green; font-weight: bold">${sessionScope.order.price}</span></p>
    </div>
    <div style="overflow:auto; padding-top: 36px;">
        <div style="float:right;">
            <a href="/controller?command=show_cabinet">
                <input id="refuse-button" type="text" value="${later}" style="width: 152px; text-align: center"/>
            </a>
            <input id="payment-button" type="submit" value="${submit}"/>
        </div>
    </div>
</form>
<jsp:include page="../component/footer.jsp" flush="true"/>
</body>
<script>
    var checkBox = document.getElementById("acceptCredit");
    var creditInfo = document.getElementById("creditInfo");
    var priceElem = document.getElementById("price");
    var price = parseFloat(${sessionScope.order.price});
    var creditPercentage = parseFloat(${sessionScope.creditPercentage});
    var creditPeriod = parseInt(${sessionScope.creditPeriod})


    function outPayment() {
        if (checkBox.checked == true) {
            var pricePerMonth = price * (1 + (creditPercentage / 100)) / creditPeriod;
            creditInfo.innerHTML = "${period} " + creditPeriod + " ${month2}. " + "${perMonth} " + pricePerMonth
                + " ${money}";
            priceElem.innerHTML = pricePerMonth + " ${money}";
            creditInfo.style.display = "block";
        } else {
            priceElem.innerHTML = price.toString() + " ${money}";
            creditInfo.style.display = "none";
        }
    }
</script>
</html>
