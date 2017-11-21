//封装了一些常用的操作
var Unit = {
	//带动画打开页面 id为obj 或者 id
	openPage: function(url, id, callback) {
		//设置背景色透明可以去掉加载时候的白色空白
		var styles = {
			'background': 'transparent',
			'popGesture': 'none'
		}; //关闭了ios上的右滑关闭功能
		callback = callback != undefined ? callback() : '';
		plus.webview.open(url, id, styles, "slide-in-right", 500, callback);
	},
	//包含加载完成和百分比
	openPageOther: function(options) {
		var that = this;
		var styles = {
			'background': 'transparent',
			'popGesture': 'none'
		}; //关闭了ios上的右滑关闭功能

		var wt = plus.webview.create(options.url, options.id, styles, {});

		//需要引入外部网络资源的文件  假设网络条件不好时  就会加载失败  此时即便打开了页面  但是相关的操作 不可用
		wt.addEventListener('loaded', function() {
			console.log(wt.id + '加载完成了!');
			plus.webview.show(wt, 'slide-in-right', 500, function() {}, {});
		});

		wt.addEventListener('loading', function() {
			console.log('加载中，请稍后！');
		});

		wt.addEventListener('rendered', function() {
			console.log(wt.id + '渲染完成了!');
		}, false);

		wt.addEventListener('rendering', function() {
			console.log(wt.id + '开始渲染了!');
		}, false);

		//加载进度
		wt.addEventListener('progressChanged', function(e) {

			// e.progress可获取加载进度
			console.log('Progress Changed: ' + e.progress + '%');
			console.log('Progress Changed: ' + JSON.stringify(e));

		}, false);
	},
	//打开前先判断是否存在这个页面对象，假设存在先关闭，防止有缓存，然后再打开
	openPageNew: function(options) {
		var that = this;
		//通过id 查找该webview是否存在
		var wt = plus.webview.getWebviewById(options.id);
		//假设存在先关闭该webview
		if(wt) {
			that.closePageQuit(options.id);
		}
		//否则直接创建
		that.openPage(options.url, options.id);
	},
	//带动画关闭页面 id为obj 或者 id
	closePage: function(id) {
		plus.webview.close(id, "slide-out-right", 500, '');
	},
	//不带动画关闭页面  id为obj 或者 id
	closePageQuit: function(id) {
		//安卓和ios有些区别
		if(mui.os.ios) {
			plus.webview.close(id);
			console.log('ios的进来关闭页面呢！');
		} else {
			plus.webview.close(id, "none", 500, '');
		}
	},
	//创建带参数的页面
	openPageExtra: function(options) {
		var styles = {
			'background': 'transparent',
			'popGesture': 'none'
		}; //关闭了ios上的右滑关闭功能

		//如果有这个先执行关闭操作
		if(options.pid) {
			console.log('这是要关闭的父元素' + options.pid);
			//关掉父级页面，然后再重新打开
			this.closePageQuit(options.pid);
		}

		var wt = plus.webview.create(options.url, options.id, styles, options.extras);
		console.log('参数值' + options.extras);

		wt.addEventListener('loaded', function() {
			console.log(wt.id + '加载完成了!');
		});

		wt.addEventListener('rendered', function() {
			console.log(wt.id + '渲染完成了!');
			plus.webview.show(wt, 'slide-in-right', 500, function() {}, {});
		}, false);
	},
	//ajax请求 - 包含流程控制 -可并行，可串行
	kmAjax: function(options) {
		var that = this;
		return new Promise(function(resolve, reject) {
			$.ajax({
				type: "POST",
				dataType: 'jsonp',
				jsonp: 'callback',
				jsonpCallback: 'callback',
				url: options.server,
				data:options.data,
				timeout: options.timeout || 3000,
				success: function(data) {
				if (options.success) {
                                        options.success(data);
                                    }
					if(resolve) {
						console.log('有返回' + JSON.stringify(data));
						resolve(data);
					}
				},
				error: function(XMLHttpRequest, textStatus, errorThrown) {
//					var err = JSON.stringify(data);
//					console.log("err:"+data)
//					mui.alert('error' + JSON.stringify(XMLHttpRequest));
					//document.getElementById('checkWrap').innerText = err;
					if(reject) {
						// 如果说type=timeout  判定为网络状态欠佳
						reject(XMLHttpRequest);
					}
				}
				/*error: function(XMLHttpRequest, textStatus, errorThrown) {
						console.log("jsonp.error:" + textStatus);
						//var b=window.jsback.GetDate("失败");
				}*/

			});
			//          mui.ajax(options.server, {
			//              data: options.data,
			//              dataType: options.dataType || 'json', //服务器返回json格式数据
			//              type: options.type || 'POST', //HTTP请求类型
			//              timeout: options.timeout || 10000, //超时时间设置为10秒
			//              //发送数据前
			//              beforeSend: function() {
			//                  if (options.load) {
			//                      that.kmWaitShow(options.basePath);
			//                  }
			//              },
			//              //发送数据后
			//              complete: function() {
			//                  if (options.load) {
			//                      that.kmWaitClose();
			//                  }
			//              },
			//              //成功取得数据
			//              success: function(data) {
			//                  if (options.success) {
			//                      options.success(data);
			//                  }
			//                  if (resolve) {
			//                      console.log('有返回' + JSON.stringify(data));
			//                      resolve(data);
			//                  }
			//              },
			//              //请求过程中出了问题
			//              error: function(xhr, type, errorThrown) {
			//
			//                  //网络开小差提示
			//                  that.kmLowWorkTips({
			//                      err: xhr,
			//                      code: type,
			//                      tips: errorThrown
			//                  });
			//
			//                  if (options.error) {
			//                      options.error(xhr, type, errorThrown)
			//                  }
			//
			//                  if (reject) {
			//                      // 如果说type=timeout  判定为网络状态欠佳
			//                      reject(xhr, type, errorThrown);
			//                  }
			//              }
			//          });
		});
	},
	//自定义上传upload
	kmUploader: function(options) {
		var that = this;
		var domBg = options.loadDom.domBg;
		var domBox = options.loadDom.domBox;
		var domSon = options.loadDom.domSon;
		return new Promise(function(resolve, reject) {
			var task = plus.uploader.createUpload(options.server, {
					method: "POST",
					timeout: options.timeout || 500
				},
				function(t, status) { //上传完成
					console.log('这是status' + status);
					if(status == 200 && t.state == 4) {
						domBg.style.display = 'none';
						domBox.style.display = 'none';
						resolve(t);
					} else {
						reject(t);
					}
				});

			var paraData = options.data; //要传递的参数

			//添加参数值
			for(var i in paraData) {
				task.addData(i, paraData[i]);
			}

			//添加file文件
			var fileslist = options.files;
			var len = fileslist.length;
			console.log(len);
			if(len > 0) {
				//遍历输出
				fileslist.forEach(function(value) {
					task.addFile(value.path, {
						key: value.name
					});
				});
			}

			//添加状态变化监听
			task.addEventListener("statechanged", function(upload, status) {
				console.log('上传的状态' + JSON.stringify(upload));
				if(upload.state == 3) {
					var percent = parseInt((upload.uploadedSize / upload.totalSize) * 100);
					var curWidth = percent * 3 >= 300 ? 300 + 'px' : percent * 3 + 'px';

					domSon.innerText = percent + '%';
					domSon.style.width = curWidth;
				}
			}, false);
			task.start();

			domBg.style.display = 'block';
			domBox.style.display = 'block';
		});
	},
	//设置本地存储 -支持批量和单条操作
	kmSetStorage: function(options) {
		var len = options.length;
		if(len > 1) {
			options.forEach(function(val) {
				window.localStorage.setItem(val.key, val.value);
			});
		} else {
			window.localStorage.setItem(options.key, options.value);
		}
	},
	//获取本地存储 -支持批量和单条操作
	kmGetStorage: function(options) {
		var type = typeof(options) == 'object' ? 1 : 0; //是数组还是string
		if(type) {
			var tmparr = [];
			options.forEach(function(val) {
				console.log('获取本地缓存' + val);
				var value = window.localStorage.getItem(val);
				tmparr.push(value);
			});
			return tmparr;
		} else {
			return window.localStorage.getItem(options);
		}
	},
	//删除本地存储 -支持批量和单条操作
	kmDelStorage: function(options) {
		var type = typeof(options) == 'object' ? 1 : 0; //是数组还是string
		if(type) {
			var tmparr = [];
			options.forEach(function(val) {
				window.localStorage.removeItem(val);
			});
		} else {
			window.localStorage.removeItem(options);
		}
	},
	//输出格式化的时间 -时间自定义或者生成
	kmGetTime: function(options) {
		var time = options.time ? new Date(options.time) : new Date();
		var outPutTime = {
			"M+": time.getMonth() + 1, //月份
			"D+": time.getDate(), //日
			"h+": time.getHours(), //小时
			"m+": time.getMinutes(), //分
			"s+": time.getSeconds(), //秒
			"q+": Math.floor((time.getMonth() + 3) / 3), //季度
			"S": time.getMilliseconds() //毫秒
		};
		if(/(Y+)/.test(options.style)) {
			options.style = options.style.replace(RegExp.$1, (time.getFullYear() + "").substr(4 - RegExp.$1.length));
		}
		for(var k in outPutTime) {
			if(new RegExp("(" + k + ")").test(options.style)) {
				options.style = options.style.replace(RegExp.$1, (RegExp.$1.length == 1) ? (outPutTime[k]) : (("00" + outPutTime[k]).substr(("" + outPutTime[k]).length)));
			}
		}
		return options.style;
	},
	//自定义loading样式 加载
	kmWaitShow: function(basePath) {
		plus.nativeUI.showWaiting(appInfo['load'], {
			width: '240px',
			height: '120px',
			round: '4px',
			padding: '10px',
			loading: {
				display: 'block',
				height: '60px',
				icon: basePath + appInfo['loadico']
			}
		});
	},
	//自定义loading样式 关闭
	kmWaitClose: function() {
		plus.nativeUI.closeWaiting();
	},
	//自定义重启app
	kmReStart: function() {
		//直接提示重启
		plus.nativeUI.alert(appInfo['restart'], function() {
			plus.runtime.restart();
		}, appInfo['name'], appInfo['btnOk']);
	},
	//自定义提示层
	kmAlert: function(str) {
		if(str) {
			if(mui.os.android) {
				//android
				plus.nativeUI.alert(str, function() {
					var main = plus.android.runtimeMainActivity(); //获取activity
					var Intent = plus.android.importClass('android.content.Intent');
					var Settings = plus.android.importClass('android.provider.Settings');
					var intent = new Intent(Settings.ACTION_SETTINGS); //打开系统设置
					main.startActivity(intent);
				}, appInfo['name'], appInfo['btnOk']);
			} else {
				//ios
				plus.nativeUI.alert(str, function() {
					var UIApplication = plus.ios.import("UIApplication");
					var NSURL = plus.ios.import("NSURL");
					var setting = NSURL.URLWithString("app-settings:");
					var application = UIApplication.sharedApplication();
					application.openURL(setting);
					plus.ios.deleteObject(setting);
					plus.ios.deleteObject(application);
				}, appInfo['name'], appInfo['btnOk']);
			}
		} else {
			//直接提示重启
			plus.nativeUI.alert(appInfo['restart'], function() {
				plus.runtime.restart();
			}, appInfo['name'], appInfo['btnOk']);
		}
	},
	//自定义确定和取消按钮的函数
	kmDoAlertAction: function(options) {
		// 弹出提示信息对话框
		plus.nativeUI.confirm(options.tips, function(e) {
			if(e.index == 0) {
				options.callback(options.data);
			}
		}, appInfo['name'], [appInfo['btnOk'], appInfo['btnEsc']]);
	},
	//ajax ping
	kmPing: function(options) {
		mui.ajax(options.server, {
			dataType: 'html', //服务器返回json格式数据
			type: 'GET', //HTTP请求类型
			timeout: 5000, //超时时间设置为5秒
			//成功取得数据
			success: function() {
				plus.nativeUI.toast(appInfo['help']);
			},
			//请求过程中出了问题
			error: function(xhr, type, errorThrown) {
				plus.nativeUI.toast(appInfo['netset']);
			}
		});
	},
	//网络差给出提示
	kmLowWorkTips: function(options) {
		console.log('网络出故障了' + JSON.stringify(options.err) + '故障类型' + options.code + '错误' + options.tips);
		plus.nativeUI.toast(appInfo['netset']);
	}
}