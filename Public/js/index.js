$(function() {
    // addStatisticalData();
    if (window.screen.width < window.screen.height) {
        $('#about_img img').css("width", "100%");
    }
    else {
        $('#about_img img').css("width", "30%");
    }

    $('#about').on('click', function() {
        if ($("#about_img").css("display") == "none") {
            $("#about_img").slideDown('slow');
        }
        else {
            $("#about_img").slideUp('slow');
        }
        
    });
});
// 增加统计数据
function addStatisticalData() {
    $.ajax({
        type: "POST",
        data: {},
        dataType: "json",
        url: "Index/addStatisticalData",
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            // alert("加载错误，错误原因：\n" + errorThrown);
        },
        success: function(data) {}
    });
}
