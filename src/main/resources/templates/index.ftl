<!doctype html>
<html>
<head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="initial-scale=1.0, user-scalable=no, width=device-width">
    <title>位置监控</title>
    <link rel="stylesheet" href="https://cache.amap.com/lbs/static/main1119.css"/>
    <script src="https://webapi.amap.com/maps?v=1.4.8&key=您申请的key值"></script>
</head>
<body>
<div id="container"></div>
<script>
    /*高德api开始*/
    var map = new AMap.Map('container', {
        center: ${position},
        zoom: 14
    });
    var marker = new AMap.Marker({
        position: ${position},
        map: map
    });
    /*高德api结束*/

    /*sse开始*/
    if ('EventSource' in window) {
        var source = new EventSource("/sse");

        source.onopen = function (event) {
            console.log("sse连接建立成功");
        };

        source.onmessage = function (event) {
            var data = JSON.parse(event.data);
            console.log(data);
            marker.setPosition(data.position);
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