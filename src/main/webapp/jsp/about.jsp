<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale"/>
<fmt:message var="title" key="about.title"/>
<fmt:message var="meeting" key="about.meeting"/>
<fmt:message var="name1" key="about.team1.name"/>
<fmt:message var="description1" key="about.team1.description"/>
<fmt:message var="name2" key="about.team2.name"/>
<fmt:message var="description2" key="about.team2.description"/>
<fmt:message var="name3" key="about.team3.name"/>
<fmt:message var="description3" key="about.team3.description"/>
<fmt:message var="name4" key="about.team4.name"/>
<fmt:message var="description4" key="about.team4.description"/>
<fmt:message var="name5" key="about.team5.name"/>
<fmt:message var="description5" key="about.team5.description"/>
<html>
<head>
    <title>${title}</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="${pageContext.request.contextPath}/style/style.css" type="text/css" rel="stylesheet">
</head>
<style>
    body {
        font-family: "Open sans";
    }

    h1, h2, h3, h4, h5, h6 {
        font-family: "PT serif";
    }
</style>
<body>
<jsp:include page="component/header.jsp" flush="true"/>
<div class="w3-content" style="max-width:1100px">
    <h1 class="w3-center" style="margin-top: 72px">${meeting}</h1>
    <c:forEach var="trainer" items="${requestScope.trainerList}" varStatus="loopStatus">
        <c:choose>
            <c:when test="${loopStatus.index % 2 == 0}">
                <div class="w3-row w3-padding-64">
                    <div class="w3-col l6 w3-padding-large">
                        <h1 class="w3-left">${trainer.firstName} ${trainer.secondName}</h1><br>
                        <br><br><br>
                        <p class="w3-large">
                                ${trainer.description}
                        </p>
                    </div>

                    <div class="w3-col l6 w3-padding-large">
                        <img src="<c:out value='${trainer.photoPath}'/>" class="w3-round w3-image" width="600"
                             height="750">
                    </div>
                </div>
            </c:when>
            <c:otherwise>
                <div class="w3-row w3-padding-64">
                    <div class="w3-col m6 w3-padding-large w3-hide-small">
                        <img src="${trainer.photoPath}" class="w3-round w3-image" width="600" height="750">
                    </div>

                    <div class="w3-col m6 w3-padding-large">
                        <h1 class="w3-left">${trainer.firstName} ${trainer.secondName}</h1>
                        <br><br><br>
                        <p class="w3-large">
                                ${trainer.description}
                        </p>
                    </div>
                </div>
            </c:otherwise>
        </c:choose>
        <c:if test="${not loopStatus.last}">
            <hr>
        </c:if>
    </c:forEach>
</div>
<div>
    <jsp:include page="component/footer.jsp" flush="true"/>
</div>
</body>
</html>

