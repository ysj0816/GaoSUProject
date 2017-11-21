package com.chuyu.gaosuproject.presenter;


import com.chuyu.gaosuproject.base.BasePresenter;
import com.chuyu.gaosuproject.model.SignNdoeModel;
import com.chuyu.gaosuproject.model.interfacemodel.ISignNodeModel;
import com.chuyu.gaosuproject.view.ISignNodeView;

/**
 * Created by wo on 2017/7/15.
 */

public class SignNodePresenter extends BasePresenter<ISignNodeView> {
    /**
     * 获取个人签到记录
     *
     * @param userid
     */
    public void getOneselfNode(String userid) {

        if (!isViewAttached()) {
            return;
        }
        final ISignNodeView view = getView();
        view.showWaiting();
        SignNdoeModel.getInstance().getOneselfNode(userid, new ISignNodeModel.GetSignNodeListener() {

            @Override
            public void getDateSuccess(String s) {
                view.colseWaiting();
                view.onSuccess(s);
            }

            @Override
            public void getFaile() {
                view.colseWaiting();
                view.onFailed();
            }

            @Override
            public void shwoExpcetion(String msg) {
                view.colseWaiting();
                view.showExpcetion(msg);
            }
        });
    }

    /**
     * 获取个人单日签到记录
     *
     * @param userid
     * @param dutyDate
     */
    public void getOneSelfNodeDate(String userid, String dutyDate) {
        if (!isViewAttached()) {
            return;
        }
        final ISignNodeView view = getView();
        view.showWaiting();
        SignNdoeModel.getInstance().getOneSelfNodeDate(userid, dutyDate, new ISignNodeModel.GetOneSelfNodeDateListener() {
            @Override
            public void getDateSuccess(String s) {
                view.colseWaiting();
                view.onSuccess(s);
            }

            @Override
            public void getFaile() {
                view.colseWaiting();
                view.onFailed();
            }

            @Override
            public void shwoExpcetion(String msg) {
                view.colseWaiting();
                view.showExpcetion(msg);
            }
        });
    }

    /**
     * 获取所有人的签到记录
     *
     * @param userid
     */
    public void getAllPresonNode(String userid) {
        if (!isViewAttached()) {
            return;
        }
        final ISignNodeView view = getView();
        view.showWaiting();
        SignNdoeModel.getInstance().getAllPresonNode(userid, new ISignNodeModel.GetAllPresonNodeListener() {
            @Override
            public void getDateSuccess(String s) {
                view.colseWaiting();
                view.onSuccess(s);
            }

            @Override
            public void getFaile() {
                view.colseWaiting();
                view.onFailed();
            }

            @Override
            public void shwoExpcetion(String msg) {
                view.colseWaiting();
                view.showExpcetion(msg);
            }
        });
    }

    /**
     * 删除当然考勤记录
     *
     * @param userid
     * @param dutyType
     * @param currentDate
     */
    public void deleteSignNodeDate(String userid, int dutyType, String currentDate) {
        if (!isViewAttached()) {
            return;
        }
        final ISignNodeView view = getView();
        view.showWaiting();
        SignNdoeModel.getInstance().deleteSignNodeDate(userid, dutyType, currentDate, new ISignNodeModel.DeleteSignNodeListener() {
            @Override
            public void getDateSuccess(String s) {
                view.colseWaiting();
                view.onSuccess(s);
            }

            @Override
            public void getFaile() {
                view.colseWaiting();
                view.onFailed();
            }

            @Override
            public void shwoExpcetion(String msg) {
                view.colseWaiting();
                view.showExpcetion(msg);
            }
        });
    }

    /**
     * 未打卡记录
     *
     * @param userid
     * @param date
     */
    public void notSignNode(String userid, String date) {
        if (!isViewAttached()) {
            return;
        }
        final ISignNodeView view = getView();
        view.showWaiting();
        SignNdoeModel.getInstance().notSignNode(userid, date, new ISignNodeModel.NotSignListener() {
            @Override
            public void getDateSuccess(String s) {
                view.colseWaiting();
                view.onSuccess(s);
            }

            @Override
            public void getFaile() {
                view.colseWaiting();
                view.onFailed();
            }

            @Override
            public void shwoExpcetion(String msg) {
                view.colseWaiting();
                view.showExpcetion(msg);
            }
        });
    }
}
