$(document).ready(function () {

    $("#smtp_in").submit(function (event) {
        event.preventDefault();
        document.getElementById("result_smtp").innerHTML = ''
        var search = {}
        search['customer_firstName'] = $("#customer_firstName").val();
		search['customer_email'] = $("#customer_email").val();
		search['customer_comments'] = $("#customer_comments").val();
        $("#login-submit").prop("disabled", true);
        $.ajax({
            type: "GET",
            url: "/smtp_injection/form/",
            data: search,
            cache: false,
            timeout: 600000,
            success: function (command_data) {
                // var data = JSON.parse(command_data);
                if (command_data != '') {
                    $('#result_smtp').append(command_data);
                }
                $("#login-submit").prop("disabled", false);
            },
            error: function (e) {
                // var json = JSON.parse(command_data);
                console.log(command_data.msg)
                $('#result_smtp').append(command_data.msg);
                $("#login-submit").prop("disabled", false);
            }
        });
    });

});