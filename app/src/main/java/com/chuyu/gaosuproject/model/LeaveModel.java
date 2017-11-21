package com.chuyu.gaosuproject.model;

import android.text.TextUtils;
import android.util.Log;

import com.chuyu.gaosuproject.bean.IsSignBean;
import com.chuyu.gaosuproject.bean.LeaveBean;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.model.interfacemodel.ILeaveModel;
import com.chuyu.gaosuproject.model.interfacemodel.ISignsModel;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wo on 2017/7/14.
 */

public class LeaveModel implements ILeaveModel {

    private static LeaveModel leaveModel;
    private static Gson gson;

    private LeaveModel() {
    }

    public static LeaveModel getInstance() {
        if (leaveModel == null) {
            leaveModel = new LeaveModel();
            gson = new Gson();
        }
        return leaveModel;
    }

    /**
     * @param UserId
     * @param DutyType
     * @param StartDate
     * @param EndDate
     * @param LeaveReason
     * @param type
     * @param leaveListener
     */
    @Override
    public void submitLeave(String UserId, int DutyType, String StartDate,
                            String EndDate, String LeaveReason, int type, final LeaveListener leaveListener) {
        //Log.i("test","1231请假："+UserId+"\n"+DutyType+"\n"+StartDate+"\n"+EndDate+"\n"+LeaveReason+"\n"+type);
        OkGo.post(UrlConstant.formatUrl(UrlConstant.LEAVEURL))
                .params("UserId", UserId)
                .params("DutyType", DutyType)
                .params("StartDate", StartDate)
                .params("EndDate", EndDate)
                .params("LeaveReason", LeaveReason)
                .params("type", type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //解析
                        LeaveBean leaveBean = gson.fromJson(s, LeaveBean.class);
                        Log.i("test","ben:"+leaveBean);
                        if (leaveBean.isSuccess()){
                            leaveListener.submitSuccess();
                        }else {
                            leaveListener.shwoExpretion(leaveBean.getMsg());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        leaveListener.submitFaile();
                    }
                });

    }

    /**
     * 判断是否重复请假
     * @param UserId
     * @param DutyType
     * @param DutyDate
     * @param resLeaveListener
     */
    @Override
    public void judgeLeave(String UserId, int DutyType, String DutyDate, final ResLeaveListener resLeaveListener) {
        OkGo.post(UrlConstant.formatUrl(UrlConstant.IsSIgn))
                .connTimeOut(30000)//设置30s超时
                .params("UserId",UserId)
                .params("DutyDate",DutyDate)
                .params("DutyType",DutyType)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //解析
                       // Log.i("test","是否重复签到："+s.toString());
                        IsSignBean isSign = gson.fromJson(s, IsSignBean.class);
                        if (isSign.isSuccess()){
                            //可以请假
                            resLeaveListener.isLeave();

                        }else {
                            //重复请假不能提交

                            resLeaveListener.shwoExpretion(isSign.getMsg());
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        //网络失败
                        resLeaveListener.notLeave();
                    }
                });
    }
}
