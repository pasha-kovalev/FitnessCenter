<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<html>
<head>
    <title>Confirmation</title>
    <link href="../../style/style.css" type="text/css" rel="stylesheet">
</head>
<body class="form1">
<jsp:include page="component/header.jsp" flush="true"/>
<div class="center-text">
    <c:choose>
        <c:when test="${requestScope.errorConfirmEmail != null}">
            <p>${requestScope.errorConfirmEmail}</p>
        </c:when>
        <c:otherwise>
            <p>Email confirmed</p>
            <p style="color: #3f51b5">
                <a href="${pageContext.request.contextPath}/controller?command=show_login" >Go to login page</a>
            </p>
        </c:otherwise>
    </c:choose>
</div>
<jsp:include page="component/footer.jsp" flush="true"/>
</div>
</body>
</html>