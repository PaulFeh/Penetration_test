$(document).ready(function () {

    $("#xpath_injection").submit(function (event) {
        event.preventDefault();
        document.getElementById("xpath_injection_result").innerHTML = ''
        var search = {}
        search['email_address'] = $("#email").val();
        $("#val_submit").prop("disabled", true);
        $.ajax({
            type: "POST",
            url: "/xpath_injection/",
            data: search,
            cache: false,
            timeout: 600000,
            success: function (xpath_data) {
                if (xpath_data.msg != '') {
                    $('#xpath_injection_result').append(xpath_data.msg);
                }
                $("#val_submit").prop("disabled", false);
            },
            error: function (e) {
                $('#xpath_injection_result').append(xpath_data.msg);
                $("#val_submit").prop("disabled", false);
            }
        });
    });

});