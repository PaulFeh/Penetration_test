$(document).ready(function () {

    $("#sql_injection_login").submit(function (event) {
        //stop submit the form, we will post it manually.
        event.preventDefault();
        var btn = $(this).find("input[type=submit]:focus" );
        if (btn[0].id == 'login-submit')
            fire_login();
        else if (btn[0].id == 'reset-submit')
            fire_reset();
    });

    function fire_login() {
        var search = {}
        search['employee_username'] = $("#employee_username").val();
        search['employee_password'] = $("#employee_password").val();

        $("#login-submit").prop("disabled", true);

        $.ajax({
            type: "POST",
            /* contentType: "application/json", */
            url: "/sql_injection/",
            data: search,
            cache: false,
            timeout: 600000,
            success: function (data) {
                var json = JSON.stringify(data.msg);
                $('#alert-login-info').html(json);
                $("#login-submit").prop("disabled", false);
            },
            error: function (e) {
                var json = JSON.stringify(data.msg);
                $('#alert-login-info').html(json);
                $("#login-submit").prop("disabled", false);
            }
        });
    }

    function fire_reset() {
        $("#reset-submit").prop("disabled", true);
        $.ajax({
            type: "POST",
            url: "/sql_injection/reset",
            cache: false,
            timeout: 600000,
            success: function (data) {
                var json = JSON.stringify(data.msg);
                $('#alert-login-info').html(json);
                $("#reset-submit").prop("disabled", false);
            },
            error: function (e) {
                var json = JSON.stringify(data.msg);
                $('#alert-login-info').html(json);
                $("#reset-submit").prop("disabled", false);
            }
        });
    }

    function prepare_table(data, appened_div) {
        table_html = ''
        var data_length = data.length;
        if(data_length > 0){
            table_html += '<table style="width: 100%;">'
            table_html += '<tr><td><b>Username</b></td><td><b>First Name</b></td><td><b>Last Name</b></td><td><b>Department</b></td><td><b>Address</b></td></tr>'
            for (var i = 0; i < data_length; i++) {
                item = data[i]
                console.log(item)
                table_html += '<tr><td>' + item.username + '</td><td>' + item.first_name + '</td><td>' + item.last_name + '</td><td>' + item.department+ '</td><td>' + item.address + '</td></tr>';
            }
            table_html += '</table>'
            $('#' + appened_div).append(table_html);
        }
    }

    $("#update_address").submit(function (event) {
        event.preventDefault();
        document.getElementById("update_address_result").innerHTML = ''

        var search = {}
        search['updated_address'] = $("#address").val();
        $("#update_address_submit").prop("disabled", true);
        $.ajax({
            type: "POST",
            /* contentType: "application/json", */
            url: "/sql_injection/update_address/",
            data: search,
            cache: false,
            timeout: 600000,
            success: function (employee_data) {
                var data = JSON.parse(employee_data);
                if (data.msg != '') {
                    $('#update_address_result').append(data.msg);
                }
                prepare_table(data, 'update_address_result')
                $("#update_address_submit").prop("disabled", false);
            },
            error: function (e) {
                var json = JSON.parse(employee_data);
                console.log(json.msg)
                $('#update_address_result').append(json.msg);
                $("#update_address_submit").prop("disabled", false);
            }
        });
    });

});