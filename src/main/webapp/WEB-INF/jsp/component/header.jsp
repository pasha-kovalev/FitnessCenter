<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.epam.jwd.fitness_center.model.entity.UserRole" %>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="locale" />
<fmt:message var="home" key="header.home"/>
<fmt:message var="signup" key="header.signup"/>
<fmt:message var="login" key="header.login"/>
<fmt:message var="about" key="header.about"/>
<fmt:message var="logout" key="header.logout"/>
<fmt:message var="programs" key="header.programs"/>
<fmt:message var="programs1" key="header.programs.first"/>
<fmt:message var="programs2" key="header.programs.second"/>

<header>
<div class="w3-top">
    <div class="w3-bar w3-black w3-card">
        <a href="${pageContext.request.contextPath}/controller?command=main_page" class="w3-bar-item w3-button w3-padding-large">${home}</a>
        <div class="w3-dropdown-hover w3-hide-small">
            <button class="w3-padding-large w3-button" title="More">${programs} <i class="fa fa-caret-down"></i></button>
            <div class="w3-dropdown-content w3-bar-block w3-card-4">
                <a href="${pageContext.request.contextPath}/controller?command=show_programs" class="w3-bar-item w3-button">
                    ${programs1}
                </a>
                <a href="${pageContext.request.contextPath}/controller?command=show_transform_program" class="w3-bar-item w3-button">
                    ${programs2}
                </a>
            </div>
        </div>
        <a href="${pageContext.request.contextPath}/controller?command=show_about"
           class="w3-bar-item w3-button w3-padding-large">${about}</a>

        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <div class="w3-dropdown-hover w3-hide-small w3-right">
                    <button class="w3-padding-small w3-button" title="userButton">
                        <i class="fa fa-user-circle fa-2x" style="padding-top: 4px; padding-right: 16px; padding-left: 16px"></i>
                    </button>
                    <div class="w3-dropdown-content w3-bar-block w3-card-4">
                        <c:choose>
                            <c:when test="${sessionScope.user.role ne UserRole.ADMIN
                                            || sessionScope.user.role ne UserRole.TRAINER}">
                                <a href="${pageContext.request.contextPath}/controller?command=user_cabinet"
                                   class="w3-bar-item w3-button">Личный кабинет</a>
                            </c:when>
                            <c:when test="${sessionScope.user.role eq UserRole.ADMIN}">
                                <a href="${pageContext.request.contextPath}/controller?command=admin_cabinet"
                                   class="w3-bar-item w3-button">Личный кабинет</a>
                            </c:when>
                            <c:when test="${sessionScope.user.role eq UserRole.TRAINER}">
                                <a href="${pageContext.request.contextPath}/controller?command=trainer_cabinet"
                                   class="w3-bar-item w3-button">Личный кабинет</a>
                            </c:when>
                        </c:choose>
                        <a href="${pageContext.request.contextPath}/controller?command=log_out"
                           class="w3-bar-item w3-button">${logout}</a>
                    </div>
                </div>


            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/controller?command=show_login"
                   class="w3-padding-large w3-hover-white w3-hide-small w3-right">${login}</a>
                <a href="${pageContext.request.contextPath}/controller?command=show_signup"
                   class="w3-padding-large w3-hover-white w3-hide-small w3-right">${signup}</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</header>