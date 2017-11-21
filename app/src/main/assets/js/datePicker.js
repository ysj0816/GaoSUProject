//日期选择器
function datePicker(options) {
    this.btn = options.btn; //触发的按钮
    this.box = options.box; //需要显示时间的盒子
    this.callback = options.callback; //选择时间后的回掉函数
    this.type = options.type; //注意区分只显示年月和年月日
}

datePicker.prototype = {
    Init: function() {
        var btn = this.btn;
        var optionsJson = btn.getAttribute('data-options') || '{}';
        var options = JSON.parse(optionsJson);
        var id = btn.getAttribute('id');
        var box = this.box;
        var callback = this.callback;
        var type = this.type;
        btn.addEventListener('touchstart', function() {
            var picker = new mui.DtPicker(options);
            picker.show(function(rs) {
                //日常检查是以月为单位
                if (type) {
                    box.innerText = rs.y.text + '-' + rs.m.text; //默认返回示例 2017-07
                } else {
                    box.innerText = rs.text; //默认返回示例  2017-07-05
                }
                picker.dispose();
                if (callback) {
                    callback(rs);
                }
            });
        })
    }
}
