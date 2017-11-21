package com.chuyu.gaosuproject.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wo on 2017/7/5.
 * activity基类
 */

public abstract class BaseActivity extends AppCompatActivity {

    private Unbinder bind;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(initContent());
        bind = ButterKnife.bind(this);
        initData();
        initView();

    }
    //布局xml
    protected abstract int initContent();
    //初始化控件
    protected abstract void initView();
    //初始化数据
    protected abstract void initData();

    //取消订阅 、绑定
    @Override
    protected void onDestroy() {
        bind.unbind();
        super.onDestroy();
    }

}
