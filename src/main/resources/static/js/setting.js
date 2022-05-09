var bosConfig = {
    credentials: {
        ak: accessKey,
        sk: secretKey
    },
    endpoint: 'http://bj.bcebos.com' // 根据您选用bos服务的区域配置相应的endpoint
};
var bucket = headerBucket; // 设置您想要操作的bucket
var client = new baidubce.sdk.BosClient(bosConfig);
var key;
var blob;
var options;

$('#head-image').on('change', function (evt) {
    var file = evt.target.files[0]; // 获取要上传的文件
    var suffix = file.name.substr(file.name.lastIndexOf('.'));
    key = $("#fileName").val() + suffix;// 保存到bos时的key，您可更改，默认以文件名作为key
    blob = file;

    var ext = key.split(/\./g).pop();
    var mimeType = baidubce.sdk.MimeType.guess(ext);
    if (/^text\//.test(mimeType)) {
        mimeType += '; charset=UTF-8';
    }
    options = {
        'Content-Type': mimeType
    };
});
$("#submit").click(function () {
    client.putObjectFromBlob(bucket, key, blob, options)
        .then(function (res) {
            // 上传完成，添加您的代码
            $.post(
                CONTEXT_PATH + "/user/header/url",
                {"fileName": key},
                function (data) {
                    data = $.parseJSON(data);
                    window.location.reload();
                }
            );
        })
        .catch(function (err) {
            // 上传失败，添加您的代码
            alert("头像上传失败！");
        });
    return false;
});
