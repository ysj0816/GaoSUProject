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

    private SignsModel(){}

    public static SignsModel signsModel;
    private static Gson gson;

    public static SignsModel getInstance(){
        if (signsModel==null){
            signsModel=new SignsModel();
            gson=new Gson();
        }
        return signsModel;
    }


    @Override
    public void SignSubmit(String UserId, String DutyDate, String DutyType, String Location, String Lng, String lat,
                            String Type, String rebark, File file, final SignSubmitListener signSubmitListener) {
        if (TextUtils.isEmpty(UserId)||TextUtils.isEmpty(DutyDate)){
            signSubmitListener.textEmpty();
        }else {
            Log.i("test","teqing:"+Type);
            OkGo.post(UrlConstant.formatUrl(UrlConstant.SIGNURL))
                    .params("UserId",UserId)
                    .params("DutyDate",DutyDate)
                    .params("DutyType",DutyType)
                    .params("Location",Location)
                    .params("Lng",Lng)
                    .params("file",file)
                    .params("Lat",lat)
                    .params("type",Type)
                    .params("Remark",rebark)

                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            try {
                                SignQIanBean signQIanBean = gson.fromJson(s, SignQIanBean.class);
                                Log.i("test","sign:"+signQIanBean.toString());
                                if (signQIanBean.isSuccess()){
                                    signSubmitListener.submitSuccess();
                                }else {
                                    signSubmitListener.submitExpretion(signQIanBean.getMsg());
                                }
                            }catch  (Exception e){
                                signSubmitListener.submitExpretion("数据解析异常");
                            }

                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            signSubmitListener.submitFailed();
                        }
                    });
        }
    }

    /**
     * 判断是否重复签到
     * @param userid
     * @param DutyDate
     * @param DutyType
     * @param SignListener
     */
    @Override
    public void rePreSign(String userid, String DutyDate, final int DutyType, final SignListener SignListener) {
        OkGo.post(UrlConstant.formatUrl(UrlConstant.IsSIgn))
                .connTimeOut(30000)//设置30s超时

                .params("UserId",userid)
                .params("DutyDate",DutyDate)
                .params("DutyType",DutyType)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //解析
                        Log.i("test","是否重复签到："+s.toString());
                        IsSignBean isSign = gson.fromJson(s, IsSignBean.class);
                        if (isSign.isSuccess()){
                            //可以签到
                            SignListener.Success("成功");
                        }else {
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
}
