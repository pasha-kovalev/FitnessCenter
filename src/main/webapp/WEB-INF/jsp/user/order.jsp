<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Заказ</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <link href="../../../style/style.css" type="text/css" rel="stylesheet">
</head>
<style>
    .tab {
        display: none;
    }
</style>
<body class="form2" onload="refreshPrice()">
<jsp:include page="../component/header.jsp" flush="true"/>
<form id="order" name="order-form" action="${pageContext.request.contextPath}/controller?command=order" method="post">
    <h1>Заказ</h1>
    <div class="tab">
        <label style="margin-top: 20px;" for="program">Выберите программу:</label>
        <select id="program" name="program" form="order">
            <c:forEach var="item" items="${requestScope.productList}" varStatus="loop">
                <option value="${item.id}"
                        price="${item.price}"
                        total="${requestScope.productListDiscount[loop.index].price}">
                        ${item.name}</option>
            </c:forEach>
        </select>
        <label for="period">Выберите срок:</label>
        <select id="period" name="period" form="order" required>
            <option value="1">1 месяц</option>
            <option value="2">2 месяца</option>
            <option value="3">3 месяца</option>
        </select>
        <c:if test="${sessionScope.userDetails == null || sessionScope.userDetails.personalTrainerId == null}">
            <label style="margin-top: 20px;" for="trainer">Выберите личного тренера:</label>
            <select id="trainer" name="trainer" form="order">
                <c:forEach var="trainer" items="${requestScope.trainerList}" >
                    <option value="${trainer.id}">${trainer.firstName} ${trainer.secondName}</option>
                </c:forEach>
            </select>
        </c:if>
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
    </div>
    <div class="tab"><p style="text-align: center">Информация для тренера</p>
        <label for="weight">Вес</label>
        <input id="weight" name="weight" type="number" step="1" min="25" max="500" required
               oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">
        <label for="height">Рост</label>
        <input id="height" name="height" type="number" step="1" min="50" max="250" required
               oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">
        <label for="comment">Расскажите нам о доступном снаряжении и своём опыте:</label>
        <textarea id="comment" name="comment" rows="4" cols="33" maxlength="1000" required
                  oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')"></textarea>
    </div>
    <div style="overflow:auto; padding-top: 36px;">
        <div style="float:right;">
            <button type="button" id="prevBtn" onclick="nextPrev(-1)">Previous</button>
            <button type="button" id="nextBtn" onclick="nextPrev(1)">Next</button>
        </div>
    </div>
    <div style="text-align:center;margin-top:40px;">
        <span class="step"></span>
        <span class="step"></span>
    </div>
</form>
<jsp:include page="../component/footer.jsp" flush="true"/>
<script>
    var currentTab = 0;
    var program = document.getElementById("program");
    var periods = document.getElementById("period");
    var price = document.getElementById("price");
    var totalPrice = document.getElementById("total");

    showTab(currentTab);

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
        if(program.options[program.selectedIndex].text === 'transformation') {
            price.innerHTML = parseFloat(program.options[program.selectedIndex].getAttribute("price")) + " ";
            totalPrice.innerHTML = parseFloat(program.options[program.selectedIndex].getAttribute("total")) + " ";
        } else {
            price.innerHTML = parseFloat(program.options[program.selectedIndex].getAttribute("price")) *
                parseFloat(periods.options[periods.selectedIndex].getAttribute("value")) + " ";
            totalPrice.innerHTML = parseFloat(program.options[program.selectedIndex].getAttribute("total")) *
                parseFloat(periods.options[periods.selectedIndex].getAttribute("value")) + " ";
        }

    }
    function showTab(n) {
        var x = document.getElementsByClassName("tab");
        x[n].style.display = "block";
        if (n == 0) {
            document.getElementById("prevBtn").style.display = "none";
        } else {
            document.getElementById("prevBtn").style.display = "inline";
        }
        if (n == (x.length - 1)) {
            document.getElementById("nextBtn").innerHTML = "Submit";
        } else {
            document.getElementById("nextBtn").innerHTML = "Next";
        }
        fixStepIndicator(n)
    }

    function nextPrev(n) {
        var x = document.getElementsByClassName("tab");
        if (n == 1 && !validateForm()) return false;
        x[currentTab].style.display = "none";
        currentTab = currentTab + n;
        if (currentTab >= x.length) {
            document.getElementById("order").submit();
            return false;
        }
        showTab(currentTab);
    }

    function validateForm() {
        var x, y, i, valid = true;
        x = document.getElementsByClassName("tab");
        y = x[currentTab].getElementsByTagName("input");
        z = x[currentTab].getElementsByTagName("textarea");
        for (i = 0; i < y.length; i++) {
            if(!y[i].checkValidity()) {
                y[i].className += " invalid"
                valid = false;
                y[i].reportValidity();
            } else if(typeof z.comment !== "undefined"
                && !z.comment.checkValidity()) {
                y[i].className += " invalid"
                valid = false;
                z[i].reportValidity();
            }

            if (y[i].value == "") {
                y[i].className += " invalid";
                valid = false;
            }
        }
        if (valid) {
            document.getElementsByClassName("step")[currentTab].className += " finish";
        }
        return valid;
    }

    function fixStepIndicator(n) {
        var i, x = document.getElementsByClassName("step");
        for (i = 0; i < x.length; i++) {
            x[i].className = x[i].className.replace(" active", "");
        }
        x[n].className += " active";
    }
</script>
</body>
</html>
