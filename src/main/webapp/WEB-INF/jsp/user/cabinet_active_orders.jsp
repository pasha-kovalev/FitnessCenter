<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%--
  Created by IntelliJ IDEA.
  User: TokyoPashka
  Date: 11.12.2021
  Time: 19:44
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link href="../../style/style.css" type="text/css" rel="stylesheet">
    <style>
        th
        {
            text-align: left;
        }
        table {
            border-collapse: separate;
            border-spacing: 50px 0;
        }
    </style>
</head>
<body>
<jsp:include page="user_header.jsp" flush="true"/>
<jsp:include page="cabinet.jsp" flush="true"/>
<div class="w3-main" style="margin-left:300px;margin-top:43px; height: 95%">
    <table>
        <tr><th>Дата заказа</th><th>Программа</th><th>Тренер</th><th>Цена</th><th>Статус</th></tr>
        <c:forEach var="order" items="${requestScope.orderList}">
            <tr>
                <td><c:out value="${order.creationDate}" /></td>
                <td><c:out value="${order.item.name}" /></td>
                <td><c:out value="${order.trainerName}" /></td>
                <td><c:out value="${order.price}" /> BYN</td>
                <td><c:out value="${order.orderStatus}" /></td>
            </tr>
        </c:forEach>
    </table>
</div>
<jsp:include page="../component/footer.jsp" flush="true"/>
</body>
</html>
