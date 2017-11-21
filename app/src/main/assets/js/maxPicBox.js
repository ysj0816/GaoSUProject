//原生native  实现图片点击放大盒子

function createPicBoxView(that) {

    var basePath = that.getAttribute('src'); //获取src
    //要绘制的图片属性值
    var options = [{
        tag: 'img',
        id: 'picWrapBox',
        src: basePath,
        position: {
            top: '0',
            left: '0',
            width: '100%',
            height: '100%'
        }
    }];
    //绘制的图片控件
    view = new plus.nativeObj.View('picBox', {
        top: '0',
        left: '0',
        height: '100%',
        width: '100%'
    });
    //执行绘制
    view.draw(options);
    //显示
    view.show();

    //触摸后关闭
    view.addEventListener('touchstart', function() {
        view.close();
        console.log('我被触摸关闭了！');
    }, false);
}
