//访问接口
var apiUrlList = {
	'ip': 'http://192.168.9.155', //IP地址
	'port': ':8888', //端口

	/*'ip': 'http://59.173.239.197', //IP地址
	'port': ':10801', //端口*/

	'userLogin': '/Account/UserLogin', //用户登录
	'userInfo': '/Sys_Department/GetDeptInfoByUserId', //获取登录用户的信息

	'getPersonOnDuty': '/Eve_OnDutyLog/GetDutyLogDetailByUserId', //获得个人考勤记录
	'getNoOnDuty': '/Eve_OnDutyLog/GetNoOnDutyLogList', //获得没有打考勤的
	'getAllOnDuty': '/Eve_OnDutyLog/GetOnDutyLogList', //获得所有考勤记录
	'delPeronOnDuty': '/Eve_OnDutyLog/DeleteDutyLog', //删除当日考勤记录
	'getDefinedDuty': '/Eve_OnDutyLog/GetOnDutyLogByDate', //获取指定日期的考勤记录
	'hasDayDuty': '/Eve_OnDutyLog/GetUserOnDutyLogByDateLe', //用户是否有上班考勤
	'makeDuty': '/Eve_OnDutyLog/CreateOnDutyLog', //上班打卡
	'hasPersonOnDuty': '/Eve_OnDutyLog/GetUserOnDutyLogByDate', //检查用户是否已打指定类型的考勤


//  'getRecentCheckId': '/SEA_CheckInforMain/GetRecentlyChenckInfor', //获得最近一次的Checkid和Deptcode
	'getRecentCheckId': '/GS/a/mobile/check/GetRecentlyChenckInfor', //  CY 获得最近一次的Checkid和Deptcode
//	'getDeptInfo': '/Sys_Department/GetDepartmentByUser', //获取服务区名称和信息
	'getDeptInfo': '/GS/a/mobile/check/GetDepartmentByUser', //CY获取服务区名称和信息
//	'addCheckList': '/SEA_CheckInforMain/AddCheckInforMain', //添加检查列表
	'addCheckList': '/GS/a/mobile/check/AddCheckInforMain', //CY添加检查列表
//  'getGYUnit': '/SEA_CheckInfor/GetCheckProject', //获取公益项目的检查单位
	'getGYUnit': '/GS/a/mobile/check/GetCheckProject', //获取公益项目的检查单位  CY
//	'getGYProject': '/SEA_CheckInfor/GetAllSubProject', //获取公益项目的检查项目
     'getGYProject': '/GS/a/mobile/check/GetAllSubProject', // 获取公益项目的检查项目 CY
//	'getJYUnit': '/SEA_BusinessUnit/GetBusinessUnitBySea', //获取经营项目的检查单位
	'getJYUnit': '/GS/a/mobile/check/GetBusinessUnitBySea', //获取经营项目的检查单位 CY
//'getCheckInfo': '/SEA_CheckInfor/GetCheckInfor', //获取检查列表中的检查信息
     'getCheckInfo': '/GS/a/mobile/check/GetCheckInfor', //获取检查列表中的检查信息 CY
//	'getCheckList': '/SEA_CheckInforMain/GetCheckInforMain', //获取指定月份的检查列表
	'getQrCode': '/GS/a/mobile/check/ErWeiMa?', //二维码扫描获取指定检查项目
	'getCheckList': '/GS/a/mobile/check/GetCheckInforMain', //CY//获取指定月份的检查列表
	'addCheck': '/SEA_CheckInfor/AddCheckInfor', //上传检查数据

	'delDaily': '/WorkDiary/DeleteDairy', //删除日志
	'makeDaily': '/WorkDiary/MobileAddWorkDiary', //上传日志
	'getDaily': '/WorkDiary/MobileGetWorkDairy', //获得日志
	'updateDaily': '/WorkDiary/UpdateDairy', //更新日志

	'getPhone': '/Sys_User/GetDeptContracts', //获取通讯录

	'getScanCheckId': '/SEA_CheckInforMain/BarcodeScanAddCheck', //二维码扫描查找检查id

	'getDepePosition': '/Sys_Department/GetDept', //获取服务区的坐标信息

	'addImmer': '/Sys_Information/EditInformation', //添加应急消息
	'getImmer': '/Sys_Information/GetInforList', //获取应急消息列表
	'getOneImmer': '/Sys_Information/GetInfor', //获取某一条应急消息

}

//app基础信息
var appInfo = {
	'name': '服务区信息管理平台',
	'quit': '您确定要退出应用吗？',
	'userNull': '请输入用户名',
	'passNull': '请输入密码',
	'btnOk': '确认',
	'btnEsc': '取消',
	'load': '正在为您卖力加载中，请稍后！',
	'delDuty': '您确定要删除考勤吗？',
	'pluginSina': 'http://php.weather.sina.com.cn/js.php?',
	'pingUrl': 'http://int.dpool.sina.com.cn/iplookup/iplookup.php',
	'loadico': './images/load.png',
	'location': '定位中，请稍后！',
	'locgps': '请查看是否已开启定位模块或授予APP定位权限！',
	'locerror': '抱歉，定位失败！',
	'netset': '网络开小差，请检查网络环境',
	'restart': '应用运行异常，请选择重启！',
	'datePicker': '请选择日期',
	'delDaily': '确定删除日志？',
	'updateDaily': '确定修改日志？',
	'loadFailed': '网络开小差，页面加载失败！', //页面未完全加载
	'help': '服务器异常，请联系管理员' //假设服务器出了问题 先做一个ping
}