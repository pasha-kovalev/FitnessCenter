<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.epam.jwd.fitness_center.model.entity.UserRole" %>
<%@ page import="com.epam.jwd.fitness_center.model.entity.ProgramStatus" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale"/>
<fmt:message var="title" key="view.program.title"/>
<fmt:message var="reject" key="view.program.reject"/>
<fmt:message var="comment" key="view.program.comment"/>
<fmt:message var="clientInfo" key="view.program.clientInfo"/>
<fmt:message var="intensity" key="view.program.intensity"/>
<fmt:message var="schedule" key="view.program.schedule"/>
<fmt:message var="exercises" key="view.program.exercises"/>
<fmt:message var="diet" key="view.program.diet"/>
<fmt:message var="equipment" key="view.program.equipment"/>
<fmt:message var="commentForTrainer" key="view.program.commentForTrainer"/>
<fmt:message var="refuse" key="view.program.refuse"/>
<fmt:message var="edit" key="view.program.edit"/>
<fmt:message var="send" key="view.program.send"/>
<fmt:message var="notValidTitle" key="payment.input.notValid"/>

<html>
<head>
    <title>${title}</title>
    <meta http-equiv="content-type" content="text/html; charset=utf-8">
    <link href="${pageContext.request.contextPath}/style/style.css" type="text/css" rel="stylesheet">
</head>
<style>
    footer {
        top: 160%;
        bottom: 0;
    }
</style>
<body class="form2">
<jsp:include page="component/header.jsp" flush="true"/>
<form id="program" name="program-form" style="top: 80%;"
      action="${pageContext.request.contextPath}/controller?command=update_program&orderId=${requestScope.order.id}"
      method="post">
    <h1>${title}</h1>
    <c:choose>
        <c:when test="${requestScope.program.programStatus eq ProgramStatus.REFUSED.name()}">
            <p style="color: red">${reject}</p>
            <p> ${comment}: ${requestScope.order.comment}</p>
        </c:when>
        <c:otherwise>
            <div class="tooltip">${clientInfo}
                <span class="tooltiptext">${requestScope.order.comment}</span>
            </div>
        </c:otherwise>
    </c:choose>
    <div>
        <input type="hidden" id="changeMarker" name="changeMarker" value="false">
        <label for="intensity">${intensity}</label>
        <textarea id="intensity" name="intensity" rows="3" cols="33" maxlength="1000" required readonly
                  oninvalid="setCustomValidity('${notValidTitle}')"
                  oninput="setCustomValidity('')">${requestScope.program.intensity}</textarea>

        <label for="schedule">${schedule}</label>
        <textarea id="schedule" name="schedule" rows="5" cols="33" maxlength="1000" required readonly
                  oninvalid="setCustomValidity('${notValidTitle}')"
                  oninput="setCustomValidity('')">${requestScope.program.schedule}</textarea>

        <label for="exercises">${exercises}</label>
        <textarea id="exercises" name="exercises" rows="10" cols="33" maxlength="3000" required readonly
                  oninvalid="setCustomValidity('${notValidTitle}')"
                  oninput="setCustomValidity('')">${requestScope.program.exercises}</textarea>

        <label for="diet">${diet}</label>
        <textarea id="diet" name="diet" rows="10" cols="33" maxlength="3000" required readonly
                  oninvalid="setCustomValidity('${notValidTitle}')"
                  oninput="setCustomValidity('')">${requestScope.program.diet}</textarea>

        <label for="equipment">${equipment}</label>
        <textarea id="equipment" name="equipment" rows="10" cols="33" maxlength="1000" required readonly
                  oninvalid="setCustomValidity('${notValidTitle}')"
                  oninput="setCustomValidity('')">${requestScope.program.equipment}</textarea>
        <label id="comment-label" for="comment" style="visibility: hidden;">${commentForTrainer}</label>
        <textarea id="comment" name="comment" rows="1" cols="33" maxlength="1000" style="visibility: hidden;"
                  oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')"></textarea>
    </div>
    <div style="overflow:auto; padding-top: 36px;">
        <div style="float:right;">
            <c:if test="${sessionScope.user.role ne UserRole.TRAINER}">
                <input id="refuse-button" type="button" value="${refuse}" onclick="refuseProgram()"/>
            </c:if>
            <input id="edit-button" type="button" value="${edit}" onclick="editForm()"/>
            <input id="send-button" type="submit" value="${send}"/>
        </div>
    </div>
</form>
<jsp:include page="component/footer.jsp" flush="true"/>
<script>
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

    function editForm() {
        var z, i;
        var button = document.getElementById('refuse-button');
        z = document.getElementsByTagName("textarea");
        document.getElementById('edit-button').style.visibility = 'hidden';
        if (button != null) document.getElementById('refuse-button').style.visibility = 'hidden';
        document.getElementById('changeMarker').value = 'changed';
        for (i = 0; i < z.length; i++) {
            z[i].readOnly = false;
            z[i].style.borderColor = "green"
        }
    }

    function refuseProgram() {
        document.getElementById('edit-button').style.visibility = 'hidden';
        document.getElementById('refuse-button').style.visibility = 'hidden';
        document.getElementById('comment').style.visibility = 'visible'
        document.getElementById('comment-label').style.visibility = 'visible'
        document.getElementById('comment').required = 'true';
        document.getElementById('comment').style.borderColor = 'green';
        document.getElementById('changeMarker').value = 'refused';
    }
</script>
</body>
</html>
