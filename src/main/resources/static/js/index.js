$(function () {
    $("#publishBtn").click(publish);
});

function publish() {
    $("#publishModal").modal("hide");

    //获取标题和正文
    var title = $("#recipient-name").val();
    var content = $("#message-text").val();

    //发送异步请求
    $.post(
        CONTEXT_PATH + "/discuss/add",
        {"title": title, "content": content},
        function (data) {
            data = $.parseJSON(data);
            //设置提示框内容
            $("#hintBody").text(data.msg);
            //显示提示框
            $("#hintModal").modal("show");
            //2秒后自动隐藏
            setTimeout(function () {
                $("#hintModal").modal("hide");
                //发布成功时刷新页面
                if(data.code == 0){
                    window.location.reload();
                }
            }, 2000);
        }
    );

}