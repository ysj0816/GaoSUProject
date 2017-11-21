package com.chuyu.gaosuproject.view;

import com.chuyu.gaosuproject.base.BaseView;

/**
 * Created by wo on 2017/7/14.
 */

public interface ILeaveView extends BaseView {
    void showWaiting();
    void colseWaiting();
    void shwoExpretion(String msg);
    void leaveSuccess();
    void leaveFaile();
    //void 是否重复请假
    void isLeaved(boolean isleave);
}
