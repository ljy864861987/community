$(function () {
    $("#getVerifyCode").click(send_verifyCodeEmail);
});

function send_verifyCodeEmail() {
    var email = $("#your-email").val();

    if (!email) {
        alert("请先填写您的邮箱！");
        return false;
    }
    $.get(
        CONTEXT_PATH + "/forget/sendVerifyCode",
        {"email": email},
        function (data) {
            data = $.parseJSON(data);
            if (data.code == 0) {
                alert("验证码发送成功！");
            } else {
                alert(data.msg);
            }
        }
    );
}