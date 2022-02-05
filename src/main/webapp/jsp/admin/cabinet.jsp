<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="com.epam.jwd.fitness_center.model.entity.UserStatus" %>
<%@ page import="com.epam.jwd.fitness_center.model.entity.UserRole" %>
<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" %>
<fmt:setLocale value="${sessionScope.locale}"/>
<fmt:setBundle basename="locale"/>
<fmt:message var="title" key="user.cabinet.title"/>
<fmt:message var="welcome" key="user.cabinet.welcome"/>
<fmt:message var="currentOrders" key="user.cabinet.currentOrders"/>
<fmt:message var="personalTrainer" key="user.cabinet.personalTrainer"/>
<fmt:message var="orders" key="user.cabinet.orders"/>
<fmt:message var="settings" key="user.cabinet.settings"/>
<fmt:message var="notValidTitle" key="payment.input.notValid"/>
<fmt:message var="usersManage" key="admin.cabinet.button.usersManage"/>
<fmt:message var="programsManage" key="admin.cabinet.button.programsManage"/>
<fmt:message var="discountManage" key="admin.cabinet.button.discountManage"/>
<fmt:message var="login" key="admin.cabinet.button.login"/>
<fmt:message var="name" key="admin.cabinet.button.name"/>
<fmt:message var="surname" key="admin.cabinet.button.surname"/>
<fmt:message var="role" key="admin.cabinet.button.role"/>
<fmt:message var="status" key="admin.cabinet.button.status"/>
<fmt:message var="change" key="admin.cabinet.button.change"/>
<fmt:message var="discount" key="admin.cabinet.button.discount"/>
<fmt:message var="naming" key="admin.cabinet.button.naming"/>
<fmt:message var="cost" key="admin.cabinet.button.cost"/>
<fmt:message var="delete" key="admin.cabinet.button.delete"/>
<fmt:message var="add" key="admin.cabinet.button.add"/>
<fmt:message var="save" key="admin.cabinet.button.save"/>


<html>
<head>
    <meta charset="UTF-8">
    <title>${title}</title>
    <link href="${pageContext.request.contextPath}/style/style.css" type="text/css" rel="stylesheet">
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
    <script src="https://ajax.googleapis.com/ajax/libs/jqueryui/1.11.2/jquery-ui.min.js"></script>
    <script type="text/javascript" src="${pageContext.request.contextPath}/script/paging.js"></script>
    <style>
        footer {
            position: relative;
        }

        th {
            text-align: inherit;
        }
    </style>
</head>
<body onload="showUsers()">
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
        <button class="w3-bar-item w3-button w3-padding" onclick="showUsers()">
            <i class="fa fa-user fa-fw"></i>${usersManage}
        </button>
        <button class="w3-bar-item w3-button w3-padding" onclick="showItems()">
            <i class="fa fa-tasks fa-fw"></i>${programsManage}
        </button>
        <button class="w3-bar-item w3-button w3-padding" onclick="showUsersDiscount()">
            <i class="fa fa-percent fa-fw"></i>${discountManage}
        </button>
    </div>
</nav>
<div class="w3-main" style="margin-left:300px;padding-top:90px; height: auto; padding-bottom: 50px; min-height: 95%">
    <input id="myInput" type="text" style="margin-left: 255px;">
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
    var editingElem = "";
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

    function showUsers(rowId, pageNum) {
        document.getElementById("myInput").style.display = "";
        jQuery.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/controller?command=show_manage_users',
            success: function (responseJson) {
                document.getElementById("mainData").innerHTML = "";
                var $table = $(`<table class="custom-table table-striped" id="tableData">`).appendTo($("#mainData"));
                $(`<thead id="table-thead">`).appendTo($table)
                    .append($(`<tr>
                                   <th onclick="sortTable(0, true)" style="cursor: pointer">ID</th>
                                   <th>${login}</th>
                                   <th>${name}</th>
                                   <th>${surname}</th>
                                   <th>${role}</th>
                                   <th>${status}</th>
                               </tr>`));
                $.each(responseJson, function (index, user) {
                    var lastTd = `<button onclick="editUser(this)" ` +
                        `class="btn btn-warning">${change}</button>`;
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
            },
            complete: function () {
                $('#tableData').paging({limit:10});
                if(rowId != null) {
                    highlightTr(rowId);

                }
                if(pageNum != null) {
                    $(".paging-nav").find('[data-page="' + pageNum +'"]')[0].click();
                }
            }
        });
    }

    function showUsersDiscount(rowId) {
        document.getElementById("myInput").style.display  = "none";
        jQuery.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/controller?command=show_manage_discount',
            success: function (responseJson) {
                document.getElementById("mainData").innerHTML = "";
                var $table = $(`<table class="custom-table" id="tableData">`).appendTo($("#mainData"));
                $("<thead>").appendTo($table)
                    .append($(`<tr>
                                   <th onclick="sortTable(0, true)" style="cursor: pointer">ID</th>
                                   <th>${login}</th>
                                   <th>${name}</th>
                                   <th>${surname}</th>
                                   <th>${role}</th>
                                   <th>${status}</th>
                                   <th>${discount}</th>
                               </tr>`));
                console.log(responseJson);
                $.each(responseJson.users, function (index, user) {
                    var lastTd = `<button onclick="editDiscount(this)" ` +
                        `class="btn btn-warning">${change}</button>`;
                    $("<tr style='border-color:'>").appendTo($table)
                        .append($("<td class='user-id'>").text(user.id))
                        .append($("<td>").text(user.email))
                        .append($("<td>").text(user.firstName))
                        .append($("<td>").text(user.secondName))
                        .append($(`<td class="role-td">`).text(user.role))
                        .append($(`<td class="status-td">`).text(user.status))
                        .append($(`<td><input type="number" required readonly class="inp-adm discount" name="discount"
                                              min="0" max="99"
                                              value="` + getDetailsById(responseJson.userDetails, user.id) +
                            `" step=".5"></td>`))
                        .append($("<td>").append(lastTd));
                    console.log(getDetailsById(responseJson.userDetails, user.id));
                });
            },
            complete: function () {
                if(rowId != null) {
                    highlightTr(rowId);
                }
            }
        });
    }

    function getDetailsById(data, id) {
        return data.filter(
            function (data) {
                return data.userId == id
            }
        )[0].discount;
    }

    function showItems(rowId) {
        document.getElementById("myInput").style.display = "none";
        jQuery.ajax({
            type: 'GET',
            url: '${pageContext.request.contextPath}/controller?command=show_manage_items',
            success: function (responseJson) {
                document.getElementById("mainData").innerHTML = "";
                var $table = $(`<table class="custom-table" id="tableData" style="min-width: 600px">`).appendTo($("#mainData"));
                $("<thead>").appendTo($table)
                    .append($(`<tr>
                                   <th onclick="sortTable(0)" style="cursor: pointer">${naming}</th>
                                   <th onclick="sortTable(0, true)" style="cursor: pointer">${cost}</th>
                               </tr>`));
                $.each(responseJson, function (index, item) {
                    $("<tr style='border-color:'>").appendTo($table)
                        .append($("<td class='item-id' style='display: none'>").text(item.id))
                        .append($("<td class='item-name' >")
                            .append($(`<textarea required readonly class="ta-adm item-name" name="item-name" rows="1"
                                             cols="11" minlength="2" maxlength="45" oninvalid="setCustomValidity('${notValidTitle}')"
                                             oninput="setCustomValidity('')">`).text(item.name)))
                        .append($(`<td><input type="number" required readonly class="inp-adm price" name="price" min="10" max="9999"
                                          value="` + item.price + `" step=".01"></td>`))
                        .append($("<td>").append(`<button onclick="editItem(this)" ` +
                            `class="btn btn-warning">${change}</button>`))
                        .append($("<td>").append(`<button onclick="editItem(this, true)" ` +
                            `class="btn btn-danger">${delete}</button>`));
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
                        `class="btn btn-danger">${add}</button>`));
            },
            complete: function () {
                if(rowId != null) {
                    highlightTr(rowId);
                }
            }
        });
    }

    function sendUserData(id, name1, name2, value1, value2, elem) {
        if (value1 === '' || value2 === '') {
            elem.style.border = '1px solid red';
            return;
        }
        var page = document.getElementsByClassName("selected-page")[0].getAttribute('data-page');
        jQuery.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/controller',
            data: jQuery.param({
                command: 'manage_user_data',
                userId: id,
                name: [name1, name2],
                value: [value1, value2]
            }, true),
            success: function (responseJson) {
                if (responseJson.includes('error')) {
                    elem.style.border = '1px solid red';
                } else {
                    showUsers(id, page);
                }
            }
        })
    }

    function sendItemData(id, name, price, elem) {
        if (name === '' || price === '' || price === '0') {
            elem.style.border = '1px solid red';
            return;
        }
        jQuery.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/controller',
            data: jQuery.param({command: 'manage_item_data', id: id, name: name, price: price}),
            success: function (responseJson) {
                console.log(responseJson);
                if (responseJson.includes('error')) {
                    elem.style.border = '1px solid red';
                } else {
                    showItems(id);
                }
            }
        })
    }

    function deleteItem(id, elem) {
        jQuery.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/controller',
            data: jQuery.param({command: 'delete_item', id: id}),
            success: function (responseJson) {
                console.log(responseJson);
                if (responseJson.toString().includes('error')) {
                    elem.style.border = '1px solid red';
                } else {
                    hideTr(id);
                }
            }
        })
    }

    function sendNewItemData(name, price, elem) {
        if (name === '' || price === '' || price === '0') {
            elem.style.border = '1px solid red';
            return;
        }
        jQuery.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/controller',
            data: jQuery.param({command: 'manage_new_item_data', name: name, price: price}),
            success: function (responseJson) {
                console.log(responseJson);
                if (responseJson.includes('error')) {
                    elem.style.border = '1px solid red';
                } else {
                    showItems();
                }
            }
        });
    }

    function sendNewDiscount(id, discount, elem) {
        if (discount < 0 || discount > 99) {
            elem.style.border = '1px solid red';
            return;
        }
        jQuery.ajax({
            type: 'POST',
            url: '${pageContext.request.contextPath}/controller',
            data: jQuery.param({command: 'manage_new_discount', id: id, discount: discount}),
            success: function (responseJson) {
                console.log(responseJson);
                if (responseJson.includes('error')) {
                    elem.style.border = '1px solid red';
                } else {
                    showUsersDiscount(id);
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
        if(isFormEditing === true && ("item" + id) !== editingElem) {
            isFormEditing = false;
        }
        var textArea = trClosest.find('textarea')[0];
        var input = trClosest.find('.price')[0];
        var name = textArea.value;
        var price = input.value;
        if (isToDelete) {
            deleteItem(id, trClosest[0]);
        } else if (isFormEditing) {
            sendItemData(id, name, price, trClosest[0]);
            $(el).closest('button')[0].innerText = "${change}"
            textArea.readOnly = true;
            input.readOnly = true;
            isFormEditing = false;
            editingElem = "";
        } else {
            textArea.readOnly = false;
            input.readOnly = false;
            $(el).closest('button')[0].innerText = "${save}"
            isFormEditing = true;
            editingElem = "item" + id;
        }
    }

    function editDiscount(el) {
        var trClosest = $(el).closest('tr');
        var id = trClosest.find('.user-id')[0].innerHTML;
        if(isFormEditing === true && ("disc" + id) !== editingElem) {
            isFormEditing = false;
        }
        var input = trClosest.find('.discount')[0];
        var discount = input.value;
        if (isFormEditing) {
            sendNewDiscount(id, discount, trClosest[0]);
            $(el).closest('button')[0].innerText = "${change}"
            input.readOnly = true;
            isFormEditing = false;
            editingElem = "";
        } else {
            input.readOnly = false;
            $(el).closest('button')[0].innerText = "${save}"
            isFormEditing = true;
            editingElem = "disc" + id;
        }
    }

    function editUser(el) {
        var trClosest = $(el).closest('tr');
        var id = trClosest.find('.user-id')[0].innerHTML;
        if(isFormEditing === true && ("user" + id) !== editingElem) {
            isFormEditing = false;
        }
        var roleTd = trClosest.find('.role-td')[0];
        var statusTd = trClosest.find('.status-td')[0];
        var roleSelectTd = trClosest.find('.role-select-td')[0];
        var statusSelectTd = trClosest.find('.status-select-td')[0];
        if (isFormEditing) {
            sendUserData(id, 'role', 'status', $(roleSelectTd.firstChild).val(), $(statusSelectTd.firstChild).val(),
                trClosest[0]);
            displayByElem(roleSelectTd, true);
            displayByElem(statusSelectTd, true);
            displayByElem(roleTd);
            displayByElem(statusTd);
            $(el).closest('button')[0].innerText = "${change}"
            $(el).attr('class', 'btn btn-warning');
            isFormEditing = false;
            editingElem = "";
        } else {
            $(roleSelectTd.firstChild).val(roleTd.innerText);
            $(statusSelectTd.firstChild).val(statusTd.innerText);
            displayByElem(roleTd, true);
            displayByElem(statusTd, true);
            displayByElem(roleSelectTd);
            displayByElem(statusSelectTd);
            $(el).closest('button')[0].innerText = "${save}"
            $(el).attr('class', 'btn btn-danger');
            isFormEditing = true;
            editingElem = "user" + id;
        }
    }

    function displayByElem(elem, isToHide = false) {
        var displayVal = '';
        if (isToHide) {
            displayVal = 'none';
        }
        elem.style.display = displayVal;
    }

    function sortTable(n, isNumber = false) {
        var table, rows, switching, i, x, y, shouldSwitch, dir, switchcount = 0;
        table = document.getElementById("tableData");
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
                    if (isNumber) {
                        if (Number(x.innerHTML) > Number(y.innerHTML)) {
                            shouldSwitch = true;
                            break;
                        }
                    } else if (x.innerHTML.toLowerCase() > y.innerHTML.toLowerCase()) {
                        shouldSwitch = true;
                        break;
                    }
                } else if (dir == "desc") {
                    if (isNumber) {
                        if (Number(x.innerHTML) < Number(y.innerHTML)) {
                            shouldSwitch = true;
                            break;
                        }
                    } else if (x.innerHTML.toLowerCase() < y.innerHTML.toLowerCase()) {
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
            if('${entry}' !== '${UserRole.GUEST.name()}') {
                arr.push('${entry}');
            }
        </c:forEach>
        return arr;
    }

    function getStatuses() {
        return ['ACTIVE', 'BANNED'];
    }

    function insertOptions(arr, id) {
        $.each(arr, function (i, p) {
            $('.' + id).append($(`<option value="${p}"></option>`)
                .val(p).html(p));
        });
    }



    function hideTr(id) {
        var el = $("td:contains("+ id +")")[0].parentNode;
        el.style.display = 'none';
    }

    function highlightTr(id) {
        var el = $("td:contains("+ id +")")[0].parentNode;
        el.style.border = '1px solid green';
    }

    $(document).ready(function(){
        $("#myInput").on("keyup", function() {
            if($(this).val() == "") {
                showUsers();
            }
            var value = $(this).val().toLowerCase();
            $("tbody tr").filter(function() {
                $(this).toggle($(this).children('td').slice(0, 6).text().toLowerCase().indexOf(value) > -1)
            });
        });
    });
</script>
</body>
</html>