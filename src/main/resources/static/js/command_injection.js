$(document).ready(function () {

    $("#command_injection").submit(function (event) {
        event.preventDefault();
        document.getElementById("command_injection_result").innerHTML = ''
        var search = {}
        search['ip_address'] = $("#ip_address").val();
        $("#command_submit").prop("disabled", true);
        $.ajax({
            type: "POST",
            url: "/command_injection/output/",
            data: search,
            cache: false,
            timeout: 600000,
            success: function (command_data) {
                // var data = JSON.parse(command_data);
                if (command_data.msg != '') {
                    $('#command_injection_result').append(command_data.msg);
                }
                $("#command_submit").prop("disabled", false);
            },
            error: function (e) {
                // var json = JSON.parse(command_data);
                console.log(command_data.msg)
                $('#command_injection_result').append(command_data.msg);
                $("#command_submit").prop("disabled", false);
            }
        });
    });

});