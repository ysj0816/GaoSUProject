package com.chuyu.gaosuproject.base;

/**
 * Created by wo on 2017/7/5.
 * mvp Presenter基类
 */

public class BasePresenter<V extends BaseView> {
    public V view;

    public void attach(V view) {
        this.view = view;
    }

    public boolean isViewAttached() {
        return view != null;
    }

    public V getView() {
        return view == null ? null : view;
    }

    public void distach() {

        view = null;
    }
}
