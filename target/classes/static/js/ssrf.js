$(document).ready(function () {
    $("#product_search").submit(function (event) {
        event.preventDefault();
        document.getElementById("product_search_result").innerHTML = ''
        var search = {}
        search['program'] = 'item';
        search['parameter'] = 'id';
        search['param_value'] = $("#value").val();
        $("#product_search_submit").prop("disabled", true);
        $.ajax({
            type: "POST",
            url: /ssrf/,
            data: search,
            cache: false,
            timeout: 600000,
            success: function (product_data) {
                if (product_data.msg != '') {
                    $('#product_search_result').append(product_data.msg);
                }
                $("#product_search_submit").prop("disabled", false);
            },
            error: function (e) {
                $('#product_search_result').append(product_data.msg);
                $("#product_search_submit").prop("disabled", false);
            }
        });
    });

});