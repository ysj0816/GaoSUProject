// 日志管理 包含日志查询  日志添加跳转 日志删除

var GYBtn, JYBtn, timeBox, timeBtn, colBox, addListBtn,userid,back,path ,plusishow= null;
var curType = 1;

window.addEventListener('load',function(){
	pageInit();
});

//模拟页面初始化
function pageInit() {
    userid=jsproject.getUserID();
    path=jsproject.backPath();
    plusishow=jsproject.plusishow();
    domReady();
    getCheckList();
}

//dom初始化
function domReady() {
    GYBtn = document.getElementById('showGY'); //上报管理员日志按钮
    JYBtn = document.getElementById('showJY'); //上报水电工日志按钮
    addListBtn = document.getElementById('addBtn'); //添加检查列表

    timeBtn = document.getElementById('demo6'); //日期选择器按钮
    timeBox = document.getElementById('result'); //日期选择器盒子

    colBox = document.getElementById('col'); //检查列表内容区
    back=document.getElementById('back');
    if(plusishow==1){
        addListBtn.style.visibility="hidden";
    }
}

//获取日志的相关操作
function getCheckList() {

    var settings = {
        GYBtn: GYBtn, //公益项目的按钮
        JYBtn: JYBtn, //经营项目的按钮
        timeBtn: timeBtn, //日期选择器按钮
        timeBox: timeBox, //日期选择器盒子
        colBox: colBox, //检查列表内容区
        addListBtn: addListBtn //添加列表按钮
    }

    var MakeCheckObject = new kmMakeCheck(settings);
    console.log(MakeCheckObject);
    console.log(MakeCheckObject.constructor);
    MakeCheckObject.Init();
    
}

//自定义类实现水电工和日志管理
function kmMakeCheck(options) {
    this.GYBtn = options.GYBtn; //公益项目的按钮
    this.JYBtn = options.JYBtn; //经营项目的按钮
    this.timeBtn = options.timeBtn; //日期选择器按钮
    this.timeBox = options.timeBox; //日期选择器盒子
    this.colBox = options.colBox; //日志内容区
    this.addListBtn = options.addListBtn; //添加列表按钮
}

kmMakeCheck.prototype = {
    Init: function() {
        this.btnAction();
    },
    //按钮绑定事件
    btnAction: function() {
        this.getPicker(); //日期选择器
        this.lookCheckList(); //查询月份的检查列表
        this.pageBtnAction(); //公益和经营项目的按钮
    },
    //公益项目和经营项目跳转跳转
    pageBtnAction: function() {
        var that = this;
        var GYBtn = that.GYBtn; //公益项目
        var JYBtn = that.JYBtn; //经营项目
        var addListBtn = that.addListBtn; //添加检查列表

        //公益项目
        GYBtn.addEventListener('tap', function() {
            colBox.innerHTML = '<div id="kmLoading" class="kmLoading"></div>';
            curType = 1;
            selectCheckList();
        });

        //经营项目
        JYBtn.addEventListener('tap', function() {
            colBox.innerHTML = '<div id="kmLoading" class="kmLoading"></div>';
            curType = 2;
            selectCheckList();
        });

        //添加检查列表
        addListBtn.addEventListener('tap', function() {
            jsproject.Jump();
        });
        //返回上一层
        back.addEventListener('tap', function() {
            jsproject.onbackup();
        });
    },
    //日期选择
    getPicker: function() {
        timeBox.innerText = Unit.kmGetTime({ style: 'YYYY-MM' });
        var that = this;
        //日历配置项
        var options = {
            btn: that.timeBtn, //触发选择器的按钮
            box: that.timeBox, //选择后显示日期的盒子
            type: 1, //没有type值就显示完整的 有type值显示年月
            callback: selectCheckList //回掉函数 
        }
        var pickerObj = new datePicker(options);
        pickerObj.Init();
    },
    //查询当月的检查列表
    lookCheckList: function() {
        selectCheckList();
    }
}

//查询检查列表记录
function selectCheckList() {
    ajaxGetCheckList().then(function(data) {
        console.log('这是获取到的检查列表数据' + JSON.stringify(data));
        //处理返回的数据
        var len = data.data.length;
        if (len > 0) {
            var html = createCheckDOM(data.data);
            colBox.innerHTML = html;
        } else {
            colBox.innerHTML="";
            mui.toast('当月没有检查列表记录！');
        }
    }, function(err) {
        console.log(JSON.stringify(err));
    });
}

//ajax 获取检查列表
function ajaxGetCheckList() {

    //获取需要查询的月份时间
    var baseTime = timeBox.innerText;
    var curTime = Unit.kmGetTime({ time: baseTime, style: 'YYYY-MM' });
    //ajax  参数
    var para = {
        url: path + apiUrlList['getCheckList'],
        data: {
            UserId: userid,
            Date: curTime, //指定的月份日期
            CheckType: curType //检查类型 1为公益  2为经营
        }
    }

    //一个简单的ajax流程
    var promiseCheckList = Unit.kmAjax({
        server: para.url,
        dataType: 'json',
        type: 'POST',
        timeout: 5000,
        data: para.data
    });

    return promiseCheckList;
}

//生成相应的dom树
function createCheckDOM(result) {
    var html = '';
    for (var item in result) {
        html += '<ul class="mui-table-view"><li class="mui-table-view-cell">';
        html += '<div class="mui-table"><div class="mui-table-cell mui-col-xs-8">';
        html += '<img class="mui-media-object mui-pull-left" src="../img/logo.png">';
        html += '<div class="mui-media-body"><strong>' + result[item].DeptName + ' </strong></div></div>';
        html += '<div class="mui-table-cell mui-col-xs-4 mui-text-right"><span class="mui-h5">检查人：<strong>' + result[item].CheckUser + '</strong></span></div></div>';
        html += '</li><li class="mui-table-view-cell"><div class="mui-table"><div class="mui-table-cell mui-col-xs-6"><h5>总扣分：<span style="color: #3c99ff;">' + result[item].TotalDeduct + '</span></h5></div>';
        html += '<div class="mui-table-cell mui-col-xs-6 mui-text-right"><p>' + result[item].CheckDate + '</p></div></div></li>';
        html += '<li class="mui-table-view-cell mui-media"><div class="mui-media-body mui-pull-right">';

        //传递的相关参数值
        var options = {
            CheckDate: result[item].CheckDate, //检查日期
            CheckId: result[item].CheckId, //检查id
            DeptName: result[item].DeptName, //服务区名称
            DeptCode: result[item].DeptCode //服务区id
        };

        var tmpstr = JSON.stringify(options);
        html += '<a data-mode=\'' + tmpstr + '\' onclick="pageRefer(this);"><button type="button" class=" mui-btn mui-btn-warning">详情查看</button></a></div></li></ul>';
    }
    return html;
}

//页面跳转
function pageRefer(that) {

    console.log('传递过来的列表信息' + that.getAttribute('data-mode'));
    var str = JSON.parse(that.getAttribute('data-mode'));

    var CheckDate = str.CheckDate; //检查日期
    var CheckId = str.CheckId; //检查id
    var DeptCode = str.DeptCode; //服务区id
    var DeptName = str.DeptName; //服务区名称
    var showType='2';
    jsproject.setDetailData(CheckDate,CheckId,DeptCode,DeptName,showType,curType);
}
