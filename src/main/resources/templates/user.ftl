<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <title>长时间操作</title>
</head>
<body>
<div id="container">
<a href="/trigger/${id}" target="_blank">trigger</a>
<p id="userStatus"></p>
</div>
<script>
    /*sse开始*/
    if ('EventSource' in window) {
        var source = new EventSource("/sse/${id}");

        source.onopen = function (event) {
            console.log("sse连接建立成功");
        };

        source.onmessage = function (event) {
            var data = JSON.parse(event.data);
            console.log(data);
            userStatus.innerText = data.progress + "%"
        };

        source.onerror = function (event) {
            console.log("sse连接错误");
        };

    } else {
        alert("请使用chrome或者firefox测试");
    }
    /*sse结束*/

</script>
</body>
</html>