$(document).ready(function () {

    $("#log_injection").submit(function (event) {
        event.preventDefault();
        document.getElementById("log_injection_result").innerHTML = ''
        var search = {}
        search['log_value'] = $("#val").val();
        $("#val_submit").prop("disabled", true);
        $.ajax({
            type: "POST",
            url: "/log_injection/",
            data: search,
            cache: false,
            timeout: 600000,
            success: function (log_data) {
                if (log_data.msg != '') {
                    $('#log_injection_result').append(log_data.msg);
                }
                $("#val_submit").prop("disabled", false);
            },
            error: function (e) {
                $('#log_injection_result').append(log_data.msg);
                $("#val_submit").prop("disabled", false);
            }
        });
    });

});