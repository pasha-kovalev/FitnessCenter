<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.epam.jwd.fitness_center.model.entity.UserStatus" %>
<%@ page import="com.epam.jwd.fitness_center.model.entity.UserRole" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.locale}" />
<fmt:setBundle basename="locale" />
<fmt:message var="title" key="user.cabinet.title"/>
<fmt:message var="welcome" key="user.cabinet.welcome"/>
<fmt:message var="currentOrders" key="user.cabinet.currentOrders"/>
<fmt:message var="personalTrainer" key="user.cabinet.personalTrainer"/>
<fmt:message var="orders" key="user.cabinet.orders"/>
<fmt:message var="settings" key="user.cabinet.settings"/>

<html>
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link href="../../style/style.css" type="text/css" rel="stylesheet">
    <script src="https://code.jquery.com/jquery-latest.min.js"></script>
    <style>
        .ta-adm, .inp-adm {
            background-color: inherit;
            border: none;
            color: white;
            width: 150px;
        }
        footer {
            position: relative;
        }
        .custom-table {
            min-width: 1100px;
            margin: 0 auto;
            color: #212529;
            font-family: "Roboto",-apple-system,BlinkMacSystemFont,"Segoe UI",Roboto,"Helvetica Neue",Arial,"Noto Sans",sans-serif,"Apple Color Emoji","Segoe UI Emoji","Segoe UI Symbol","Noto Color Emoji";
            border-collapse: collapse;
        }

        .custom-table thead tr, .custom-table thead th {
            padding-bottom: 30px;
            border-top: none;
            border-bottom: none!important;
            color: #F5F5F5;
            font-size: 16px;
            text-transform: uppercase;
            letter-spacing: .2rem;
        }
        .custom-table thead th {
            vertical-align: bottom;
            border-bottom: 2px solid #dee2e6;
        }
        .custom-table th, .custom-table td {
            padding: .75rem;
            vertical-align: top;
            border-top: 1px solid #dee2e6;
        }
        th {
            text-align: inherit;
        }

        .custom-table tbody tr:nth-of-type(odd) {
            background-color: rgba(0,0,0,.2);
        }
        .custom-table tbody tr {
            -webkit-transition: .3s all ease;
            -o-transition: .3s all ease;
            transition: .3s all ease;
        }
        .custom-table tbody th, .custom-table tbody td {
            color: #DCDCDC;
            font-weight: 400;
            padding-bottom: 15px;
            padding-top: 15px;
            font-weight: 300;
            border: none;
            -webkit-transition: .3s all ease;
            -o-transition: .3s all ease;
            transition: .3s all ease;
        }
        .custom-table th, .custom-table td {
            padding: .75rem;
            vertical-align: top;
            border-top: 1px solid #dee2e6;
        }
    </style>
</head>
<body onload="showUsers()">
<jsp:include page="../component/header.jsp" flush="true"/>
<nav class="w3-sidebar w3-collapse w3-white w3-animate-left" style="z-index:3;width:300px;margin-top: 46px" id="mySidebar"><br>
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
        <button class="w3-bar-item w3-button w3-padding" onclick="showUsers()">
            <i class="fa fa-user fa-fw"></i>Управление Пользователями</button>
        <button class="w3-bar-item w3-button w3-padding" onclick="showItems()">
            <i class="fa fa-tasks fa-fw"></i>Управление Программами</button>
        <button class="w3-bar-item w3-button w3-padding" onclick="manageDiscount()">
            <i class="fa fa-percent fa-fw"></i>Управление Скидками</button>
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
    var statusArrCurrentPos = 0;

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

    function showUsers() {
        jQuery.ajax({
            type: 'GET',
            url : '${pageContext.request.contextPath}/controller?command=show_manage_users',
            success : function(responseJson) {
                document.getElementById("mainData").innerHTML = "";
                var $table = $(`<table class="custom-table" id="mainTable">`).appendTo($("#mainData"));
                $("<thead>").appendTo($table)
                    .append($(`<tr>
                                   <th onclick="sortTable(0, true)" style="cursor: pointer">ID</th>
                                   <th>Логин</th>
                                   <th>Имя</th>
                                   <th>Фамилия</th>
                                   <th onclick="filterBy('role')" style="cursor: pointer">Роль</th>
                                   <th onclick="filterBy('status')" style="cursor: pointer">Статус</th>
                               </tr>`));
                $.each(responseJson, function(index, user) {
                    var lastTd = `<button onclick="editUser(this)" ` +
                        `class="btn btn-warning">Изменить</button>`;
                    $("<tr style='border-color:'>").appendTo($table)
                        .append($("<td class='user-id'>").text(user.id))
                        .append($("<td>").text(user.email))
                        .append($("<td>").text(user.firstName))
                        .append($("<td>").text(user.secondName))
                        .append($(`<td class="role-td">`).text(user.role))
                        .append($(`<td class="status-td">`).text(user.status))
                        .append($(`<td class="role-select-td" style="display: none">`)
                            .append(`<select name="role-select" class="role-select">`))
                        .append($(`<td class="status-select-td" style="display: none">`)
                            .append(`<select name="status-select" class="status-select">`))
                        .append($("<td>").append(lastTd));
                });
                insertOptions(getRoles(), 'role-select');
                insertOptions(getStatuses(), 'status-select');
            }
        });
    }

    function showItems() {
        jQuery.ajax({
            type: 'GET',
            url : '${pageContext.request.contextPath}/controller?command=show_manage_items',
            success : function(responseJson) {
                document.getElementById("mainData").innerHTML = "";
                var $table = $(`<table class="custom-table" id="mainTable" style="min-width: 600px">`).appendTo($("#mainData"));
                $("<thead>").appendTo($table)
                    .append($(`<tr>
                                   <th onclick="sortTable(0)" style="cursor: pointer">Название</th>
                                   <th onclick="sortTable(0, true)" style="cursor: pointer">Стоимость</th>
                               </tr>`));
                $.each(responseJson, function(index, item) {
                    $("<tr style='border-color:'>").appendTo($table)
                        .append($("<td class='item-id' style='display: none'>").text(item.id))
                        .append($("<td class='item-name' >")
                            .append($(`<textarea required readonly class="ta-adm item-name" name="item-name" rows="1"
                                             cols="11" minlength="2" maxlength="45" oninvalid="setCustomValidity('${notValidTitle}')"
                                             oninput="setCustomValidity('')">`).text(item.name)))
                        .append($(`<td><input type="number" required readonly class="inp-adm price" name="price" min="10" max="9999"
                                          value="` + item.price +`" step=".01"></td>`))
                        .append($("<td>").append(`<button onclick="editItem(this)" ` +
                            `class="btn btn-warning">Изменить</button>`))
                        .append($("<td>").append(`<button onclick="editItem(this, true)" ` +
                                `class="btn btn-danger">Удалить</button>`));
                });
                $("<tr style='border-color:'>").appendTo($table)
                    .append($("<td class='item-name' >")
                        .append($(`<textarea required class="ta-adm item-name" name="item-name" rows="1"
                                             cols="11" minlength="2" maxlength="45" oninvalid="setCustomValidity('${notValidTitle}')"
                                             oninput="setCustomValidity('')" value="program"
                                             style="border: 1px solid whitesmoke;">`)))
                    .append($(`<td><input type="number" required class="inp-adm price" name="price" min="10" max="9999"
                                          value="" step=".01" style="border: 1px solid whitesmoke;"></td>`))
                    .append($("<td>").append(`<button onclick="addNewItem(this)" ` +
                        `class="btn btn-danger">Добавить</button>`));
            }
        });
    }

    function sendUserData(id, name1, name2, value1, value2, elem) {
        if(value1 === '' || value2 === '') {
            elem.style.border = '1px solid red';
            return;
        }
        jQuery.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/controller',
            data: jQuery.param({command: 'manage_user_data', userId: id, name: [name1, name2], value: [value1, value2]}, true),
            success : function(responseJson) {
                if(responseJson.includes('error')) {
                    elem.style.border = '1px solid red';
                } else {
                    elem.style.border = '1px solid green';
                }
            }
        })
    }

    function sendItemData(id, name, price, elem) {
        if(name === '' || price === '' || price === '0') {
            elem.style.border = '1px solid red';
            return;
        }
        jQuery.ajax({
            type : 'POST',
            url : '${pageContext.request.contextPath}/controller',
            data: jQuery.param({command: 'manage_item_data', id: id, name: name, price: price}),
            success : function(responseJson) {
                console.log(responseJson);
                if(responseJson.includes('error')) {
                    elem.style.border = '1px solid red';
                } else {
                    elem.style.border = '1px solid green';
                }
            }
        })
    }

    function deleteItem(id, elem) {
        jQuery.ajax({
            type : 'POST',
            url : '${pageContext.request.contextPath}/controller',
            data: jQuery.param({command: 'delete_item', id: id}),
            success : function(responseJson) {
                console.log(responseJson);
                if(responseJson.toString().includes('error')) {
                    elem.style.border = '1px solid red';
                } else {
                    elem.style.border = '1px solid green';
                    elem.style.display = 'none';
                }
            }
        })
    }

    function sendNewItemData(name, price, elem) {
        if(name === '' || price === '' || price === '0') {
            elem.style.border = '1px solid red';
            return;
        }
        jQuery.ajax({
            type : 'POST',
            url : '${pageContext.request.contextPath}/controller',
            data: jQuery.param({command: 'manage_new_item_data', name: name, price: price}),
            success : function(responseJson) {
                console.log(responseJson);
                if(responseJson.includes('error')) {
                    elem.style.border = '1px solid red';
                } else {
                    elem.style.border = '1px solid green';
                }
            }
        });
    }

    function addNewItem(el) {
        var trClosest = $(el).closest('tr');
        var textArea = trClosest.find('textarea')[0];
        var input = trClosest.find('.price')[0];
        var name = textArea.value;
        var price = input.value;
        sendNewItemData(name, price, trClosest[0]);
    }

    function editItem(el, isToDelete = false) {
        console.log(isToDelete);
        var trClosest = $(el).closest('tr');
        var id = trClosest.find('.item-id')[0].innerHTML;
        var textArea = trClosest.find('textarea')[0];
        var input = trClosest.find('.price')[0];
        var name = textArea.value;
        var price = input.value;
        if(isToDelete) {
            deleteItem(id, trClosest[0]);
        } else if(isFormEditing) {
                sendItemData(id, name, price, trClosest[0]);
                $(el).closest('button')[0].innerText = "Изменить"
                textArea.readOnly = true;
                input.readOnly = true;
                isFormEditing = false;

        } else {
                textArea.readOnly = false;
                input.readOnly = false;
                $(el).closest('button')[0].innerText = "Сохранить"
                isFormEditing = true;
        }
    }
    function editUser(el) {
        var trClosest = $(el).closest('tr');
        var id = trClosest.find('.user-id')[0].innerHTML;
        var roleTd = trClosest.find('.role-td')[0];
        var statusTd = trClosest.find('.status-td')[0];
        var roleSelectTd = trClosest.find('.role-select-td')[0];
        var statusSelectTd = trClosest.find('.status-select-td')[0];
        if(isFormEditing) {
            console.log(roleSelectTd.firstChild);
            console.log();
            sendUserData(id, 'role', 'status', $(roleSelectTd.firstChild).val(), $(statusSelectTd.firstChild).val(),
                         trClosest[0]);
            displayByElem(roleSelectTd, true);
            displayByElem(statusSelectTd, true);
            displayByElem(roleTd);
            displayByElem(statusTd);
            $(el).closest('button')[0].innerText = "Изменить"
            $(el).attr('class', 'btn btn-warning');
            isFormEditing = false;
        } else {
            $(roleSelectTd.firstChild).val(roleTd.innerText);
            $(statusSelectTd.firstChild).val(statusTd.innerText);
            displayByElem(roleTd, true);
            displayByElem(statusTd, true);
            displayByElem(roleSelectTd);
            displayByElem(statusSelectTd);
            $(el).closest('button')[0].innerText = "Сохранить"
            $(el).attr('class', 'btn btn-danger');
            isFormEditing = true;
        }
    }

    function displayByElem(elem, isToHide = false) {
        var displayVal = '';
        if(isToHide) {
            displayVal = 'none';
        }
        elem.style.display = displayVal;
    }

    function sortTable(n, isNumber = false) {
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
                    if(isNumber) {
                        if (Number(x.innerHTML) > Number(y.innerHTML)) {
                            shouldSwitch = true;
                            break;
                        }
                    }
                    else if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                        shouldSwitch = true;
                        break;
                    }
                } else if (dir == "desc") {
                    if(isNumber) {
                        if (Number(x.innerHTML) < Number(y.innerHTML)) {
                            shouldSwitch = true;
                            break;
                        }
                    }
                    else if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
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

    function getRoles() {
        var arr = [];
        <c:forEach var="entry" items="${UserRole.values()}">
            arr.push('${entry}');
        </c:forEach>
        return arr;
    }

    function getStatuses() {
        var arr = [];
        <c:forEach var="entry" items="${UserStatus.values()}">
            arr.push('${entry}');
        </c:forEach>
        return arr;
    }

    function insertOptions(arr, id) {
        $.each(arr, function(i, p) {
            $('.' + id).append($(`<option value="${p}"></option>`)
                .val(p).html(p));
        });
    }

    function filterBy(columnName) {
        var arr = [];
        var  className = '';
        switch (columnName) {
            case 'role':
                arr = getRoles();
                className = "role-td"
                break;
            case 'status':
                arr = getStatuses();
                className = "status-td"
                break;
        }
        var size = arr.length;
        var filter, table, tr, td, i, txtValue;
        filter = arr[statusArrCurrentPos];
        table = document.getElementById("mainTable");
        tr = table.getElementsByTagName("tr");
        if(statusArrCurrentPos  === size || filter === '${UserRole.GUEST.name()}') {
            for (i = 0; i < tr.length; i++) {
                tr[i].style.display = "";
            }
        } else {
            for (i = 0; i < tr.length; i++) {
                td = tr[i].getElementsByClassName(className)[0];
                if (td) {
                    txtValue = td.textContent || td.innerText;
                    if (txtValue.toUpperCase() == filter) {
                        tr[i].style.display = "";
                    } else {
                        tr[i].style.display = "none";
                    }
                }
            }
    }
    statusArrCurrentPos = ++statusArrCurrentPos % (size + 1);
}
</script>
</body>
</html>