<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale"/>
<fmt:message var="author" key="footer.author"/>

<footer class="w3-container w3-padding-8 w3-center w3-light-grey w3-xlarge"
        style="background-color: #2c2f31;z-index: 4;">
    <p class="w3-left w3-medium" style="font-max-size: 8px">
        <a href="${pageContext.request.contextPath}/controller?command=switch_locale&locale=en">EN</a>
        <a href="${pageContext.request.contextPath}/controller?command=switch_locale&locale=ru">Русский</a>
    </p>
    <p class="w3-medium">&#169; 2021-22 ${author}</p>
</footer>

