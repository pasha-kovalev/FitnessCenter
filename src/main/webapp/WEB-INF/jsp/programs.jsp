<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale"/>
<fmt:message var="title" key="programs.title"/>
<fmt:message var="meetingTitle" key="programs.meeting.title"/>
<fmt:message var="specTitle" key="programs.spec.title"/>
<fmt:message var="specText1" key="programs.spec.text1"/>
<fmt:message var="specText2" key="programs.spec.text2"/>
<fmt:message var="specText3" key="programs.spec.text3"/>
<fmt:message var="specText4" key="programs.spec.text4"/>
<fmt:message var="planTitle" key="programs.plan.title"/>
<fmt:message var="planDesc" key="programs.plan.desc"/>
<fmt:message var="permonth" key="programs.perMonth"/>
<fmt:message var="sign" key="programs.sign"/>
<fmt:message var="buy" key="programs.buy"/>
<fmt:message var="basicTitle" key="programs.basic.title"/>
<fmt:message var="basicLi1" key="programs.basic.li1"/>
<fmt:message var="basicLi2" key="programs.basic.li2"/>
<fmt:message var="basicLi3" key="programs.basic.li3"/>
<fmt:message var="basicLi4" key="programs.basic.li4"/>
<fmt:message var="basicPrice" key="programs.basic.price"/>
<fmt:message var="proTitle" key="programs.pro.title"/>
<fmt:message var="proLi1" key="programs.pro.li1"/>
<fmt:message var="proLi2" key="programs.pro.li2"/>
<fmt:message var="proLi3" key="programs.pro.li3"/>
<fmt:message var="proLi4" key="programs.pro.li4"/>
<fmt:message var="proLi5" key="programs.pro.li5"/>
<fmt:message var="proPrice" key="programs.pro.price"/>
<fmt:message var="premiumTitle" key="programs.premium.title"/>
<fmt:message var="premiumLi1" key="programs.premium.li1"/>
<fmt:message var="premiumLi2" key="programs.premium.li2"/>
<fmt:message var="premiumLi3" key="programs.premium.li3"/>
<fmt:message var="premiumLi4" key="programs.premium.li4"/>
<fmt:message var="premiumLi5" key="programs.premium.li5"/>
<fmt:message var="premiumLi6" key="programs.premium.li6"/>
<fmt:message var="premiumPrice" key="programs.premium.price"/>
<fmt:message var="resultsTitle" key="programs.results.title"/>

<html>
<head>
    <title>${title}</title>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link href="../../style/style.css" type="text/css" rel="stylesheet">
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
<jsp:include page="component/header.jsp" flush="true"/>

<div class="w3-content" style="max-width:1100px;margin-top:40px;margin-bottom:80px">
    <p class="w3-center"
       style="font-size: 48px;  margin-bottom: 0; margin-top: 10px; font-weight: bold; font-family: 'PT serif';">${meetingTitle}</p>
    <div class="w3-display-container w3-center">
        <img src="../../images/gym4.jpg" style="width:100%">
    </div>

    <div class="w3-row w3-container" style="font-size: 20px">
        <div class="w3-center w3-padding-64">
            <span class="w3-bottombar w3-border-dark-grey w3-padding-16" style="font-size: 32px">${specTitle}</span>
        </div>
        <div class="w3-col l3 m6 w3-light-grey w3-container w3-padding-16">
            <p>${specText1}</p>
        </div>

        <div class="w3-col l3 m6 w3-grey w3-container w3-padding-16">
            <p>${specText2}</p>

        </div>

        <div class="w3-col l3 m6 w3-dark-grey w3-container w3-padding-16">
            <p>${specText3} </p>
        </div>

        <div class="w3-col l3 m6 w3-black w3-container w3-padding-16">
            <p>${specText4}</p>
        </div>
    </div>

    <div class="w3-row w3-container" style="font-size: 20px">
        <div class="w3-center w3-padding-64">
            <span class="w3-bottombar w3-border-dark-grey w3-padding-16" style="font-size: 32px">${resultsTitle}</span>
        </div>
        <div class="w3-display-container w3-center">
            <img src="../../images/beforeafter2.png" style="width:100%">
        </div>
        <div class="w3-display-container w3-center" style="padding-top: 20px">
            <img src="../../images/beforeafter.png" style="width:100%">
        </div>
        <div class="w3-display-container w3-center" style="padding-top: 20px">
            <img src="../../images/beforeafter3.png" style="width:100%">
        </div>
    </div>

    <div class="w3-row-padding" id="plans">
        <div class="w3-center w3-padding-64">
            <h3>${planTitle}</h3>
            <p>${planDesc}</p>
        </div>

        <div class="w3-third w3-margin-bottom">
            <ul class="w3-ul w3-border w3-center w3-hover-shadow">
                <li class="w3-black w3-xlarge w3-padding-32">${basicTitle}</li>
                <li class="w3-padding-16"> ${basicLi1}</li>
                <li class="w3-padding-16">${basicLi2}</li>
                <li class="w3-padding-16">
                    ${basicLi3}</li>
                <li class="w3-padding-16">
                    ${basicLi4}</li>
                <li class="w3-padding-16">
                    <h2 class="w3-wide">${basicPrice}</h2>
                    <span class="w3-opacity">${permonth}</span>
                </li>

                <li class="w3-light-grey w3-padding-24">
                    <c:choose>
                        <c:when test="${sessionScope.user == null}">
                            <a href="${pageContext.request.contextPath}/controller?command=show_signup"
                               class="w3-button w3-white w3-padding-large">${sign}</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/controller?command=show_order"
                               class="w3-button w3-white w3-padding-large">${buy}</a>
                        </c:otherwise>
                    </c:choose>
                </li>
            </ul>
        </div>

        <div class="w3-third w3-margin-bottom">
            <ul class="w3-ul w3-border w3-center w3-hover-shadow">
                <li class="w3-dark-grey w3-xlarge w3-padding-32">${proTitle}</li>
                <li class="w3-padding-16">
                    ${proLi1}</li>
                <li class="w3-padding-16"> ${proLi2}</li>
                <li class="w3-padding-16">${proLi3}</li>
                <li class="w3-padding-16">
                    ${proLi4}</li>
                <li class="w3-padding-16">
                    ${proLi5}</li>

                <li class="w3-padding-16">
                    <h2 class="w3-wide">${proPrice}</h2>
                    <span class="w3-opacity">${permonth}</span>
                </li>
                <li class="w3-light-grey w3-padding-24">
                    <c:choose>
                        <c:when test="${sessionScope.user == null}">
                            <a href="${pageContext.request.contextPath}/controller?command=show_signup"
                               class="w3-button w3-white w3-padding-large">${sign}</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/controller?command=show_order"
                               class="w3-button w3-white w3-padding-large">${buy}</a>
                        </c:otherwise>
                    </c:choose>
                </li>
            </ul>
        </div>

        <div class="w3-third w3-margin-bottom">
            <ul class="w3-ul w3-border w3-center w3-hover-shadow">
                <li class="w3-black w3-xlarge w3-padding-32">${premiumTitle}</li>
                <li class="w3-padding-16">
                    ${premiumLi1}</li>
                <li class="w3-padding-16">
                    ${premiumLi2}</li>
                <li class="w3-padding-16">${premiumLi3}</li>
                <li class="w3-padding-16">${premiumLi4}</li>
                <li class="w3-padding-16">${premiumLi5}</li>
                <li class="w3-padding-16">${premiumLi6}</li>

                <li class="w3-padding-16">
                    <h2 class="w3-wide">${premiumPrice}</h2>
                    <span class="w3-opacity">${permonth}</span>
                </li>
                <li class="w3-light-grey w3-padding-24">
                    <c:choose>
                        <c:when test="${sessionScope.user == null}">
                            <a href="${pageContext.request.contextPath}/controller?command=show_signup"
                               class="w3-button w3-white w3-padding-large">${sign}</a>
                        </c:when>
                        <c:otherwise>
                            <a href="${pageContext.request.contextPath}/controller?command=show_order"
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
