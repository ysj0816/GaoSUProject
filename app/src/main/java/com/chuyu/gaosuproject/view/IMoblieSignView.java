package com.chuyu.gaosuproject.view;

import com.chuyu.gaosuproject.base.BaseView;

/**
 * Created by wo on 2017/7/17.
 */

public interface IMoblieSignView extends BaseView {
    void showWaiting();
    void colseWaiting();
    void onSuccess(String data);
    void onFailed();

}
