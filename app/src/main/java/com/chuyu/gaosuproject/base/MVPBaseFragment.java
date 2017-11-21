package com.chuyu.gaosuproject.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by wo on 2017/7/5.
 * mvp Fragment基类
 */

public abstract class MVPBaseFragment<V extends BaseView,P extends BasePresenter<V>> extends BaseFragment {
    private P mPresenter;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //初始化presenter
        mPresenter = initPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //父类实现方法
        //   View view
        View view = super.onCreateView(inflater, container, savedInstanceState);
        //添加进p中
        mPresenter.attach((V) this);
        return view;
    }

    protected abstract P initPresenter();

    @Override
    public void onDestroy() {
        mPresenter.distach();
        super.onDestroy();
    }
}
