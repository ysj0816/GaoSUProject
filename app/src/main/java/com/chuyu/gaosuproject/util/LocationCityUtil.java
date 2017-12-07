package com.chuyu.gaosuproject.util;

import android.content.Context;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;

/**
 * 获取定位的工具类
 * Created by wo on 2017/7/12.
 */

public class LocationCityUtil {

    private static LocationCityUtil locationCityUtil;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;

    private LocationCityUtil(){}

    public static synchronized LocationCityUtil getInstance(){
        if (locationCityUtil==null){
            locationCityUtil=new LocationCityUtil();
        }
        return locationCityUtil;
    }

    /**
     * 获取当前位置
     *
     */
    public  void getNowLocaiton(AMapLocationClient mLocationClient,AMapLocationClientOption mLocationOption,LocationListener locationListener){

        this.locationListener=locationListener;
        this.mLocationClient=mLocationClient;
        //初始化定位
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener);
        //初始化AMapLocationClientOption对象
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，
        // 启动定位时SDK会返回最近3s内精度最高的一次定位结果。
        // 如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //设置是否强制刷新WIFI，默认为true，强制刷新。
        mLocationOption.setWifiScan(true);
        //单位是毫秒，默认30000毫秒，建议超时时间不要低于8000毫秒。
        mLocationOption.setHttpTimeOut(30000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();


    }
    //声明定位回调监听器
    public AMapLocationListener mLocationListener = new AMapLocationListener() {
        @Override
        public void onLocationChanged(AMapLocation amapLocation) {
            if (amapLocation != null) {
                if (amapLocation.getErrorCode() == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    Log.i("test", "启动定位");

                    locationListener.locationSuccess(amapLocation);
                } else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    Log.e("AmapError", "location Error, ErrCode:"
                            + amapLocation.getErrorCode() + ", errInfo:"
                            + amapLocation.getErrorInfo());
                    locationListener.locationFaile(amapLocation);
                }
            }
        }
    };
    /**
     * 刷新当前位置
     */
    public void reFreshLocation(){
        Log.i("test","刷新定位1");
        //刷新定位
        if (mLocationClient!=null){
            mLocationClient.startLocation();
            //
            Log.i("test","刷新定位2");
        }
    }

    /**
     * 结束定位
     */
    public void stopLcaction(){
        if (mLocationClient!=null){
            mLocationClient.stopLocation();//停止定位后，本地定位服务并不会被销毁

        }
    }

    /**
     * 结束当前服务
     */
    public void stopServer(){
        if (mLocationClient!=null){
            mLocationClient.onDestroy();//销毁定位客户端，同时销毁本地定位服务。
        }
    }

    /**
     * 接口回调
     * top1 定义一个接口
     * top2 接口私有化
     * top3 提供一个公开的接口访问方法
     */
    public interface LocationListener{
        void locationSuccess(AMapLocation amapLocation);
        void locationFaile(AMapLocation amapLocation);
    }
    private LocationListener locationListener;


}
