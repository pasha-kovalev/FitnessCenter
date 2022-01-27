<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="locale" />
<fmt:message var="title" key="emailInfo.title"/>
<fmt:message var="msgSuccess" key="emailInfo.msg.success"/>

<html>
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link href="../../../style/style.css" type="text/css" rel="stylesheet">
</head>
<meta charset="UTF-8">
<body class="form1">
<jsp:include page="../component/header.jsp" flush="true"/>
<div class="center-text">
    <c:choose>
        <c:when test="${requestScope.errorConfirmEmail != null}">
            <p>${requestScope.errorConfirmEmail}</p>
        </c:when>
        <c:otherwise>
            <p>${msgSuccess}</p>
        </c:otherwise>
    </c:choose>
</div>
<jsp:include page="../component/footer.jsp" flush="true"/>
</div>
</body>
</html>