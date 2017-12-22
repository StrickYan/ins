$(function() {
    getQueryResultDetails(view_id);

    // 自动隐藏导航栏
    $(window).scroll(function() {
        //$(document).scrollTop() 获取垂直滚动的距离
        //$(document).scrollLeft() 这是获取水平滚动条的距离
        // if ($(document).scrollTop() <= 0) {
        //     alert("滚动条已经到达顶部为0");
        // }
        if ($(document).scrollTop() <= 0 || ($(document).scrollTop() >= $(document).height() - $(window).height() - 16)) {
            $('#header').slideDown('slow');
        }
        else if ($(document).scrollTop() > 0){
            $('#header').slideUp('slow')
        }
    });

});
// 获取具体一条搜索结果详情
function getQueryResultDetails(view_id) {
    $.ajax({
        type: "POST",
        data: {
            'view_id': view_id,
        },
        dataType: "json",
        url: "./getQueryResultDetails",
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            // alert("加载错误，错误原因：\n" + errorThrown);
        },
        success: function(data) {
            var result = "";

            result += "<div id='content'>";
            result += "<p>" + data['retData']['content'] + "</p>";
            result += "</div>";

            $("#page").html(result);
        }
    });
}