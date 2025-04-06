$(document).ready(function () {

    $("#path_manipulation").submit(function (event) {
        event.preventDefault();
        document.getElementById("path_manipulation_result").innerHTML = ''
        var search = {}
        search['file_name'] = $("#fileName").val();
        $("#path_submit").prop("disabled", true);
        $.ajax({
            type: "GET",
            url: "/path_manipulation/viewFile/",
            data: search,
            cache: false,
            timeout: 600000,
            success: function (path_data) {
                if (path_data.msg != '') {
                    $('#path_manipulation_result').append(path_data.msg);
                }
                $("#path_submit").prop("disabled", false);
            },
            error: function (e) {
                $('#path_manipulation_result').append(path_data.msg);
                $("#path_submit").prop("disabled", false);
            }
        });
    });

});