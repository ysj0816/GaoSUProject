package com.chuyu.gaosuproject.presenter;

import android.util.Log;

import com.chuyu.gaosuproject.base.BasePresenter;
import com.chuyu.gaosuproject.bean.weather.WeatherInfo;
import com.chuyu.gaosuproject.model.MainModel;
import com.chuyu.gaosuproject.model.interfacemodel.IMainModel;
import com.chuyu.gaosuproject.view.IMainView;

/**
 * Created by wo on 2017/7/7.
 */

public class MainPresenter extends BasePresenter<IMainView> {
    /**
     * 获取天气
     */
    public void getWeather(String city, String ouput, String ak){
        if(isViewAttached()){

        }else {
            return;
        }
        final IMainView view = getView();
        view.showLoading();
        /**
         * 获取model中的天气
         */
        MainModel.getInstance().getWeather(city, ouput, ak, new IMainModel.GetWeatherDataListener<WeatherInfo>() {
            @Override
            public void Success(WeatherInfo weatherInfo) {
                view.setWeather(weatherInfo);
            }

            @Override
            public void Failed() {

            }

            @Override
            public void querayException(String e) {

            }
        }
    );
    }

}
