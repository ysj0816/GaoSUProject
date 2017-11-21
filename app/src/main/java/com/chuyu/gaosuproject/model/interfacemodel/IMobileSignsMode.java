package com.chuyu.gaosuproject.model.interfacemodel;

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;

import java.util.zip.Inflater;

/**
 * 打卡
 * Created by wo on 2017/7/11.
 */

public interface IMobileSignsMode {

    interface GetLocationListener<T>{
        void fixedSuccess(T t);
        void fixedFailed();
    }

    interface GetTimeListener<T>{
        void success(T t);
        void failed();
    }

    /**
     * 获取所有人的签到记录
     * @param userid
     * @param getAllPresonNodeListener
     */
    void getAllPresonNode(String userid,GetAllPresonNodeListener getAllPresonNodeListener);

    /**
     * 获取所有人的签到记录监听
     */
    interface GetAllPresonNodeListener{
        void getDateSuccess(String s);
        void getFaile();
        void shwoExpcetion(String msg);
    }

    //top 1  获取定位数据
    String getLocation(AMapLocationClient aMapLocationClient, AMapLocationClientOption aMapLocationClientOption, IMainModel.GetWeatherDataListener getWeatherDataListener);
    //top2 获取时间
    String getSignTime();
    //TODO 3 判断打卡流程是否正确（未打上班，直接下班、重复签到）

    //TODO 4 提交签到数据
}
