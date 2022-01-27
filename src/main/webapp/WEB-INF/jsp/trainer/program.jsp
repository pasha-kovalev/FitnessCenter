<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.epam.jwd.fitness_center.model.entity.UserRole" %>
<%--
  Created by IntelliJ IDEA.
  User: TokyoPashka
  Date: 13.01.2022
  Time: 16:57
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Программа</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <link href="../../../style/style.css" type="text/css" rel="stylesheet">
</head>
<style>
    .tab {
        display: none;
    }
</style>
<body class="form2">
<jsp:include page="../component/header.jsp" flush="true"/>
<form id="program" name="program-form"
        <c:if test="${not empty sessionScope.user}">
            <c:choose>
                <c:when test="${sessionScope.user.role eq UserRole.TRAINER}">
                    action="${pageContext.request.contextPath}/controller?command=make_program&orderId=${requestScope.order.id}"
                </c:when>
                <c:otherwise>
                    action="${pageContext.request.contextPath}/controller?command=update_program&orderId=${requestScope.order.id}"
                </c:otherwise>

            </c:choose>
        </c:if>
      method="post">
    <h1>Программа</h1>
    <div class="tooltip">Информация о клиенте
        <span class="tooltiptext">${requestScope.order.comment}</span>
    </div>
    <div class="tab">
        <label for="intensity">Интенсивность</label>
        <textarea id="intensity" name="intensity" rows="3" cols="33" maxlength="1000" required
                  oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">${requestScope.program.intensity}</textarea>

        <label for="schedule">Расписание</label>
        <textarea id="schedule" name="schedule" rows="5" cols="33" maxlength="1000" required
                  oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">${requestScope.program.schedule}</textarea>
    </div>
    <div class="tab">
        <label for="exercises">Упражнения</label>
        <textarea id="exercises" name="exercises" rows="10" cols="33" maxlength="3000" required
                  oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">${requestScope.program.exercises}</textarea>
    </div>
    <div class="tab">
        <label for="diet">Диета</label>
        <textarea id="diet" name="diet" rows="10" cols="33" maxlength="3000" required
                  oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">${requestScope.program.diet}</textarea>
    </div>
    <div class="tab">
        <label for="equipment">Оборудование</label>
        <textarea id="equipment" name="equipment" rows="10" cols="33" maxlength="1000" required
                  oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">${requestScope.program.equipment}</textarea>
    </div>
    <div style="overflow:auto; padding-top: 36px;">
        <div style="float:right;">
            <button type="button" id="prevBtn" onclick="nextPrev(-1)">Назад</button>
            <button type="button" id="nextBtn" onclick="nextPrev(1)">Дальше</button>
        </div>
    </div>
    <div style="text-align:center;margin-top:40px;">
        <span class="step"></span>
        <span class="step"></span>
        <span class="step"></span>
        <span class="step"></span>
    </div>
</form>
<jsp:include page="../component/footer.jsp" flush="true"/>
<script>
    var currentTab = 0;
    showTab(currentTab);

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
            document.getElementById("program").submit();
            return false;
        }
        showTab(currentTab);
    }

    function validateForm() {
        var x, i, valid = true;
        x = document.getElementsByClassName("tab");
        z = x[currentTab].getElementsByTagName("textarea");
        for (i = 0; i < z.length; i++) {
            if(typeof z[i] !== "undefined" && !z[i].checkValidity()) {
                z[i].className += " invalid"
                valid = false;
                z[i].reportValidity();
            }
            if (z[i].value == "") {
                z[i].className += " invalid";
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
