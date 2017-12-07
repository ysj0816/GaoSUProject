package com.chuyu.gaosuproject.model;

import android.text.TextUtils;
import android.util.Log;

import com.chuyu.gaosuproject.bean.IsSignBean;
import com.chuyu.gaosuproject.bean.SignQIanBean;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.model.interfacemodel.ISignsModel;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.io.File;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wo on 2017/7/13.
 */

public class SignsModel implements ISignsModel {

    private SignsModel() {
    }

    public static SignsModel signsModel;
    private static Gson gson;

    public static SignsModel getInstance() {
        if (signsModel == null) {
            signsModel = new SignsModel();
            gson = new Gson();
        }
        return signsModel;
    }

    /**
     * 正常提交
     *
     * @param UserId
     * @param DutyDate
     * @param DutyType
     * @param Location
     * @param Lng
     * @param lat
     * @param Type
     * @param rebark
     * @param file
     * @param signSubmitListener
     */
    @Override
    public void SignSubmit(String UserId, String DutyDate, String DutyType, String Location, String Lng, String lat,
                           String Type, String rebark, File file, final SignSubmitListener signSubmitListener) {
        if (TextUtils.isEmpty(UserId) || TextUtils.isEmpty(DutyDate)) {
            signSubmitListener.textEmpty();
        } else {
            Log.i("test", "UserId:" + UserId + "\nDutyDate:" + DutyDate + "\nDutyType:" + DutyType + "\nLocation:" + Location
                    + "\nLng:" + Lng
                    + "\nlat:" + lat
                    + "\nType:" + Type
                    + "\nrebark:" + rebark
            );
            OkGo.post(UrlConstant.formatUrl(UrlConstant.SIGNURL))
                    .connTimeOut(120000)
                    .params("UserId", UserId)
                    .params("DutyDate", DutyDate)
                    .params("DutyType", DutyType)
                    .params("Location", Location)
                    .params("Lng", Lng)
                    .params("file", file)
                    .params("Lat", lat)
                    .params("type", Type)
                    .params("Remark", rebark)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            try {
                                SignQIanBean signQIanBean = gson.fromJson(s, SignQIanBean.class);
                                Log.i("test", "sign:" + signQIanBean.toString());
                                if (signQIanBean.isSuccess()) {
                                    signSubmitListener.submitSuccess();
                                } else {
                                    signSubmitListener.submitExpretion(signQIanBean.getMsg());
                                }
                            } catch (Exception e) {
                                signSubmitListener.submitExpretion("数据解析异常");
                            }

                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            Log.i("test", "提交失败");
                            e.printStackTrace();
                            signSubmitListener.submitFailed();
                        }
                    });
        }
    }

    /**
     * 判断是否重复签到
     *
     * @param userid
     * @param DutyDate
     * @param DutyType
     * @param SignListener
     */
    @Override
    public void rePreSign(String userid, String DutyDate, final int DutyType, final SignListener SignListener) {
        OkGo.post(UrlConstant.formatUrl(UrlConstant.IsSIgn))
                .connTimeOut(30000)//设置30s超时
                .tag(this)
                .params("UserId", userid)
                .params("DutyDate", DutyDate)
                .params("DutyType", DutyType)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //解析
                        Log.i("test", "是否重复签到：" + s.toString());
                        IsSignBean isSign = gson.fromJson(s, IsSignBean.class);
                        if (isSign.isSuccess()) {
                            //可以签到
                            SignListener.Success("成功");
                        } else {
                            //重复签到不能提交
                            SignListener.Failed(isSign.getMsg());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        //网络失败
                        SignListener.NetWorKException("网络错误");
                    }
                });
    }

    /**
     * 接收到网络状态改变时 请求服务器判断是否重复签到
     *
     * @param userid
     * @param DutyDate
     * @param DutyType
     * @param onReceiverIsSignListener
     */
    @Override
    public void recevicerIsSign(String userid, String DutyDate, int DutyType,
                                final ReceiverIsSignListener onReceiverIsSignListener) {
        Log.i("test", "数据库中检查userid:" + userid + "\nDutyDate:" + DutyDate + "\nDutyType:" + DutyType);
        OkGo.post(UrlConstant.formatUrl(UrlConstant.IsSIgn))
                .connTimeOut(60000)//设置30s超时
                .params("UserId", userid)
                .params("DutyDate", DutyDate)
                .params("DutyType", DutyType)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {

                        IsSignBean isSign = gson.fromJson(s, IsSignBean.class);
                        //解析
                        Log.i("receiver", "广播中是否重复签到：" + s.toString());
                        if (isSign.isSuccess()) {
                            //可以签到
                            onReceiverIsSignListener.Success();
                        } else {
                            //重复签到不能提交
                            onReceiverIsSignListener.Failed();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        Log.i("test", "广播中检查网络错误");
                        //网络失败
                        onReceiverIsSignListener.NetWorKException();
                    }
                });
    }

    /**
     * 在接收到广播后提交数据
     *
     * @param UserId
     * @param DutyDate
     * @param DutyType
     * @param Location
     * @param Lng
     * @param lat
     * @param Type
     * @param rebark
     * @param file
     * @param onReceiverSubmitSignListener
     */
    @Override
    public void SubmitOnReceive(String UserId, String DutyDate,
                                String DutyType, String Location, String Lng,
                                String lat, String Type, String rebark, File file,
                                final OnReceiverSubmitSignListener onReceiverSubmitSignListener) {
        OkGo.post(UrlConstant.formatUrl(UrlConstant.SIGNURL))
                .connTimeOut(60000)//设置30s超时
                .params("UserId", UserId)
                .params("DutyDate", DutyDate)
                .params("DutyType", DutyType)
                .params("Location", Location)
                .params("Lng", Lng)
                .params("file", file)
                .params("Lat", lat)
                .params("type", Type)
                .params("Remark", rebark)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            SignQIanBean signQIanBean = gson.fromJson(s, SignQIanBean.class);
                            Log.i("test", "广播wifi中签到:" + signQIanBean.toString());
                            if (signQIanBean.isSuccess()) {
                                onReceiverSubmitSignListener.onSubmitSuccess();
                            } else {
                                onReceiverSubmitSignListener.onSubmitFailed();
                            }
                        } catch (Exception e) {
                            onReceiverSubmitSignListener.NetWorKException();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        onReceiverSubmitSignListener.NetWorKException();
                    }
                });
    }


}
