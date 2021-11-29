<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.epam.jwd.fitness_center.model.entity.UserRole" %>
<header>
<div class="w3-top">
    <div class="w3-bar w3-black w3-card">
        <a href="${pageContext.request.contextPath}/controller" class="w3-bar-item w3-button w3-padding-large">HOME</a>
        <c:if test="${not empty sessionScope.user && sessionScope.user.role eq UserRole.ADMIN}">

        </c:if>
        <c:choose>
            <c:when test="${not empty sessionScope.user}">
                <a href="${pageContext.request.contextPath}/controller?command=log_out"
                   class="w3-padding-large w3-hover-red w3-hide-small w3-right">Log out</a>
            </c:when>
            <c:otherwise>
                <a href="${pageContext.request.contextPath}/controller?command=show_login"
                   class="w3-padding-large w3-hover-red w3-hide-small w3-right">Log in</a>
                <a href="${pageContext.request.contextPath}/controller?command=show_signup" class="w3-padding-large w3-hover-red w3-hide-small w3-right">Sign Up</a>
            </c:otherwise>
        </c:choose>
    </div>
</div>
</header>