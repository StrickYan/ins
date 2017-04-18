$(function() {
    getQueryRusult(page);
    checkRadio(sort);
    $("#sort_select").bind('click', function() {
        if ($("#sort_pick").css("display") == "none") {
            $("#sort_pick").slideDown('slow');
        } else {
            $("#sort_pick").slideUp('slow');
        }
    });
});
// 获取搜索结果
function getQueryRusult(page) {
    var q = $('#query').attr("value");
    var start_time = new Date().getTime();
    $.ajax({
        type: "POST",
        data: {
            'q': q,
            'p': page,
            'sort': sort,
        },
        dataType: "json",
        url: "Search/getQueryResult",
        error: function(XMLHttpRequest, textStatus, errorThrown) {
            // alert("加载错误，错误原因：\n" + errorThrown);
        },
        success: function(data) {
            var end_time = new Date().getTime();
            var run_time = end_time - start_time;
            // if (page == 0 && run_time < 1200) {
            //     sleep(1200);
            // }
            // $("#fakeloader").hide();
            // var result = '';
            var tips = "Page " + data['retData']['page']['page'] + " of about " + data['retData']['page']['totalRows'] + " results (" + data['retData']['time'] + "s)";
            $('#tips').html(tips).slideDown('slow');
            $('#cur_page a').html("Page " + data['retData']['page']['page'] + "");
            var p = page - 1 == 0 ? 1 : page - 1;
            var pre_url = "search?q=" + q + "&p=" + p + "&sort=" + sort;
            $('#pre_page a').attr("href", pre_url);
            var next_url = "search?q=" + q + "&p=" + (page + 1) + "&sort=" + sort;
            $('#next_page a').attr("href", next_url);
            for (var i = 0; i < data['retData']['newslist'].length; i++) {
                var result = "";
                result += "<div class='result'>";
                result += "<div class='title'>";
                // result += "<a href='http://www.szu.edu.cn/board/view.asp?id=" + data['retData']['newslist'][i]['id'] + "'>" + data['retData']['newslist'][i]['Title'] + "</a>";
                result += "<a href='search/details?id=" + data['retData']['newslist'][i]['id'] + "'>" + data['retData']['newslist'][i]['Title'] + "</a>";
                result += "</div>";
                result += "<div class='content'>";
                result += "<p>" + data['retData']['newslist'][i]['Artical'] + "...</p>";
                result += "</div>";
                result += "<div class='url'>www.szu.edu.cn</div>";
                result += "</div>";
                $("#page_controller").before(result);
            }
        }
    });
}
// 选择排序方式
function checkRadio(sort) {
    if (sort == "time") {
        $("input[type=radio][name=sort][value=time]").attr("checked", 'checked');
    } else if (sort == "heat") {
        $("input[type=radio][name=sort][value=heat]").attr("checked", 'checked');
    } else {
        $("input[type=radio][name=sort][value=relevancy]").attr("checked", 'checked');
    }
}