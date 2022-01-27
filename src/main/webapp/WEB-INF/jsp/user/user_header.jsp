<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale"/>
<fmt:message var="home" key="header.home"/>
<fmt:message var="cabinet" key="user.cabinet.title"/>
<fmt:message var="logout" key="header.logout"/>

<header>
    <div class="w3-top">
        <div class="w3-bar w3-black w3-card">
            <a href="${pageContext.request.contextPath}/controller?command=main_page"
               class="w3-bar-item w3-button w3-padding-large">${home}</a>
            <div class="w3-dropdown-hover w3-hide-small w3-right">
                <button class="w3-padding-small w3-button" title="userButton">
                    <i class="fa fa-user-circle fa-2x"
                       style="padding-top: 4px; padding-right: 16px; padding-left: 16px"></i>
                </button>
                <div class="w3-dropdown-content w3-bar-block w3-card-4">
                    <a href="${pageContext.request.contextPath}/controller?command=user_cabinet"
                       class="w3-bar-item w3-button">${cabinet}</a>
                    <a href="${pageContext.request.contextPath}/controller?command=log_out"
                       class="w3-bar-item w3-button">${logout}</a>
                </div>
            </div>
        </div>
    </div>
</header>