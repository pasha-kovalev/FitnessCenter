<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale"/>
<fmt:message var="title" key="transformation.title"/>
<fmt:message var="Meetingtitle" key="transformation.meeting.title"/>
<fmt:message var="MeetingDesc" key="transformation.meeting.desc"/>
<fmt:message var="SpecTitle" key="transformation.spec.title"/>
<fmt:message var="SpecText1" key="transformation.spec.text1"/>
<fmt:message var="SpecText2" key="transformation.spec.text2"/>
<fmt:message var="SpecText3" key="transformation.spec.text3"/>
<fmt:message var="PlanTitle" key="transformation.plan.title"/>
<fmt:message var="PlanDesc" key="transformation.plan.desc"/>
<fmt:message var="PlanLi1" key="transformation.plan.li1"/>
<fmt:message var="PlanLi2" key="transformation.plan.li2"/>
<fmt:message var="PlanLi3" key="transformation.plan.li3"/>
<fmt:message var="PlanLi4" key="transformation.plan.li4"/>
<fmt:message var="PlanLi5" key="transformation.plan.li5"/>
<fmt:message var="PlanLi6" key="transformation.plan.li6"/>
<fmt:message var="PlanLi7" key="transformation.plan.li7"/>
<fmt:message var="PlanLi8" key="transformation.plan.li8"/>
<fmt:message var="PlanLi8Span" key="transformation.plan.li8.span"/>
<fmt:message var="sign" key="programs.sign"/>
<fmt:message var="buy" key="programs.buy"/>
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
<body style="background-color: #F5F5F5; color: black">>
<c:set var="item" scope="page" value="${requestScope.item}"/>
<c:if test="${requestScope.item.isArchive eq 'true'}">
    <jsp:forward page="error/error.jsp"/>
</c:if>
<jsp:include page="component/header.jsp" flush="true"/>
<div class="w3-content" style="max-width:1100px;margin-top:40px;margin-bottom:80px">
    <p class="w3-center"
       style="font-size: 48px;margin-bottom: 0;margin-top: 10px;padding-bottom: 0;font-weight: bold;font-family: 'PT serif';">${Meetingtitle}</p>
    <p class="w3-center" style="font-size: 22px;padding: 0;margin-top: 0;">${MeetingDesc}</p>

    <div class="w3-display-container w3-center">
        <img src="${pageContext.request.contextPath}/images/balance.jpg" style="width:100%">
    </div>


    <div class="w3-row w3-container" style="font-size: 20px">
        <div class="w3-center w3-padding-64">
            <span class="w3-bottombar w3-border-dark-grey w3-padding-16" style="font-size: 32px">${SpecTitle}</span>
        </div>
        <div class="w3-col l3 m6 w3-light-grey w3-container w3-padding-16" style="margin-left: 130px;">
            <p>${SpecText1}</p>
        </div>
        <div class="w3-col l3 m6 w3-grey w3-container w3-padding-16">
            <p>${SpecText2}</p>
        </div>
        <div class="w3-col l3 m6 w3-dark-grey w3-container w3-padding-16">
            ${SpecText3}
        </div>
    </div>


    <div class="w3-row-padding" id="plans">
        <div class="w3-center w3-padding-16">
            <h3>${PlanTitle}</h3>
        </div>
        <div class="w3-third w3-margin-bottom" style="margin-left: 33.3%">
            <ul class="w3-ul w3-border w3-center w3-hover-shadow">
                <li class="w3-black w3-xlarge w3-padding-32">
                    ${fn:toUpperCase(fn:substring(item.name, 0, 1))}${fn:substring(item.name, 1, item.name.length())}
                </li>
                <c:forEach var="li" items="${fn:split(item.description, '\\\\')}">
                    <li class="w3-padding-16">${li}</li>
                </c:forEach>
                <li class="w3-padding-16">
                    <c:choose>
                        <c:when test="${empty requestScope.productListDiscount}">
                            <h2>${item.price}BYN</h2>
                        </c:when>
                        <c:otherwise>
                            <h2><span id="price" style="text-decoration: line-through;">${item.price}</span>
                                <span id="total">${requestScope.productListDiscount[0].price}</span>BYN</h2>
                        </c:otherwise>
                    </c:choose>
                    <span class="w3-opacity">${permonth}</span>
                </li>
                <li class="w3-light-grey w3-padding-24">
                    <c:choose>
                        <c:when test="${empty sessionScope.user}">
                            <a href="${pageContext.request.contextPath}/controller?command=show_signup"
                               class="w3-button w3-white w3-padding-large">${sign}</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/controller?command=show_order&id=${item.id}"
                               class="w3-button w3-white w3-padding-large">${buy}</a>
                        </c:otherwise>
                    </c:choose>
                </li>
            </ul>
        </div>
    </div>
</div>
<div>
    <jsp:include page="component/footer.jsp" flush="true"/>
</div>
</body>
</html>
