package com.chuyu.gaosuproject.view;


import android.view.View;

import com.chuyu.gaosuproject.base.BaseView;

/**
 * Created by wo on 2017/7/15.
 */

public interface ISignNodeView extends BaseView {
    void showWaiting();
    void colseWaiting();
    void onSuccess(String data);
    void onFailed();
    void showExpcetion(String msg);
}
