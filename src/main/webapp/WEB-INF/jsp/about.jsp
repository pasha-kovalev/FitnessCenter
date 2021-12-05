<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="locale" />
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
    <link href="../../style/style.css" type="text/css" rel="stylesheet">
</head>
<style>
    body {font-family: "Open sans";}
    h1, h2, h3, h4, h5, h6 {
        font-family: "PT serif";
    }
</style>
<body>
<jsp:include page="component/header.jsp" flush="true"/>
<div class="w3-content" style="max-width:1100px">
    <h1 class="w3-center" style="margin-top: 72px">${meeting}</h1>
    <div class="w3-row w3-padding-64">
        <div class="w3-col m6 w3-padding-large w3-hide-small">
            <img src="../../images/gigachad.png" class="w3-round w3-image" width="600" height="750">
        </div>

        <div class="w3-col m6 w3-padding-large">
            <h1 class="w3-left">${name1}</h1>
            <br><br><br>
            <p class="w3-large">
                ${description1}
            </p>
        </div>
    </div>

    <hr>

    <div class="w3-row w3-padding-64">
        <div class="w3-col l6 w3-padding-large">
            <h1 class="w3-left">${name2}</h1><br>
            <br><br><br>
            <p class="w3-large">
                ${description2}
            </p>
        </div>

        <div class="w3-col l6 w3-padding-large">
            <img src="../../images/gosling.jpg" class="w3-round w3-image" width="600" height="750">
        </div>
    </div>

    <hr>

    <div class="w3-row w3-padding-64">
        <div class="w3-col m6 w3-padding-large w3-hide-small">
            <img src="../../images/floppa.jpg" class="w3-round w3-image" width="600" height="750">
        </div>

        <div class="w3-col m6 w3-padding-large">
            <h1 class="w3-left">${name3}</h1>
            <br><br><br>
            <p class="w3-large">
                ${description3}
            </p>
        </div>
    </div>

    <hr>

    <div class="w3-row w3-padding-64" id="menu">
        <div class="w3-col l6 w3-padding-large">
            <h1 class="w3-left">${name4}</h1><br>
            <br><br>
            <p class="w3-large">${description4}</p>
        </div>

        <div class="w3-col l6 w3-padding-large">
            <img src="../../images/geralt.png" class="w3-round w3-image" width="600" height="750">
        </div>
    </div>

    <hr>

    <div class="w3-row w3-padding-64" id="about">
        <div class="w3-col m6 w3-padding-large w3-hide-small">
            <img src="../../images/karamba.jpg" class="w3-round w3-image" width="600" height="750">
        </div>

        <div class="w3-col m6 w3-padding-large">
            <h1 class="w3-left">${name5}</h1>
            <br><br><br>
            <p class="w3-large">
                ${description5}
            </p>
        </div>
    </div>
</div>
<div>
    <jsp:include page="component/footer.jsp" flush="true"/>
</div>
</body>
</html>

