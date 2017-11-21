var kmNetWorkFlag; //当前网络状态判定函数

mui.plusReady(function() {
    document.addEventListener("netchange", neworkchange, false);
    //获取当前网络类型 0 为wifi 1为流浪或者蜂窝网络  2为断网状态或其他状态
    function neworkchange() {
        var nt = plus.networkinfo.getCurrentType();
        switch (nt) {
            case plus.networkinfo.CONNECTION_ETHERNET:
            case plus.networkinfo.CONNECTION_WIFI:
                // mui.toast("当前网络为WiFi");
                return 0;
                break;
            case plus.networkinfo.CONNECTION_CELL2G:
            case plus.networkinfo.CONNECTION_CELL3G:
            case plus.networkinfo.CONNECTION_CELL4G:
                // mui.toast("当前网络非WiFi");
                return 1;
                break;
            default:
                // mui.toast("当前没有网络");
                return 2;
                break;
        }
    }
    kmNetWorkFlag = function() {
        var m = neworkchange();
        if (m == 2) {
            mui.toast("当前没有网络");
            return 2;
        }
    }
    kmNetWorkFlag();
});
