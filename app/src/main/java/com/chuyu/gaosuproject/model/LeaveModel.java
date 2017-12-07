package com.chuyu.gaosuproject.model;

import android.text.TextUtils;
import android.util.Log;

import com.chuyu.gaosuproject.activity.LeaveActivity;
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
    public void submitLeave(LeaveActivity activity, String UserId, int DutyType, String StartDate,
                            String EndDate, String LeaveReason, int type, final LeaveListener leaveListener) {
        //Log.i("test","1231请假："+UserId+"\n"+DutyType+"\n"+StartDate+"\n"+EndDate+"\n"+LeaveReason+"\n"+type);
        OkGo.post(UrlConstant.formatUrl(UrlConstant.LEAVEURL))
                .tag(activity)
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
                        Log.i("test","请假提交失败：");
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
    public void judgeLeave(LeaveActivity activity,String UserId, int DutyType, String DutyDate, final ResLeaveListener resLeaveListener) {
        OkGo.post(UrlConstant.formatUrl(UrlConstant.IsSIgn))
                .tag(activity)
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
                        //网络失败
                        resLeaveListener.notLeave();
                        e.printStackTrace();
                        Log.i("test","请假检查失败：");
                    }
                });
    }

    /**
     * 广播中判断是否能够请假
     * @param UserId
     * @param DutyType
     * @param DutyDate
     * @param resLeaveListener
     */
    public void ableToleave(Long tag,  String UserId, int DutyType, String DutyDate, final ResLeaveListener resLeaveListener){
        OkGo.post(UrlConstant.formatUrl(UrlConstant.IsSIgn))
                .connTimeOut(30000)//设置30s超时
                .tag(tag)
                .params("UserId",UserId)
                .params("DutyDate",DutyDate)
                .params("DutyType",DutyType)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try{
                            IsSignBean isSign = gson.fromJson(s, IsSignBean.class);
                            if (isSign.isSuccess()){
                                //可以请假
                                resLeaveListener.isLeave();

                            }else {
                                //重复请假不能提交

                                resLeaveListener.notLeave();
                            }
                        }catch (Exception e){
                            resLeaveListener.shwoExpretion("");
                        }
                        Log.i("test","可以请假："+s);

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        resLeaveListener.shwoExpretion("");
                        e.printStackTrace();
                        Log.i("test","请假检查网络异常：");
                    }
                });
    }

    /**
     * 在广播中提交请假
     * @param UserId
     * @param DutyType
     * @param StartDate
     * @param EndDate
     * @param LeaveReason
     * @param type
     * @param leaveListener
     */
    public void onReceiverLeave(Long tag,String UserId, int DutyType, String StartDate,
                                String EndDate, String LeaveReason, int type, final LeaveListener leaveListener){
        OkGo.post(UrlConstant.formatUrl(UrlConstant.LEAVEURL))
                .tag(tag)
                .params("UserId", UserId)
                .params("DutyType", DutyType)
                .params("StartDate", StartDate)
                .params("EndDate", EndDate)
                .params("LeaveReason", LeaveReason)
                .params("type", type)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            //解析
                            LeaveBean leaveBean = gson.fromJson(s, LeaveBean.class);
                            Log.i("test","ben:"+leaveBean);
                            if (leaveBean.isSuccess()){
                                leaveListener.submitSuccess();
                            }else {
                                leaveListener.submitFaile();
                            }
                        }catch (Exception e){
                            leaveListener.shwoExpretion("");
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        e.printStackTrace();
                        leaveListener.shwoExpretion("");
                        Log.i("test","请假提交网络异常：");
                    }
                });
    }

    /**
     * 取消tag
     * @param tag
     */
    public void cancelOKGO(Object tag){
        OkGo.getInstance().cancelTag(tag);
    }
}
