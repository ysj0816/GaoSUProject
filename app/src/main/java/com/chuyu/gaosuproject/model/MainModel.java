package com.chuyu.gaosuproject.model;

import android.text.TextUtils;
import android.util.Log;

import com.chuyu.gaosuproject.bean.weather.WeatherInfo;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.model.interfacemodel.IMainModel;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wo on 2017/7/7.
 */

public class MainModel implements IMainModel {

    public static MainModel model;

    private static Gson gson;

    private MainModel(){};

    public static MainModel getInstance(){
        if (model==null){
            model=new MainModel();
            gson=new Gson();
        }
        return model;

    }


    @Override
    public void getWeather(String city, String ouput, String ak, final GetWeatherDataListener<WeatherInfo> getWeatherDataListener) {
        if (TextUtils.isEmpty(city)){
            getWeatherDataListener.querayException("参数为空");
        }else {

            OkGo.get(UrlConstant.WEATHERURL+"location="+city+"&output="+ouput+"&ak="+ak+"&mcode="+"EE:0C:C8:50:54:53:96:5A:55:8C:23:2F:93:7E:EB:AE:D8:C8:1B:F1;com.example.tangdekun.androidannotationsdemo")
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            try{
                                WeatherInfo weatherInfo = gson.fromJson(s, WeatherInfo.class);
                                getWeatherDataListener.Success(weatherInfo);
                            }catch (Exception e){

                            }


                        }
                    });
        }
    }

    @Override
    public void getLocation() {

    }
}
