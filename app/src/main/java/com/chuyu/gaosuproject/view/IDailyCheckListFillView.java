package com.chuyu.gaosuproject.view;

import com.chuyu.gaosuproject.base.BaseView;

/**
 * Created by Administrator on 2017/7/26. 日常检查表填报
 */

public interface IDailyCheckListFillView extends BaseView{
    void showWaiting();
    void colseWaiting();
    void showExpretion(String msg);
    void fillSuccess();
    void fillFaile();
}
