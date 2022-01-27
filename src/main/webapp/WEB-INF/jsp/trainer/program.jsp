<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.epam.jwd.fitness_center.model.entity.UserRole" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale"/>
<fmt:message var="notValidTitle" key="payment.input.notValid"/>
<fmt:message var="title" key="view.program.title"/>
<fmt:message var="clientInfo" key="view.program.clientInfo"/>
<fmt:message var="intensity" key="view.program.intensity"/>
<fmt:message var="schedule" key="view.program.schedule"/>
<fmt:message var="exercises" key="view.program.exercises"/>
<fmt:message var="diet" key="view.program.diet"/>
<fmt:message var="equipment" key="view.program.equipment"/>
<fmt:message var="previous" key="order.button.previous"/>
<fmt:message var="next" key="order.button.next"/>
<fmt:message var="submit" key="button.submit"/>
<html>
<head>
    <title>${title}</title>
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
    <div class="tooltip">${clientInfo}
        <span class="tooltiptext">${requestScope.order.comment}</span>
    </div>
    <div class="tab">
        <label for="intensity">${intensity}</label>
        <textarea id="intensity" name="intensity" rows="3" cols="33" maxlength="1000" required
                  oninvalid="setCustomValidity('${notValidTitle}')"
                  oninput="setCustomValidity('')">${requestScope.program.intensity}</textarea>

        <label for="schedule">${schedule}</label>
        <textarea id="schedule" name="schedule" rows="5" cols="33" maxlength="1000" required
                  oninvalid="setCustomValidity('${notValidTitle}')"
                  oninput="setCustomValidity('')">${requestScope.program.schedule}</textarea>
    </div>
    <div class="tab">
        <label for="exercises">${exercises}</label>
        <textarea id="exercises" name="exercises" rows="10" cols="33" maxlength="3000" required
                  oninvalid="setCustomValidity('${notValidTitle}')"
                  oninput="setCustomValidity('')">${requestScope.program.exercises}</textarea>
    </div>
    <div class="tab">
        <label for="diet">${diet}</label>
        <textarea id="diet" name="diet" rows="10" cols="33" maxlength="3000" required
                  oninvalid="setCustomValidity('${notValidTitle}')"
                  oninput="setCustomValidity('')">${requestScope.program.diet}</textarea>
    </div>
    <div class="tab">
        <label for="equipment">${equipment}</label>
        <textarea id="equipment" name="equipment" rows="10" cols="33" maxlength="1000" required
                  oninvalid="setCustomValidity('${notValidTitle}')"
                  oninput="setCustomValidity('')">${requestScope.program.equipment}</textarea>
    </div>
    <div style="overflow:auto; padding-top: 36px;">
        <div style="float:right;">
            <button type="button" id="prevBtn" onclick="nextPrev(-1)">${previous}</button>
            <button type="button" id="nextBtn" onclick="nextPrev(1)">${next}</button>
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
            document.getElementById("nextBtn").innerHTML = "${submit}";
        } else {
            document.getElementById("nextBtn").innerHTML = "${next}";
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
            if (typeof z[i] !== "undefined" && !z[i].checkValidity()) {
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
