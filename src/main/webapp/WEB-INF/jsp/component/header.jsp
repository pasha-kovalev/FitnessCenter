<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.epam.jwd.fitness_center.model.entity.UserRole" %>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="locale" />
<fmt:message var="home" key="header.home"/>
<fmt:message var="signup" key="header.signup"/>
<fmt:message var="login" key="header.login"/>
<fmt:message var="logout" key="header.logout"/>

<header>
<div class="w3-top">
    <div class="w3-bar w3-black w3-card">
        <a href="${pageContext.request.contextPath}/controller?command=main_page" class="w3-bar-item w3-button w3-padding-large">${home}</a>
        <c:if test="${not empty sessionScope.user && sessionScope.user.role eq UserRole.ADMIN}">

        </c:if>
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/controller?command=log_out"
                   class="w3-padding-large w3-hover-red w3-hide-small w3-right">${logout}</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/controller?command=show_login" class="w3-hide-small w3-right">
                    <i class="fa fa-user-circle fa-2x" style="padding-top: 8px; padding-right: 12px; padding-left: 12px"></i>
                </a>
                <a href="${pageContext.request.contextPath}/controller?command=show_login"
                   class="w3-padding-large w3-hover-red w3-hide-small w3-right">${login}</a>
                <a href="${pageContext.request.contextPath}/controller?command=show_signup"
                   class="w3-padding-large w3-hover-red w3-hide-small w3-right">${signup}</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</header>