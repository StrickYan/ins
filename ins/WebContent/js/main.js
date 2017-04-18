$(function() {
    $("#sort_select").bind('click', function() {
        if ($("#sort_pick").css("display") == "none") {
            $("#sort_pick").slideDown('slow');
        }
        else{
            $("#sort_pick").slideUp('slow');
        }
    });
});