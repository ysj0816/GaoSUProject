/*
    用于中心人员或者服务区人员  添加公益项目或者经营项目
 */

var timeBox, deptBox, userBox, manageBox, typeBox, mlistBtn, subBtn, CheckType, formBox, loadBox ,userid,back,username,path,showtype= null;

window.addEventListener('load',function(){
	pageInit();
});

//模拟页面初始化
function pageInit() {
    userid=jsproject.getUserID();
    username=jsproject.backUsername();
    path=jsproject.backpath();
    showtype=jsproject.showType();

	domReady();
	getDeptInfo();
	btnAction();
}

//dom赋值
function domReady() {
	timeBox = document.getElementById('sbtime'); //检查时间盒子
	deptBox = document.getElementById('ManagerArea'); //服务区盒子
	userBox = document.getElementById('checkuser'); //检查人盒子
	manageBox = document.getElementById('manageuser'); //管理员盒子
	typeBox = document.getElementById('Type'); //检查类型  1公益项目   2经营项目
	mlistBtn = document.getElementById('mlist'); //查看列表按钮
	subBtn = document.getElementById('addBtn'); //提交检查列表按钮

	formBox = document.getElementById('formBoxWrap'); //所有的盒子
	loadBox = document.getElementById('kmLoading'); //加载圈圈盒子
	back=document.getElementById('back');
	if(showtype==3){
	   mlistBtn.style.visibility="hidden";
	}else{
	   mlistBtn.style.visibility="visible";
	}
}

//获取服务区名称
function getDeptInfo() {
	var userInfo = Unit.kmAjax({
		server: path + apiUrlList['getDeptInfo'],
		data: {
		    UserId: userid
		},
		success: function(data) {
			setBaseInfo();
			setDeptInfo(data.data);
			formBox.style.display = 'block'; //有数据后再显示 不然不容许操作
			loadBox.style.display = 'none'; //隐藏加载圈圈
		}
	});
	return userInfo;
}

//设置服务区名称
function setDeptInfo(data) {
	var html = '';
	for(var i in data) {
		var value = data[i];
		if(showtype==2||showtype==3){
		  html += '<option value="' + value.id + '">' + value.name + '</option>';
		}else{
		  html="";
//		  $("#deptBox ").prop("disabled", false);
		}
//		html += '<option value="' + value.id + '">' + value.name + '</option>';
	}
	deptBox.innerHTML = html;
}

//处理部分不需要从网络获取的信息
function setBaseInfo() {
	timeBox.value = Unit.kmGetTime({
		style: 'YYYY-MM-DD hh:mm'
	}); //设置时间
	userBox.value = username; //设置检查人
}

//所有按钮事件
function btnAction() {
	//上报按钮
	subBtn.addEventListener('tap', function() {
		var cur = this;
		var curClass = cur.className;
		//通过设置cur 执行锁定操作
		if(curClass.indexOf('cur') <= -1) {
			cur.className = curClass + ' cur';
			if(showtype==2||showtype==3){
			    addCheckList();
			}else{
			    mui.toast('用户权限不够');
			}
//			addCheckList();
			window.setTimeout(function() {
				cur.className = curClass.replace(' cur', '');
			}, 2000)
		}
	});
    //返回上一层
    back.addEventListener('tap', function() {
            jsproject.onbackup();
     });
    mlistBtn.addEventListener('tap', function() {
            jsproject.StartActivity();
    });
}

//添加日常检查列表
function addCheckList() {
	var DeptCode = deptBox.options[deptBox.selectedIndex].value; //服务区id
	var DeptName = deptBox.options[deptBox.selectedIndex].text; //服务区名称
	CheckType = typeBox.options[typeBox.selectedIndex].value; //检查类别
	var ManageUser = manageBox.value; //检查人
       //如果这几项不不存在   禁止提交
       	if(DeptCode && DeptName && CheckType && ManageUser) {
       		var addInfo = Unit.kmAjax({
       			server: path+ apiUrlList['addCheckList'],
       			dataType: 'json',
       			type: 'POST',
       			timeout: '5000',
       			data: {
       				UserId: userid, //设置检查人
       				DeptCode: DeptCode, //服务区id
       				DeptName: DeptName, //服务区名称
       				CheckType: CheckType, //检查类别
       				CheckDate: timeBox.value, //检查日期
       				ManageUser: ManageUser //检查人
       			}
       		});
       		//ajax流程控制
       		addInfo.then(function(data) {
       			console.log(JSON.stringify(data));
       			mui.toast('添加检查列表成功');
       			var str = JSON.stringify(data.data);
       			referPage(data.data);
       		}, function(err, type) {
       			console.log('错误' + JSON.stringify(err))
       		});
       	} else {
       		mui.toast('请补全所有信息后再上报');
       	}


}

function referPage(data) {
	jsproject.showMessage(data.DeptCode,CheckType,"2",data.CheckDate,data.CheckId, data.DeptName);
	//跳转到最近一次的列表页面
	Unit.openPageExtra(pageInfo);
}

//跳转到列表页面
function referListPage() {
	Unit.openPageNew({
		url: 'check.html',
		id: 'check'
	});
}