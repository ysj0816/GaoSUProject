package com.chuyu.gaosuproject.presenter;

import com.chuyu.gaosuproject.base.BasePresenter;
import com.chuyu.gaosuproject.model.SignNdoeModel;
import com.chuyu.gaosuproject.model.interfacemodel.IMobileSignsMode;
import com.chuyu.gaosuproject.model.interfacemodel.ISignNodeModel;
import com.chuyu.gaosuproject.view.IMoblieSignView;

/**
 * Created by wo on 2017/7/17.
 */

public class MobileSignPresenter extends BasePresenter<IMoblieSignView> {

    /**
     * 获取所有人的签到记录
     *
     * @param userid
     */
    public void getAllPresonNode(String userid) {
        if (!isViewAttached()) {
            return;
        }
        final IMoblieSignView view = getView();
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
                view.onFailed();

            }
        });
    }

}
