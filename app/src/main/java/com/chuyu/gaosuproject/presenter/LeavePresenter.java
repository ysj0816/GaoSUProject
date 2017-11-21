package com.chuyu.gaosuproject.presenter;

import android.util.Log;

import com.chuyu.gaosuproject.base.BasePresenter;
import com.chuyu.gaosuproject.model.LeaveModel;
import com.chuyu.gaosuproject.model.interfacemodel.ILeaveModel;
import com.chuyu.gaosuproject.view.ILeaveView;

/**
 * Created by wo on 2017/7/14.
 */

public class LeavePresenter extends BasePresenter<ILeaveView> {

    public void submitLeave(String UserId, int DutyType, String StartDate,
                            String EndDate, String LeaveReason,int type){
        if(isViewAttached()){

        }else {
            return;
        }
        final ILeaveView view = getView();
        //view.showWaiting();
        LeaveModel.getInstance().submitLeave(UserId, DutyType, StartDate, EndDate, LeaveReason, type, new ILeaveModel.LeaveListener() {
            @Override
            public void submitSuccess() {
                view.colseWaiting();
                view.leaveSuccess();
            }

            @Override
            public void submitFaile() {
                view.colseWaiting();
                view.leaveFaile();
            }

            @Override
            public void shwoExpretion(String msg) {
                view.colseWaiting();
                view.shwoExpretion(msg);

            }
        });
    }

    /**
     * 判断是否重复提交请假
     * @param userid
     * @param dutyType
     * @param dutyDate
     */
    public void JuGetLeave(String userid,int dutyType,String dutyDate){
        if(isViewAttached()){

        }else {
            return;
        }
        final ILeaveView view = getView();
        view.showWaiting();
        LeaveModel.getInstance().judgeLeave(userid, dutyType, dutyDate, new ILeaveModel.ResLeaveListener() {
            @Override
            public void isLeave() {
                view.isLeaved(true);
            }

            @Override
            public void notLeave() {
                view.colseWaiting();
                view.isLeaved(false);
            }

            @Override
            public void shwoExpretion(String msg) {
                view.colseWaiting();
                view.shwoExpretion(msg);
            }
        });

    }
}
