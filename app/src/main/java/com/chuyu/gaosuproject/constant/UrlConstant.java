package com.chuyu.gaosuproject.constant;

import android.util.Log;

/**
 * Created by wo on 2017/7/9.
 */

public class UrlConstant {
    public static String HTTP="http://";
    public static String IP;//192.168.11.9

    public static String getIP() {
        return IP;
    }

    public static void setIP(String IP) {
        UrlConstant.IP = IP;
    }

    public static String getPORT() {
        return PORT;
    }

    public static void setPORT(String PORT) {
        UrlConstant.PORT = PORT;
    }

    //  公网  http://59.173.239.197:10808/GS  http://www.chuyutech.com:8088/GS/a/login  192.168.11.48
    public static String PORT;//8088
    //public static String PATH=HTTP+getIP()+":"+getPORT();

    //天气key
    public static String WEATHERkey="GuZriL3rkm1MUnyTyfsNGvTC";
    public static String WEATHERURL="http://api.map.baidu.com/telematics/v3/weather?";
    //组拼IP （关宏伟）
    public static String formatUrl(String url){
        return HTTP+getIP()+":"+getPORT()+url;
    }

    //登录
    public static String LOGINURL="/GS/a/mobile/login/login?";
    //判断是否重复签到
    public static String IsSIgn="/GS/a/mobile/Eve_OnDutyLog/GetUserOnDutyLogByDate?";
    //签到                         /GS/a/mobile/Eve_OnDutyLog/CreateOnDutyLog
    public static String SIGNURL="/GS/a/mobile/Eve_OnDutyLog/CreateOnDutyLog?";
    //请假
    public static String LEAVEURL="/GS/a/mobile/Eve_OnDutyLog/CreateOnDutyLog?";
    //添加管理员日志
    public static String AddmManagerLogUrL="/GS/a/mobile/WorkDiary/MobileAddWorkDiary?";
    //经营检查单位
    public static String ManagementCheckProjectCompanyURL="/GS/a/mobile/check/GetBusinessUnitBySea";
    //公益检查项目
    public static String PublicwelfareCheckprojectUrL="/GS/a/mobile/check/GetAllSubProject";
    //公益检查单位
    public static String PublicwelfareCheckprojectCompanyURL="/GS/a/mobile/check/GetCheckProject";
    //获取管理员日志
    public static String GetWorkDairyURL="/GS/a/mobile/WorkDiary/MobileGetWorkDairy?";
    //修改管理员日志
    public static String UpdateDairyURL="/GS/a/mobile/WorkDiary/UpdateDairy?";
    //个人考勤记录
    public static String PERSONSIGNURL="/GS/a/mobile/Eve_OnDutyLog/GetDutyLogDetailByUserId?";
    //所有人的考勤记录
    public static String ALLPRESONURL="/GS/a/mobile/Eve_OnDutyLog/GetOnDutyLogList?";
    //未打卡记录
    public static String NOSIGNURL="/GS/a/mobile/Eve_OnDutyLog/GetNoOnDutyLogList?";
    //当日考勤记录删除
    public static String DELETESIGNURL="/GS/a/mobile/Eve_OnDutyLog/DeleteDutyLog?";
    //个人当天考勤记录
    public static String PERSONDATEURL="/GS/a/mobile/Eve_OnDutyLog/GetOnDutyLogByDate?";
    //上传日常检查数据
    public static String updateCheckInforURL="/GS/a/mobile/check/AddCheckInfor";
    //判断用户是否为管理员
    public static String JudgeUserIsAdminURL="http://59.173.239.197:7066/Sys_Department/GetDeptInfoByUserId";
    //二维码扫描请求
    public static String QRECODEURL="/GS/a/mobile/check/ErWeiMa?";
    //wifi状态下提交的接口
    public static String WIFIIPLOAD="/GS/a/mobile/wifi/dataSubmit?";
}
