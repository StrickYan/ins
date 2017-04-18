$(function() {
    getQueryRusultDetails(view_id);

});
// 获取具体一条搜索结果详情
function getQueryRusultDetails(view_id) {
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
            result += "<div id='page'>";
            result += "<div id='content'>";
            result += "<p>" + data['retData']['content'] + "</p>";
            result += "</div>";
            result += "</div>";
            $("body").prepend(result);
        }
    });
}