<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.epam.jwd.fitness_center.model.entity.OrderStatus" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale"/>
<fmt:message var="title" key="user.cabinet.title"/>
<fmt:message var="activeOrders" key="trainer.cabinet.activeOrders"/>
<fmt:message var="notProcOrders" key="trainer.cabinet.notProcOrders"/>
<fmt:message var="history" key="trainer.cabinet.history"/>
<fmt:message var="settings" key="trainer.cabinet.settings"/>
<fmt:message var="fistName" key="trainer.cabinet.fistName"/>
<fmt:message var="lastName" key="trainer.cabinet.lastName"/>
<fmt:message var="desc" key="trainer.cabinet.desc"/>
<fmt:message var="photo" key="trainer.cabinet.photo"/>
<fmt:message var="program" key="trainer.cabinet.program"/>
<fmt:message var="personalTrainer" key="trainer.cabinet.personalTrainer"/>
<fmt:message var="take" key="trainer.cabinet.take"/>
<fmt:message var="process" key="trainer.cabinet.process"/>
<fmt:message var="view" key="trainer.cabinet.view"/>
<fmt:message var="orderDate" key="trainer.cabinet.orderDate"/>
<fmt:message var="status" key="trainer.cabinet.status"/>
<fmt:message var="chooseImg" key="trainer.cabinet.chooseImg"/>
<fmt:message var="submit" key="button.submit"/>
<fmt:message var="delete" key="admin.cabinet.button.delete"/>
<fmt:message var="password" key="signup.password"/>
<fmt:message var="passwordTitle" key="signup.password.input.title"/>
<fmt:message var="errorMismatch" key="signup.error.passwordMismatch"/>

<html>
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link href="${pageContext.request.contextPath}/style/style.css" type="text/css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-latest.min.js"></script>
    <style>
        footer {
            position: relative;
        }

        th {
            text-align: inherit;
        }
        .custom-file-input::-webkit-file-upload-button {
            visibility: hidden;
        }
        .custom-file-input {
            width: 150px;
        }
        .custom-file-input::before {
            -webkit-appearance: button;
            content: '${chooseImg}';
            display: inline-block;
            background-color: #ffffffff;
            color: black;
            padding: 4px;
            border: 2px black;
            cursor: pointer;
            outline: none;
            white-space: nowrap;
        }

        #submit-image {
            display: inline-block;
            background-color: #ffffffff;
            color: black;
            padding: 4px;
            border: 2px black;
            cursor: pointer;
            outline: none;
            white-space: nowrap;
        }

    </style>
</head>
<body onload="showOrders('active')">
<jsp:include page="../component/header.jsp" flush="true"/>
<nav class="w3-sidebar w3-collapse w3-white w3-animate-left" style="z-index:3;width:300px;margin-top: 46px"
     id="mySidebar"><br>
    <div class="w3-container w3-row">
        <div class="w3-col s2">
            <i class="fa fa-user-circle fa-2x w3-margin-right" style="width:46px"></i>
        </div>
        <div class="w3-col s8 w3-bar">
            <span>${welcome} <strong>${sessionScope.user.firstName}</strong></span><br>
        </div>
    </div>
    <hr>
    <div class="w3-bar-block">
        <button class="w3-bar-item w3-button w3-padding" onclick="showOrders('active')">
            <i class="fa fa-bullseye fa-fw"></i>${activeOrders}
        </button>
        <button class="w3-bar-item w3-button w3-padding" onclick="showOrders('untaken')">
            <i class="fa fa-bullseye fa-fw"></i>${notProcOrders}
        </button>
        <button class="w3-bar-item w3-button w3-padding" onclick="showOrders('history')">
            <i class="fa fa-history fa-fw"></i>${history}
        </button>
        <button class="w3-bar-item w3-button w3-padding" onclick="showSetting()">
            <i class="fa fa-gear fa-fw"></i>${settings}
        </button>
    </div>
</nav>
<div class="w3-main" style="margin-left:300px;padding-top:90px; height: auto; padding-bottom: 50px; min-height: 95%">
    <div id="mainData"></div>
    <div id="review" style="visibility: hidden; position: absolute; background-color: white;
                            min-width: 140px;min-height: 30px;color: black; padding: 10px">
    </div>
</div>
<jsp:include page="../component/footer.jsp" flush="true"/>
<script>
    var mySidebar = document.getElementById("mySidebar");
    var overlayBg = document.getElementById("myOverlay");
    var mainDataLoaded = false;
    var isFiltered = false;
    var reviewLoaded = false;
    var isFormEditing = false;

    function resetDefault() {
        mainDataLoaded = false;
        isFiltered = false;
        reviewLoaded = false;
        isFormEditing = false;
        var review = document.getElementById("review");
        review.style.visibility = 'hidden';
        review.innerHTML = "";
    }

    function w3_open() {
        if (mySidebar.style.display === 'block') {
            mySidebar.style.display = 'none';
            overlayBg.style.display = "none";
        } else {
            mySidebar.style.display = 'block';
            overlayBg.style.display = "block";
        }
    }

    function w3_close() {
        mySidebar.style.display = "none";
        overlayBg.style.display = "none";
    }

    function showSetting() {
        resetDefault();
        document.getElementById("mainData").innerHTML = "";
        $("#mainData").append($("<h1>").text("${settings}"));
        var $div = $("<div style='padding-left: 100px'>").appendTo($("#mainData"));
        $div.append($("<h3>").text("${fistName}"))
            .append($(`<textarea id="firstname" name="firstname" rows="1" cols="33" maxlength="45" required readonly
            oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">`)
                .text("${sessionScope.user.firstName}"))
            .append($(`<button class="edit-btn w3-bar-item w3-button" onclick="editSettings(this)">`)
                .append($(`<i class="fa fa-edit fa-fw">`)))
            .append($("<h3>").text("${lastName}"))
            .append($(`<textarea id="lastname" name="lastname" rows="1" cols="33" maxlength="45" required readonly
                     oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">`)
                .text("${sessionScope.user.secondName}"))
            .append($(`<button class="edit-btn w3-bar-item w3-button" onclick="editSettings(this)">`)
                .append($(`<i class="fa fa-edit fa-fw">`)))
            .append($("<h3>").text("${desc}"))
            .append($(`<textarea id="description" name="description" rows="6" cols="66" maxlength="1000" required readonly
                     oninvalid="setCustomValidity('${notValidTitle}')" oninput="setCustomValidity('')">`)
                .text("${sessionScope.user.description}"))
            .append($(`<button class="edit-btn w3-bar-item w3-button" onclick="editSettings(this)">`)
                .append($(`<i class="fa fa-edit fa-fw">`)))
            .append($("<h3>").text("${photo}"))
            .append($(`<img src="${sessionScope.user.photoPath}" class="w3-round w3-image" width="600" height="750">`))
            .append(`<form action="${pageContext.request.contextPath}/controller?command=upload_image"
                               enctype="multipart/form-data" method="post" style="padding-top: 10px">
                            <input type="file" name="file" class="custom-file-input" accept="image/*" />
                            <input type="submit" id="submit-image" value="${submit}" />
                         </form>`);
    }

    function showOrders(status) {
        resetDefault();
        jQuery.ajax({
            type: 'GET',
            url: '${pageContext.request.contextPath}/controller?command=show_trainer_orders&orderStatuses=' + status,
            success: function (responseJson) {
                document.getElementById("mainData").innerHTML = "";
                var $table = $(`<table class="custom-table" id="mainTable">`).appendTo($("#mainData"));
                $("<thead>").appendTo($table)
                    .append($(`<tr><th onclick="sortTable(0)" style="cursor: pointer">${orderDate}</th>
                                   <th>${program}</th>
                                   <th onclick="filterByTrainer('${sessionScope.user.secondName}')"
                                       style="cursor: pointer">${personalTrainer}</th>
                                   <th>${status}</th></tr>`));
                $.each(responseJson, function (index, order) {
                    var lastTd;
                    var lastTd2;
                    switch (order.orderStatus) {
                        case "${OrderStatus.UNTAKEN.name()}":
                            lastTd = "<a " +
                                "href=\"${pageContext.request.contextPath}/controller" +
                                "?command=show_make_program&orderId=" + order.id +
                                '" class="btn btn-danger">${take}</a>';
                            break;
                        case "${OrderStatus.TAKEN.name()}":
                            lastTd = "<a " +
                                "href=\"${pageContext.request.contextPath}/controller" +
                                "?command=show_make_program&orderId=" + order.id +
                                '" class="btn btn-danger">${process}</a>';
                            break;
                        case "${OrderStatus.PENDING_TRAINER.name()}":
                            lastTd = "<a " +
                                "href=\"${pageContext.request.contextPath}/controller?command=show_program&orderId="
                                + order.id + "\" class=\"btn btn-warning\">${view}</a>";
                            break;
                        case "${OrderStatus.COMPLETED.name()}":
                            if (order.review != null) {
                                lastTd = "<button class=\"btn btn-success\" " +
                                    "onclick='showReview(this)'>${view}</button>";
                            } else {
                                lastTd = "";
                            }
                            lastTd2 = `<button onclick="deleteOrder(`+ order.id +`, this)" ` +
                                `class="btn btn-danger">${delete}</button>`;
                            break;
                        default:
                            lastTd = "";
                            break;
                    }
                    $("<tr>").appendTo($table)
                        .append($("<td>").text(order.creationDate.date['day'] + '-' +
                            order.creationDate.date['month'] + '-' +
                            order.creationDate.date['year']))
                        .append($("<td>").text(order.item['name']))
                        .append($(`<td class="trainer-name">`).text(order.trainerName))
                        .append($("<td>").text(order.orderStatus))
                        .append($(`<td class="reviewTd" hidden>`).text(order.review == null ? "" : order.review))
                        .append($("<td>").append(lastTd2))
                        .append($("<td>").append(lastTd));
                });
            }
        })
    }

    function deleteOrder(id, elem) {
        jQuery.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/controller',
            data: jQuery.param({command: 'delete_order', id: id}),
            success: function (responseJson) {
                responseJson = responseJson.toString();
                console.log(responseJson);
                if (responseJson.includes('false') || responseJson.includes('error')) {
                    $(elem).closest('tr')[0].style.border = '1px solid red';
                } else {
                    showOrders('history');
                }
            }
        })
    }

    function sortTable(n) {
        var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
        table = document.getElementById("mainTable");
        switching = true;
        dir = "asc";
        while (switching) {
            switching = false;
            rows = table.rows;
            for (i = 1; i < (rows.length - 1); i++) {
                shouldSwitch = false;
                x = rows[i].getElementsByTagName("TD")[n];
                y = rows[i + 1].getElementsByTagName("TD")[n];
                if (dir == "asc") {
                    if (convertDate(x.innerHTML) - convertDate(y.innerHTML) < 0) {
                        shouldSwitch = true;
                        break;
                    }
                } else if (dir == "desc") {
                    if (convertDate(x.innerHTML) - convertDate(y.innerHTML) > 0) {
                        shouldSwitch = true;
                        break;
                    }
                }
            }
            if (shouldSwitch) {
                rows[i].parentNode.insertBefore(rows[i + 1], rows[i]);
                switching = true;
                switchcount++;
            } else {
                if (switchcount == 0 && dir == "asc") {
                    dir = "desc";
                    switching = true;
                }
            }
        }
    }

    function convertDate(d) {
        var p = d.split("-");
        return +(p[2] + p[1] + p[0]);
    }

    function filterByTrainer(trainerName) {
        var filter, table, tr, td, i, txtValue;
        filter = trainerName.toUpperCase();
        table = document.getElementById("mainTable");
        tr = table.getElementsByTagName("tr");
        if (isFiltered) {
            for (i = 0; i < tr.length; i++) {
                tr[i].style.display = "";
            }
            isFiltered = false;
        } else {
            for (i = 0; i < tr.length; i++) {
                td = tr[i].getElementsByClassName("trainer-name")[0];
                if (td) {
                    txtValue = td.textContent || td.innerText;
                    if (txtValue.toUpperCase() == filter) {
                        tr[i].style.display = "";
                    } else {
                        tr[i].style.display = "none";
                    }
                }
            }
            isFiltered = true;
        }
    }

    function showReview(el) {
        var review = document.getElementById("review");
        if (reviewLoaded) {
            review.style.visibility = 'hidden';
            review.innerHTML = "";
            reviewLoaded = false;
        } else {
            var buttonTd = $(el).closest("td");
            var reviewText = $(el).closest("tr")
                .find(".reviewTd")
                .text();
            var top = $(buttonTd).position().top + $(buttonTd).outerHeight();
            var left = $(el).position().left;
            review.innerHTML = reviewText;
            $("#review").css({top: top, left: left});
            review.style.visibility = 'visible';
            reviewLoaded = true;
        }
    }

    function sendUserData(name, value, textArea) {
        jQuery.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/controller?command=edit_user_data&name=' + name +
                '&value=' + value,
            success: function (responseJson) {
                console.log(responseJson);
                if (responseJson == null) {
                    textArea.style.borderColor = 'red';
                } else {
                    textArea.style.borderColor = 'green';
                }
            }
        })
    }

    function editSettings(el) {
        var i = el.firstChild;
        var textArea = el.previousSibling;
        textArea.style.borderWidth = '2px';
        if (isFormEditing) {
            sendUserData(textArea.getAttribute("name"), textArea.value, textArea)
            textArea.readOnly = true;
            $(i).attr('class', 'fa fa-edit fa-fw');
            console.log();
            $('.fa-edit').parent().css("pointer-events","auto");
            isFormEditing = false;
        } else {
            $(i).attr('class', 'fa fa-check fa-fw');
            $('.fa-edit').parent().css("pointer-events","none");
            textArea.readOnly = false;
            isFormEditing = true;
        }
    }
</script>
</body>
</html>