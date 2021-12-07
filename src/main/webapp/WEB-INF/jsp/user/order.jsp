<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Заказ</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <link href="../../../style/style.css" type="text/css" rel="stylesheet">
</head>
<style>
    textarea {
        resize: none;
    }
    select {
        color: black;
    }
</style>
<body class="form1" onload="refreshPrice()">
<%--
вес. рост. возраст .опыт. доступное снаряжение. передавать id товара при нажатии на оформить
вывод цены + скидка если
--%>

<jsp:include page="../component/header.jsp" flush="true"/>
<div class="background ">
    <form id="order" name="order-form" action="${pageContext.request.contextPath}/controller?command=order" method="post">
        <h3>Заказ</h3>
        <%--<p id="incorrect">
            <ct:pullSessionAttribute attribute="errorSignupMsg" msg="msg"/>
            <c:if test="${pageScope.msg != null}">
                <fmt:message key="${msg}"/>
            </c:if>
        </p>--%>
        <label style="margin-top: 20px;" for="programs">Выберите программу:</label>
        <select id="programs" name="programs" form="order">
            <c:forEach var="item" items="${requestScope.productList}" varStatus="loop">
                <option value="${item.id}"
                        price="${item.price}"
                        total="${requestScope.productListDiscount[loop.index].price}">
                        ${item.name}</option>
            </c:forEach>
        </select>
        <label for="periods">Выберите срок:</label>
        <select id="periods" name="periods" form="order" required>
            <option value="1">1 месяц</option>
            <option value="2">2 месяца</option>
            <option value="3">3 месяца</option>
        </select>
        <label for="weight">Вес</label>
        <input id="weight" type="number" step="1" min="25" max="500" required>
        <label for="height">Рост</label>
        <input id="height" type="number" step="1" min="50" max="250" required>
        <label for="comment">Расскажите нам о доступном снаряжении и своём опыте:</label>
        <textarea id="comment" name="comment" rows="4" cols="33" maxlength="1000" required ></textarea>
        <p style="padding-top: 24px;font-weight: bold;">Итоговая цена:
            <c:choose>
                <c:when test="${requestScope.productListDiscount == null}">
                    <span id="price"></span>
                    <span>BYN</span>
                </c:when>
                <c:otherwise>
                    <span id="price" style="text-decoration: line-through;"></span>
                    <span id="total"></span>
                    <span>BYN</span>
                </c:otherwise>
            </c:choose>
        </p>

        <input id="signup-button" type="submit" value="Оформить" />
    </form>
</div>
<jsp:include page="../component/footer.jsp" flush="true"/>
<script defer>
    var program = document.getElementById("programs");
    var periods = document.getElementById("periods");
    var price = document.getElementById("price");
    var totalPrice = document.getElementById("total");

    program.onchange = function() {
        if(program.options[program.selectedIndex].text === 'transformation') {
            periods.value = "3";
            periods.disabled = true;
        } else {
            periods.disabled = false;
        }
        refreshPrice();
    }

    periods.onchange = function() {
        refreshPrice();
    }

    function refreshPrice() {
        price.innerHTML = parseFloat(program.options[program.selectedIndex].getAttribute("price")) *
            parseFloat(periods.options[periods.selectedIndex].getAttribute("value")) + " ";
        totalPrice.innerHTML = parseFloat(program.options[program.selectedIndex].getAttribute("total")) *
            parseFloat(periods.options[periods.selectedIndex].getAttribute("value")) + " ";
    }
</script>
</body>
</html>
