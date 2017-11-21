package com.chuyu.gaosuproject.model.interfacemodel;

import com.chuyu.gaosuproject.bean.weather.WeatherInfo;

/**
 * 主页
 * Created by wo on 2017/7/7.
 */

public interface IMainModel {

    interface GetWeatherDataListener<T>{
        void Success(T T);
        void Failed();
        void querayException(String e);
    }

    /**
     * 获取天气数据
     */
    void getWeather(String city,String ouput,String ak, GetWeatherDataListener<WeatherInfo> getWeatherDataListener);

    /**
     * 获取地理位置
     */
    void getLocation();
}
