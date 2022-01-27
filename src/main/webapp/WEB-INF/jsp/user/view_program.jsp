<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="com.epam.jwd.fitness_center.model.entity.UserRole" %>
<%@ page import="com.epam.jwd.fitness_center.model.entity.ProgramStatus" %>

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
    footer {
        top: 160%;
        bottom: 0;
    }
    #send-button, #refuse-button, #edit-button {
        background-color: #ffffffff;
        padding: 8px;
        border: 2px black;
        border: 3px solid #000000;
        cursor: pointer;
    }
</style>
<body class="form2">
<jsp:include page="../component/header.jsp" flush="true"/>
<form id="program" name="program-form" style="top: 80%;"
      action="${pageContext.request.contextPath}/controller?command=update_program&orderId=${requestScope.order.id}"
      method="post">
    <h1>Программа</h1>

    <c:choose>
        <c:when test="${requestScope.program.programStatus eq ProgramStatus.REFUSED.name()}">
            <p style="color: red">Отклонено</p>
            <p> Комментарий: ${requestScope.order.comment}</p>
        </c:when>
        <c:otherwise>
            <div class="tooltip">Информация о клиенте
                <span class="tooltiptext">${requestScope.order.comment}</span>
            </div>
        </c:otherwise>
    </c:choose>

    <div>
        <input type="hidden" id="changeMarker" name="changeMarker" value="false">
        <label for="intensity">Интенсивность</label>
        <textarea id="intensity" name="intensity" rows="3" cols="33" maxlength="1000" required readonly
                  oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">${requestScope.program.intensity}</textarea>

        <label for="schedule">Расписание</label>
        <textarea id="schedule" name="schedule" rows="5" cols="33" maxlength="1000" required readonly
                  oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">${requestScope.program.schedule}</textarea>

        <label for="exercises">Упражнения</label>
        <textarea id="exercises" name="exercises" rows="10" cols="33" maxlength="3000" required readonly
                  oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">${requestScope.program.exercises}</textarea>

        <label for="diet">Диета</label>
        <textarea id="diet" name="diet" rows="10" cols="33" maxlength="3000" required readonly
                  oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">${requestScope.program.diet}</textarea>

        <label for="equipment">Оборудование</label>
        <textarea id="equipment" name="equipment" rows="10" cols="33" maxlength="1000" required readonly
                  oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">${requestScope.program.equipment}</textarea>
        <label id="comment-label" for="comment" style="visibility: hidden;">Комментарий для тренера</label>
        <textarea id="comment" name="comment" rows="1" cols="33" maxlength="1000" style="visibility: hidden;"
                  oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')"></textarea>
    </div>
    <div style="overflow:auto; padding-top: 36px;">
        <div style="float:right;">
            <c:if test="${sessionScope.user.role ne UserRole.TRAINER}">
                <input id="refuse-button" type="button" value="Отказ" onclick="refuseProgram()" />
            </c:if>
            <input id="edit-button" type="button" value="Изменить" onclick="editForm()" />
            <input id="send-button" type="submit" value="Отправить" />
        </div>
    </div>
</form>
<jsp:include page="../component/footer.jsp" flush="true"/>
<script>
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

    function editForm() {
        var z, i;
        var button = document.getElementById('refuse-button');
        z = document.getElementsByTagName("textarea");
        document.getElementById('edit-button').style.visibility = 'hidden';
        if(button != null) document.getElementById('refuse-button').style.visibility = 'hidden';
        document.getElementById('changeMarker').value = 'changed';
        for (i = 0; i < z.length; i++) {
            z[i].readOnly = false;
            z[i].style.borderColor = "green"
        }
    }

    function refuseProgram() {
        var z, i;
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
