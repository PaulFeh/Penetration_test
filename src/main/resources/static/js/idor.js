$(document).ready(function () {

    $("#idor_login").submit(function (event) {
        event.preventDefault();
        var btn = $(this).find("input[type=submit]:focus" );
        if (btn[0].id == 'login-submit')
            fire_login_submit();
        else if (btn[0].id == 'reset-submit')
            fire_reset_submit();
    });

    $("#get_profile").submit(function (event) {
        event.preventDefault();
        get_profile();

    });

    $("#get_performance").submit(function (event) {
        event.preventDefault();
        get_performance_info();

    });

});

function fire_login_submit() {
    var search = {}
    search['customer_email'] = $("#customer_email").val();
    search['customer_password'] = $("#customer_password").val();

    $("#login-submit").prop("disabled", true);

    $.ajax({
        type: "POST",
        /* contentType: "application/json", */
        url: "/idor/",
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

function fire_reset_submit() {
    $("#reset-submit").prop("disabled", true);
    $.ajax({
        type: "POST",
        url: "/idor/reset",
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

function get_profile() {
    var search = {}
    $("#submit_profile").prop("disabled", true);

    $.ajax({
        type: "GET",
        url: "/idor/own/",
        dataType: "json",
        data: search,
        cache: false,
        timeout: 600000,
        success: function (data) {
            $('#alert-own-info').empty();
            if (data.status != undefined && data.status == 'error') {
                var json = JSON.stringify(data.msg);
                $('#alert-own-info').append(json);
            } else {
                table_html = ''
                var data_length = data.length;
                if (data_length > 0) {
                    table_html += '<table style="width:60%">'
                    table_html += '<tr><th>Name</th><th>Email</th><th>Phone</th></tr>'
                    for (var i = 0; i < data_length; i++) {
                        item = data[i]
                        table_html += '<tr><td>' + item.name + '</td><td>' + item.email + '</td><td>' + item.phone + '</td></tr>';
                    }

                    table_html += '</table>'
                    $('#alert-own-info').append(table_html);
                }
            }
            $("#submit_profile").prop("disabled", false);
        },
        error: function (e) {
            var json = JSON.stringify(data.msg);
            $('#alert-own-info').html(json);
            $("#submit_profile").prop("disabled", false);
        }
    });
}

function get_performance_info() {
    var search = {}
    search['requested_customer_id'] = $("#customer_id").val();
    /* search['customer_password'] = $("#customer_password").val(); */

    $("#submit_info").prop("disabled", true);

    $.ajax({
        type: "GET",
        url: "/idor/info/",
        dataType: "json",
        data: search,
        cache: false,
        timeout: 600000,
        success: function (data) {
            $('#alert-profile-msg').empty();
            $('#alert-profile-info').empty();

            if (data.status != undefined && data.status == 'error') {
                var json = JSON.stringify(data.msg);
                $('#alert-profile-msg').append(data.msg);
            } else {
                table_html = ''

                table_html += '<table  style="width:70%">'
                table_html += '<tr><th>Name</th><th>Email</th><th>Performance Evaluation(%)</th></tr>'
                table_html += '<tr><td>' + data.name + '</td><td>' + data.email + '</td><td>' + data.performance + '</td></tr>';
                table_html += '</table>'

                $('#alert-profile-info').append(table_html);
            }
            $("#submit_info").prop("disabled", false);
        },
        error: function (e) {
            var json = JSON.stringify(data.msg);
            $('#alert-profile-info').html(json);
            $("#submit_info").prop("disabled", false);
        }
    });
}