<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale"/>
<fmt:message var="title" key="info.title"/>
<html>
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link href="${pageContext.request.contextPath}/style/style.css" type="text/css" rel="stylesheet">
</head>
<body class="form1">
<jsp:include page="../component/header.jsp" flush="true"/>
<div class="center-text" style="margin-top: 160px; color: green;">
    <c:if test="${not empty sessionScope.info}">
        <p><fmt:message key="${sessionScope.info}"/></p>
        <c:if test="${not empty sessionScope.additionalInfo}">
            ${sessionScope.additionalInfo}
        </c:if>
    </c:if>
</div>
<jsp:include page="../component/footer.jsp" flush="true"/>
</div>
</body>
</html>