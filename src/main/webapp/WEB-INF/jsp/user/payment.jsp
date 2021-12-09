<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="ct" uri="customtag" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Оплата</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <link href="../../../style/style.css" type="text/css" rel="stylesheet">
</head>
<style>

</style>
<body class="form2" onload="outPayment()">
<jsp:include page="../component/header.jsp" flush="true"/>
<form id="order" name="payment-form" action="${pageContext.request.contextPath}/controller?command=pay" method="post">
    <h1>Оплата</h1>
    <div>
        <label for="name">Имя держателя</label>
        <input id="name" type="text" maxlength="20" required
               oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">
        <label for="cvv">CVV</label>
        <input id="cvv" type="text" pattern="[0-9]{3}"  type="text" required
               oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">
        <label>Срок</label>
        <select>
            <option value="01">January</option>
            <option value="02">February </option>
            <option value="03">March</option>
            <option value="04">April</option>
            <option value="05">May</option>
            <option value="06">June</option>
            <option value="07">July</option>
            <option value="08">August</option>
            <option value="09">September</option>
            <option value="10">October</option>
            <option value="11">November</option>
            <option value="12">December</option>
        </select>
        <select>
            <option value="21"> 2021</option>
            <option value="22"> 2022</option>
            <option value="23"> 2023</option>
            <option value="24"> 2024</option>
            <option value="25"> 2025</option>
        </select>
        <label for="ccn">Номер карты:</label>
        <input id="ccn" type="tel" inputmode="numeric" pattern="[0-9]{16}" placeholder="1111222233334444" required
               oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">
        <label for="accept">
            <input type="checkbox" id="accept" name="accept" value="yes" onclick="outPayment()"> Кредит
        </label>
        <p id="creditInfo" style="color: darkred"></p>
        <p>К оплате: <span id="price" style="color: green; font-weight: bold">${sessionScope.order.price}</span></p>
    </div>
    <div style="overflow:auto; padding-top: 36px;">
        <div style="float:right;">
            <input id="payment-button" type="submit" value="Оплатить" />
        </div>
    </div>
</form>
<jsp:include page="../component/footer.jsp" flush="true"/>
</body>
<script>
    var checkBox = document.getElementById("accept");
    var creditInfo = document.getElementById("creditInfo");
    var priceElem = document.getElementById("price");
    var price = parseFloat(${sessionScope.order.price});

    function outPayment() {
        if (checkBox.checked == true) {
            var pricePerMonth = price * 1.05 / 3;
            creditInfo.innerHTML = "На три месяца. " + "В месяц: " + pricePerMonth + " BYN";
            priceElem.innerHTML = pricePerMonth + " BYN";
            creditInfo.style.display = "block";
        } else {
            priceElem.innerHTML = price.toString();
            creditInfo.style.display = "none";
        }
    }
</script>
</html>
