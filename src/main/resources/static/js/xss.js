$(document).ready(function () {
    function ajax_call(searchKey, search_val, display_div, url){
        document.getElementById(display_div).innerHTML = ''
        var search = {}
        search[searchKey] = $(search_val).val();
        $.ajax({
            type: 'GET',
            url: url,
            data: search,
            cache: false,
            timeout: 600000,
            success: function (data) {
                if (search_val == '#js_xss_name') {
                    var hrf = "<a href='www.example.com/" + data + "' id='link' target='_blank'>Click to download</a>"
                    $('#' + display_div).html(hrf)
                }
                else {
                    $('#' + display_div).append(data);
                }
            },
            error: function (e) {
                $('#' + display_div).append('Error happend !!');
            }
        });
    }

    $("#body_xss").submit(function (event) {
        event.preventDefault();
        ajax_call('body_tagVal', '#body_xss_name', 'body_xss_result', '/xss/body_xss/');
    });

    $("#textarea_xss").submit(function (event) {
        event.preventDefault();
        ajax_call('textarea_tagVal', '#textarea_xss_name', 'textarea_xss_name', '/xss/textarea_xss/');
    });

    $("#js_xss").submit(function (event) {
        event.preventDefault();
        ajax_call('js_tagVal', '#js_xss_name', 'js_xss_result', '/xss/js_xss/');
    });

});