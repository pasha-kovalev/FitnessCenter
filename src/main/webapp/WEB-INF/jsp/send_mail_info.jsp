<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Finess Center</title>
    <link href="../../style/style.css" type="text/css" rel="stylesheet">
</head>
<body class="form1">
<jsp:include page="component/header.jsp" flush="true"/>
<div class="center-text">
    <c:choose>
        <c:when test="${sessionScope.errorResendMsg != null}">
            <p>${sessionScope.errorResendMsg}</p>
        </c:when>
        <c:when test="${sessionScope.successResendMsg != null}">
            <p>${sessionScope.successResendMsg}</p>
        </c:when>
        <c:otherwise>
            <p>Email has been sent. Please check the mail box.</p>
            <p>Not receive? <a href="${pageContext.request.contextPath}/controller?command=resend_email" >Resend</a></p>
        </c:otherwise>
    </c:choose>


</div>
    <jsp:include page="component/footer.jsp" flush="true"/>
</div>

</body>
</html>