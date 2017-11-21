package com.chuyu.gaosuproject.view;

import com.chuyu.gaosuproject.base.BaseView;
import com.chuyu.gaosuproject.bean.weather.WeatherInfo;

/**
 * Created by wo on 2017/7/4.
 */

public interface IMainView extends BaseView{
    //进度条显示隐藏
    void showLoading();//进度条显示
    void hintLoading();//隐藏
    void showError(); //失败
    void setWeather(WeatherInfo weatherInfo);

}
