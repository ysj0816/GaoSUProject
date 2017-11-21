package com.chuyu.gaosuproject.view;

import com.chuyu.gaosuproject.base.BaseView;
import com.chuyu.gaosuproject.bean.LoginBean;

/**
 * Created by wo on 2017/7/13.
 *
 * mvp中 view层 接口
 *
 */

public interface ILoginView extends BaseView {
    //等待窗口
    void showWaiting();
    //失败窗口
    void showFaile();
    //关闭窗口
    void closeWaiting();
    //异常窗口
    void showExpetion(String i);
    //成功
    void loginSuccess(LoginBean loginBean);

}
