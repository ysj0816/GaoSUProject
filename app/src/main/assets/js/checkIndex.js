//

var titileBox, GYbtn, JYbtn, checkBox, moreBtn, CheckDate, DeptName, DeptCode, userid,btback,imgpath,isadmin,ishow ,showusertype= null;
var curType = 1; // 1 为公益  2为经营
var showType = 1; // 1 为公益  2为经营  1为显示   2为隐藏按钮
PageInit();

//模拟页面初始化
function PageInit() {

	userid = jsresult.getUserid();
    imgpath=jsresult.backPath();
    isadmin=jsresult.isadmin();
    ishow=jsresult.isshow();
    showusertype=jsresult.showusertype();
	//界面初始化  找到所有元素控件
	domReady();
	 getPagePara();
	//绑定点击事件  主要是页面切换
	btnAction();
	//公益项目数据初始化
	//makeGYDomShow();
}

//dom  ready
function domReady() {
	titleBox = document.getElementById('title'); //标题
	GYbtn = document.getElementById('showGY'); //公益按钮
	JYbtn = document.getElementById('showJY'); //经营按钮
	checkBox = document.getElementById('checkWrap'); //检查列表和内容的盒子
	moreBtn = document.getElementById('hisList'); //跳转到列表页面
	btback=document.getElementById('btback');//返回上一个界面
	if(isadmin==1){

	}else if(isadmin==2){
	    //moreBtn.hide();
	    moreBtn.style.visibility="hidden";
	}
}

//绑定按钮事件
function btnAction() {
	mainCheckAction();
}

//获取页面的参数值
function getPagePara() {
    tempCurType = jsresult.ShowcurType(); //1还是2  1 为公益  2为经营
    tempShowType =jsresult.ShowType(); //1还是2  1为显示   2为隐藏按钮
    tmpCheckId =jsresult.ShowcheckId(); //检查id
    DeptCode = jsresult.ShowdeptCode(); //服务区id  经营项目中需要用到
    //假设都存在
    if (tempShowType && tempCurType) {
        curType = tempCurType;
        showType = tempShowType;
        CheckDate = jsresult.ShowcheckDate(); //日期
        DeptName = jsresult.ShowdeptName(); //服务区
        //此处主要想共享一个页面处理
        if (showType == 2) {
            GYbtn.parentNode.style.display = 'none';
            var curHTML = titleBox.innerHTML;
            titleBox.innerHTML = CheckDate + '-' + curHTML + '-' + DeptName;
        }
    }

    //做一个判断
    if (curType == 1) {
        if (tmpCheckId) {
            makeGYDomShow(tmpCheckId);
        } else {
            makeGYDomShow();
        }
    } else {
        var curClass = GYbtn.className;
        GYbtn.className = curClass.replace(' mui-active', '');
        JYbtn.className += ' mui-active';
        checkBox.innerHTML = '<div id="kmLoading" class="kmLoading"></div>';
        if (tmpCheckId && DeptCode) {
            makeJYDomShow(tmpCheckId, DeptCode);
        } else {
            makeJYDomShow();
        }
    }

}

//公益和经营项目 按钮点击后的跳转
function mainCheckAction() {

	//公益项目
	GYbtn.addEventListener('tap', function() {
		var pClass = this.className;
		var cur = this;
		if(pClass.indexOf("mui-active") <= -1) {
			curType = 1;
			var curClass = JYbtn.className;
			JYbtn.className = curClass.replace(' mui-active', '');
			cur.className += ' mui-active';
			checkBox.innerHTML = '<div id="kmLoading" class="kmLoading"></div>';
			makeGYDomShow();
		}
	});

	//经营项目
	JYbtn.addEventListener('tap', function() {
		var pClass = this.className;
		var cur = this;
		if(pClass.indexOf("mui-active") <= -1) {
			curType = 2;
			var curClass = GYbtn.className;
			GYbtn.className = curClass.replace(' mui-active', '');
			cur.className += ' mui-active';
			checkBox.innerHTML = '<div id="kmLoading" class="kmLoading"></div>';
			makeJYDomShow();
		}
	});

    //返回上一个界面
    btback.addEventListener('tap', function() {
            //先把htmL为空
           checkBox.innerHTML = '';
           jsresult.onbackup();
    	})
	//列表
	moreBtn.addEventListener('tap', function() {
           jsresult.JumpActivity();
	})
}

//处理公益项目从获得数据到渲染到页面
function makeGYDomShow(id) {
	var ProjectId, UnitArr, ProjectArr = null;

	//流程  -- 获取最新的检查id  --获取检查单位 --获取检查项目 --清理数据 --生成dom
	getRecentId().then(function(optionsA) {
		//如果存在传递过来的检查id
		if(id) {
			ProjectId = id; //检查id
		} else {
			ProjectId = optionsA.data.CheckId; //检查id
		}

		console.log('GY - ProjectId - ' + ProjectId);
		console.log(JSON.stringify(optionsA));

		getNetDataGYUnit().then(function(optionsB) {
			UnitArr = optionsB.data; //检查单位数组
			jsresult.LogStr("数组："+JSON.stringify(UnitArr));

			getNetDataGYProject().then(function(optionsC) {
				ProjectArr = optionsC.data; //检查项目数组
				var len = ProjectArr.length;
				if(len > 0) {
					checkBox.setAttribute('data-cid', ProjectId); //项目的检查id
					//下面开始进行数据清理
					var tmpArr = tidyDataGY(UnitArr, ProjectArr);
					var html = createGY(tmpArr);
					checkBox.innerHTML = html;
					console.log(html);
					console.log("ProjectId:" + ProjectId)
					//填充检查数据
					getCheckDetails(ProjectId).then(function(optionsC) {
						var tmpCheckDetails = optionsC.data;
						var curLen = tmpCheckDetails.length;
						if(curLen > 0) {
							createCheckDetails(tmpCheckDetails);
						}
					}, function(err) {
						console.log(JSON.stringify(err));
					});
				} else {
					plus.nativeUI.toast('当前没有添加公益信息');
					checkBox.innerHTML = '';
				}
			}, function(err) {
				console.log(JSON.stringify(err));
			});
		}, function(err) {
			console.log(JSON.stringify(err));
		});

	}, function(err) {
		console.log(err);
	});
}

//处理经营项目
function makeJYDomShow(id, code) {
	var ProjectId, DeptCode, UnitArr = null;

	//流程  -- 获取最新的检查id  --获取检查单位 --获取检查项目 --清理数据 --生成dom
	getRecentId().then(function(optionsA) {
		//如果存在传递过来的检查id
		if(id && code) {
			ProjectId = id; //检查id
			DeptCode = code; //服务区id
		} else {
			ProjectId = optionsA.data.CheckId; //检查id
            DeptCode = optionsA.data.DeptCode; //服务区id
		}

		getNetDataJYUnit(DeptCode).then(function(optionsB) {
			UnitArr = optionsB.rows; //检查单位数组
			var len = UnitArr.length;
			console.log("长度：" + len);
			if(len > 0) {
				checkBox.setAttribute('data-cid', ProjectId); //项目的检查id
				//下面开始进行数据清理
				var tmpArr = tidyDataJY(UnitArr);
				var html = createJY(tmpArr);
				checkBox.innerHTML = html;
				console.log(html);

				//填充检查数据
				getCheckDetails(ProjectId).then(function(optionsC) {
					var tmpCheckDetails = optionsC.data;
					var curLen = tmpCheckDetails.length;
					if(curLen > 0) {
						createCheckDetails(tmpCheckDetails);
					}
				}, function(err) {
					console.log(JSON.stringify(err));
				});
			} else {
				plus.nativeUI.toast('当前没有添加经营信息');
				checkBox.innerHTML = '';
			}
		}, function(err) {
			console.log(JSON.stringify(err));
		});

	}, function(err) {
		console.log(JSON.stringify(err));
	});
}

//获取最近一次checkId 和 DeptCode
function getRecentId() {
	//ajax  参数
	var para = {
		url: imgpath+ apiUrlList['getRecentCheckId'],
		data: {
			UserId: userid,
			CheckType: curType,
		}
	}

	console.log('检查的type' + curType);
	console.log('检查的type' + JSON.stringify(para));

	//一个简单的ajax流程
	var promiseId = Unit.kmAjax({
		server: para.url,
		dataType: 'json',
		type: 'POST',
		timeout: 5000,
		data: para.data
	});

	return promiseId;
}

//从网络获取公益的检查单位
function getNetDataGYUnit() {
	var para = {
		url: imgpath+  apiUrlList['getGYUnit'],
		data: {}
	}

	//一个简单的ajax流程
	var promiseGY = Unit.kmAjax({
		server: para.url,
		dataType: 'json',
		type: 'POST',
		timeout: 5000,
		data: para.data
	});

	return promiseGY;
}

//从网络获取公益的检查项目
function getNetDataGYProject() {
	var para = {
		url: imgpath+  apiUrlList['getGYProject'],
		data: {}
	}

	//一个简单的ajax流程
	var promiseGYC = Unit.kmAjax({
		server: para.url,
		dataType: 'json',
		type: 'POST',
		timeout: 5000,
		data: para.data
	});

	return promiseGYC;
}

//从网络获取经营的检查单位
function getNetDataJYUnit(DeptCode) {
	var para = {
		url: imgpath+  apiUrlList['getJYUnit'],
		data: {
			DeptId: DeptCode //服务区id
		}
	}

	//一个简单的ajax流程
	var promiseJY = Unit.kmAjax({
		server: para.url,
		dataType: 'json',
		type: 'POST',
		timeout: 5000,
		data: para.data
	});

	return promiseJY;
}

//整理公益的检查单位
function tidyDataGY(arrUnit, arrPro) {
	var alllist = [];
	//遍历得到数组
	arrUnit.forEach(function(value, index) {
		var tmparr = [];
		var pName = value.checkproject; //父元素名字
		var pid = value.oid; //父元素id
		var pIndex = (index + 1) + '.'; //父元素索引值
		//压入第一个
		tmparr.push({
			'OID': pid,
			'NAME': pName,
			'INDEX': pIndex
		});
		var fIndex = 0; //默认从0开始
		arrPro.forEach(function(val, innum) {
			var cName = val.subproject; //子元素的中文名称
			var cid = val.projectid; //子元素中父元素id
			var cIndex = pIndex + (fIndex + 1); //子元素的索引值
			var oid = val.oid; //子元素id
			//如果匹配到子元素
			if(cid == pid) {
				tmparr.push({
					'OID': oid,
					'NAME': cName,
					'INDEX': cIndex
				});
				fIndex++;
			}
		});
		alllist.push(tmparr);
	});

	return alllist;
}

//整理经营的检查单位
function tidyDataJY(arrUnit) {
	var alllist = [];
	//遍历得到数组
	arrUnit.forEach(function(value, index) {
		var pName = value.unitname; //元素名字
		var pid = value.id; //元素id
		var pIndex = (index + 1) + '.'; //父元素索引值
		//压入第一个
		alllist.push({
			'OID': pid,
			'NAME': pName,
			'INDEX': pIndex
		});
	});

	return alllist;
}

// 创建  公益项目的dom树
function createGY(arr) {

	var html = '';
	arr.forEach(function(value, index) {
		html += '<ul data-unitid="' + value[0].OID + '" class="mui-table-view" data-name="' + value[0].NAME + '" >';
		value.forEach(function(val, innum) {
			//注意区分 第一个和子项
			if(innum == 0) {
				html += '<li data-unitid="' + val.OID + '" class="mui-table-view-cell">';
				html += '<h4>' + val.INDEX + val.NAME + '</h4>';
				html += '</li>';
			} else {
				html += '<li onclick="toAddCheckData(this);" data-projectid="' + val.OID + '" class="mui-table-view-cell clickadd" data-name="' + val.NAME + '" >';
				html += '<span>' + val.INDEX + val.NAME + '</span>';
				html += '<span style="float: right;">+</span>';
				html += '</li>';
			}
		});
		html += '</ul>';
	});
	return html;
}

//创建经营项目的 dom树
function createJY(arr) {
	var html = '';
	arr.forEach(function(value, index) {
		html += '<ul data-unitid="' + value.OID + '" class="mui-table-view" data-name="' + value.NAME + '" >';
		html += '<li data-unitid="' + value.OID + '" class="mui-table-view-cell" >';
		html += '<h4>' + value.INDEX + value.NAME + '</h4>';
		html += '</li>';

		html += '<li onclick="toAddCheckData(this);" data-projectid="1" class="mui-table-view-cell  clickadd" data-name="经营管理"><span>' + value.INDEX + '1 经营管理</span><span style="float:right;" class="kmStyle-0">+</span ></li>';
		html += '<li onclick="toAddCheckData(this);" data-projectid="2" class="mui-table-view-cell  clickadd" data-name="环境卫生"><span>' + value.INDEX + '2 环境卫生</span><span style="float:right;" class="kmStyle-0">+</span ></li>';
		html += '<li onclick="toAddCheckData(this);" data-projectid="3" class="mui-table-view-cell clickadd" data-name="安全管理"><span>' + value.INDEX + '3 安全管理</span><span style="float:right;" class="kmStyle-0">+</span ></li>';
		html += '<li onclick="toAddCheckData(this);" data-projectid="4" class="mui-table-view-cell clickadd" data-name="文明服务"><span>' + value.INDEX + '4 文明服务</span><span style="float:right;">+</span ></li>';

		html += '</ul>';
	});
	return html;
}

//检查项目的跳转
function toAddCheckData(that) {

	console.log('检查日期' + CheckDate);
	if(CheckDate) {
		var curTime = Unit.kmGetTime({
			style: 'YYYY-MM-DD'
		});
		if(CheckDate.indexOf(curTime) <= -1) {
			mui.toast('超过检查日期！');
			return;
		}else{
		    if(showusertype==2||showusertype==3){

//		        mui.toast("不能上报");
		    }else{
		        mui.toast("用户权限不够");
		        return;
		    }
		}
	}
    //页面传递的参数
     var options = {
          curType: curType, //当前是公益还是经营
          showType: showType, //当前是否显示了公益和检查按钮
          checkId: checkBox.getAttribute('data-cid'), //检查id
          checkUnitName: that.parentNode.getAttribute('data-name'), //检查单位名称
          checkUnitId: that.parentNode.getAttribute('data-unitid'), //检查单位id
          checkProjectName: that.getAttribute('data-name'), //检查项目中文名称
          checkProjectId: that.getAttribute('data-projectid'), //检查项目id
          DeptCode: DeptCode, //服务区id
          CheckDate: CheckDate, //检查日期
          DeptName: DeptName //服务区名称
     }
          jsresult.getCheckMessage(options.curType, options.showType, options.checkId, options.checkUnitName
          , options.checkUnitId, options.checkProjectName, options.checkProjectId
          , options.DeptCode, options.CheckDate, options.DeptName);

	console.log('传递给页面的参数' + JSON.stringify(options));
}

//获取检查数据
function getCheckDetails(id) {
	var checkInfo = Unit.kmAjax({
		server: imgpath+ apiUrlList['getCheckInfo'],
		dataType: 'json',
		type: 'POST',
		timeout: '5000',
		data: {
			CheckId: id
		},
		success: function(data) {
			console.log('依据检查id获取的日常检查数据' + JSON.stringify(data));
		}
	});
	return checkInfo;
}

//js 属性选择器
function getAttrDom(options) {
	var list = options.dom.getElementsByTagName(options.son);
	var len = list.length;
	if(len > 0) {
		for(var i = 0; i < len; i++) {
			var tempAttr = list[i].getAttribute(options.attr);
			if(tempAttr == options.val) {
				return list[i];
			}
		}
	}
}

//创建检查详情数据
function createCheckDetails(data) {
	var html;
	for(var item in data) {
		html = '';
		html += '<div  class="mui-table"><div class="mui-table-cell mui-col-xs-6">';
		html += '<h5>检查结果：<span>' + data[item].checkresult + '</span></h5></div>';

		html += '<div class="mui-table-cell mui-col-xs-6 "><h5>扣分：<span >' + data[item].deductpoint + '</span></h5></div></div>   ';
		html += '<h5>具体情况：<span>' + data[item].content + '</span></h5>';
		if(data[item].imageinfor != "" && data[item].imageinfor != null) {
			var imgurl = data[item].imageinfor; //转化为json字符串
			html += '<a onclick="showPicBox(this,\'' + imgurl + '\');" style="color: #2E8B57;font-size: 14px;padding-top: 12px; padding-bottom: 12px;" class="main-wrap">点击查看图片</a>';
		}

		//查找父容器dom
		var settings = {
			dom: document.body,
			son: 'ul',
			attr: 'data-unitid',
			val: data[item].checkunit
		};

		var ulParent = getAttrDom(settings);

		console.log('这是检查单位dom' + ulParent);

		//假设存在
		if(ulParent) {
			//查找子容器dom
			var childSettings = {
				dom: ulParent,
				son: 'li',
				attr: 'data-projectid',
				val: data[item].checkproject
			};

			var liParent = getAttrDom(childSettings);

			console.log('这是检查项目dom' + liParent);

			//假设存在
			if(liParent) {
				var curHTML = liParent.innerHTML; //先得到目前已有的html
				liParent.innerHTML = curHTML + html;
				console.log('当前html' + curHTML);
				console.log('添加后的html' + curHTML + html);
				var spanDom = liParent.getElementsByTagName('span');
				spanDom[1].innerHTML = '√';
				spanDom[1].style.color = "green";
			}
		}
	}
}

//显示检查图片dom
function showPicBox(that, path, e) {
	e = e || window.event;
	e.stopPropagation(); //阻止冒泡
	e.preventDefault(); //阻止默认事件

	var arr = path.split(','); //将图片路径转化为json对象
	var parent = that.parentNode; //找到父节点
	parent.removeChild(that); //移除点击查看

	arr.forEach(function(value, index) {
		var img = document.createElement('img'); //图片对象
		img.src = imgpath+value;
		img.className = 'km-pic-wrap';
		parent.appendChild(img);
		//图片点击事件
		img.onclick = function(e) {
			var imgcur = this;
			e.stopPropagation();
			createPicBoxView(imgcur);
		};
		//图片加载成功后
		img.onload = function() {
			img.src = imgpath+value;
		};
		//图片加载失败
		img.onerror = function() {
			parent.removeChild(img);
		};
	});
}