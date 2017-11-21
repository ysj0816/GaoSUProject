
// 处理返回键

var old_back; //全局变量，方便调用

mui.plusReady(function() {
    old_back = mui.back;
    mui.back = function() {
        reWriteBack();
    }
});

//判断是否可以回退,重写回退方法
function reWriteBack() {
    //打开该页面的上一层页面是否存在
    var cdomp = plus.webview.currentWebview().opener();
    var cflag = cdomp ? true : false;
    if (cflag) {
        console.log('mui自带回退');
        old_back(); //执行mui.back()的处理逻辑
    } else {

        var startPage = plus.webview.getLaunchWebview(); //起始页面
        var main = plus.webview.getWebviewById('index'); //自己打开的首页 页面相同 但id不同
        var mflag = startPage ? startPage : main ? main : '';

        var curPage = plus.webview.currentWebview().id; //当前页面的id
        var comPage = mflag.id; //首页或者启动页面的id

        console.log('啥情况？到底存不存在index页面啊！' + mflag + '当前页面的id' + curPage);

        //已经登录了直接进入默认页面 --此处注意区分假设最后的页面就是启动页或者首页 这时候要做过滤处理
        if (mflag && curPage != comPage) {
            //假设首页index存在，则返回首页  关闭当前页面
            var curPage = plus.webview.currentWebview();
            Unit.closePage(curPage);
        } else {
            //因为ios不支持 因此此处只对安卓处理
            if (mui.os.android) {
                var btn = [appInfo.btnOk, appInfo.btnEsc];
                mui.confirm(appInfo.quit, appInfo.name, btn, function(e) {
                    if (e.index == 0) {
                        //ios不支持
                        plus.runtime.quit();
                    }
                });
            }
        }
    }
}
