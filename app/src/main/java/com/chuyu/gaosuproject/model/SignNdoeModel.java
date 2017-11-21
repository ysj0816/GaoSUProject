package com.chuyu.gaosuproject.model;

import android.text.TextUtils;
import android.util.Log;

import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.model.interfacemodel.ISignNodeModel;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wo on 2017/7/15.
 */

public class SignNdoeModel implements ISignNodeModel {

    private SignNdoeModel(){}

    private static SignNdoeModel signNdoeModel;

    public static SignNdoeModel getInstance(){
        if (signNdoeModel==null){
            signNdoeModel=new SignNdoeModel();
        }
        return signNdoeModel;
    }

    /**查看个人签到记录
     * @param userid
     * @param getSignNodeListener
     */
    @Override
    public void getOneselfNode(String userid, final GetSignNodeListener getSignNodeListener) {
        if (TextUtils.isEmpty(userid)){

        }else {
            OkGo.post(UrlConstant.formatUrl(UrlConstant.PERSONSIGNURL))
                    .params("UserId",userid)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            getSignNodeListener.getDateSuccess(s);
                            Log.i("test","个人考勤记录："+s.toString());
                        }
                    });
        }
    }

    /**
     * 获取个人的单日考勤记录监听
     * @param userid
     * @param dutyDate
     * @param getOneSelfNodeDateListener
     */
    @Override
    public void getOneSelfNodeDate(String userid, String dutyDate, final GetOneSelfNodeDateListener getOneSelfNodeDateListener) {
        if (TextUtils.isEmpty(userid)){

        }else {
            OkGo.post(UrlConstant.formatUrl(UrlConstant.PERSONSIGNURL))
                    .params("UserId",userid)
                    .params("DutyDate",dutyDate)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            getOneSelfNodeDateListener.getDateSuccess(s);
                            Log.i("test","个人通过日期考勤记录："+s.toString());
                        }
                    });
        }
    }


    /**
     * 获取所有人的签到记录监听
     * @param userid
     * @param getAllPresonNodeListener
     */
    @Override
    public void getAllPresonNode(String userid, final GetAllPresonNodeListener getAllPresonNodeListener) {
        if (TextUtils.isEmpty(userid)){

        }else {
            OkGo.post(UrlConstant.formatUrl(UrlConstant.ALLPRESONURL))
                    .params("UserId",userid)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            getAllPresonNodeListener.getDateSuccess(s);
                            Log.i("test","所有人个人考勤记录："+s.toString());
                        }
                    });
        }
    }

    /**
     * 删除当日考勤记录监听
     * @param userid
     * @param dutyType
     * @param currentDate
     * @param deleteSignNodeListener
     */
    @Override
    public void deleteSignNodeDate(String userid, int dutyType, String currentDate, final DeleteSignNodeListener deleteSignNodeListener) {
        if (TextUtils.isEmpty(userid)){

        }else {
            OkGo.post(UrlConstant.formatUrl(UrlConstant.DELETESIGNURL))
                    .params("UserId",userid)
                    .params("DutyType",dutyType)
                    .params("CurrentDate",currentDate)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            deleteSignNodeListener.getDateSuccess(s);
                            Log.i("test","删除个人考勤记录："+s.toString());
                        }
                    });
        }
    }

    /**
     * 未打卡考勤记录
     * @param userid
     * @param date
     * @param notSignListener
     */
    @Override
    public void notSignNode(String userid, String date, final NotSignListener notSignListener) {
        if (TextUtils.isEmpty(userid)){

        }else {
            OkGo.post(UrlConstant.formatUrl(UrlConstant.PERSONSIGNURL))
                    .params("UserId",userid)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            notSignListener.getDateSuccess(s);
                            Log.i("test","个人考勤记录："+s.toString());
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            notSignListener.getFaile();
                        }
                    });
        }
    }
}
