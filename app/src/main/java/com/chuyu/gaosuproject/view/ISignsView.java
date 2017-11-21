package com.chuyu.gaosuproject.view;

import com.chuyu.gaosuproject.base.BaseView;

/**
 * Created by wo on 2017/7/13.
 */

public interface ISignsView extends BaseView {
    //显示等待框
    void showWaiting();
    //关闭
    void closeWaitting();
    //提示信息
    void showMesg(String msg);
    //提示进度
    void showPrograss();
    //提示失败
    void showSubmitFaile();
    //提示成功
    void showSubSuccess();
    //void 是否重复签到
    void isReprSign(boolean isSign,String msg);
}
