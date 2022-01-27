<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale"/>
<fmt:message var="title" key="sendEmailInfo.title"/>
<fmt:message var="sent" key="sendEmailInfo.msg.sent"/>
<fmt:message var="receive" key="sendEmailInfo.msg.receive"/>
<fmt:message var="resendEmail" key="sendEmailInfo.msg.resend"/>
<html>
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link href="../../../style/style.css" type="text/css" rel="stylesheet">
</head>
<body class="form1">
<jsp:include page="../component/header.jsp" flush="true"/>
<div class="center-text" style="margin-top: 20%;">
    <p>${sent}</p>
    <p>
        ${receive}
        <a href="${pageContext.request.contextPath}/controller?command=resend_email"> ${resendEmail} </a>
    </p>
</div>
<jsp:include page="../component/footer.jsp" flush="true"/>
</div>

</body>
</html>