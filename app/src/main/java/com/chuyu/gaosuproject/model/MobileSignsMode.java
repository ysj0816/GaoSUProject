package com.chuyu.gaosuproject.model;

import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.model.interfacemodel.IMainModel;
import com.chuyu.gaosuproject.model.interfacemodel.IMobileSignsMode;

import com.chuyu.gaosuproject.model.interfacemodel.ISignNodeModel;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wo on 2017/7/11.
 * 获取定位数据
 */

public class MobileSignsMode implements IMobileSignsMode {
    public static MobileSignsMode signsMode;

    private MobileSignsMode(){}

    public static MobileSignsMode getInstance(){
        if (signsMode==null){
            signsMode=new MobileSignsMode();
        }
        return signsMode;
    }


    @Override
    public void getAllPresonNode(String userid, final GetAllPresonNodeListener getAllPresonNodeListener) {
        if (TextUtils.isEmpty(userid)){

        }else {
            OkGo.post(UrlConstant.formatUrl(UrlConstant.ALLPRESONURL))
                    .connTimeOut(30000)
                    .params("UserId",userid)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            //getAllPresonNodeListener.getDateSuccess(s);
                           // Log.i("test","所有人个人考勤记录："+s.toString());
                            //截取

                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            getAllPresonNodeListener.getFaile();
                        }
                    });
        }
    }

    @Override
    public String getLocation(AMapLocationClient mLocationClient, AMapLocationClientOption mLocationOption, IMainModel.GetWeatherDataListener getWeatherDataListener) {
        //设置定位回调监听
        AMapLocationListener mLocationListener=new AMapLocationListener() {
            @Override
            public void onLocationChanged(AMapLocation amapLocation) {
                if (amapLocation != null) {
                    if (amapLocation.getErrorCode() == 0) {
                        //可在其中解析amapLocation获取相应内容。
                        String city = amapLocation.getCity();//城市信息
                        int locationType = amapLocation.getLocationType();//获取当前定位结果来源，如网络定位结果，详见定位类型表
                    } else {
                        //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                        Log.e("AmapError", "location Error, ErrCode:"
                                + amapLocation.getErrorCode() + ", errInfo:"
                                + amapLocation.getErrorInfo());
                    }
                }
            }
        };
        mLocationClient.setLocationListener(mLocationListener);

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
        mLocationOption.setHttpTimeOut(20000);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();

        return null;
    }

    @Override
    public String getSignTime() {
        return null;
    }


}
