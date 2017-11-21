package com.chuyu.gaosuproject.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

/**
 * Created by wo on 2017/7/5.
 * mvp activity基类
 */

public abstract class MVPBaseActivity<V extends BaseView,P extends BasePresenter<V>> extends BaseActivity {

    public P mPresenter;

    /**
     * 此方法中 initPresenter()需在初始化布局之前,否则会报空指针异常
     * @param savedInstanceState
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mPresenter = initPresenter();
        mPresenter.attach((V) this);
        super.onCreate(savedInstanceState);
    }

    protected abstract P initPresenter();


}
