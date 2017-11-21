package com.chuyu.gaosuproject.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by wo on 2017/7/5.
 * Fragment基类
 */

public abstract class BaseFragment extends Fragment {

    private View view;
    private Unbinder bind;
    private boolean isVisible;                  //是否可见状态
    private boolean isPrepared;                 //标志位，View已经初始化完成。
    private boolean isFirstLoad = true;         //是否第一次加载

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = initView(inflater,container, savedInstanceState);
        bind= ButterKnife.bind(this,view);
        initData();
        isFirstLoad=true;//第一次创建
        isPrepared=true;//view初始化完成

        return view;
    }

    //用户是否可见
    /** 如果是与ViewPager一起使用，调用的是setUserVisibleHint */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (getUserVisibleHint()) {
            //获取用户可见状态
            isVisible = true;
            //可见
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }

    }
    /**
     * 如果是通过FragmentTransaction的show和hide的方法来控制显示，调用的是onHiddenChanged.
     * 若是初始就show的Fragment 为了触发该事件 需要先hide再show
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            isVisible = true;
            onVisible();
        } else {
            isVisible = false;
            onInvisible();
        }
    }

    protected abstract View initView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState);

    //数据初始化
    public abstract void initData();

    //可见时
    protected void onVisible() {
        lazyLoad();
    }
    //不可见时
    private void onInvisible() {
    }

    //异步加载  如果没有完成初始化或者不可见或者不是第一次创建  才能加载数据
    protected void lazyLoad() {
        if (!isPrepared || !isVisible || !isFirstLoad) {
            return;
        }
        isFirstLoad = false;
        initData();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        bind.unbind();
        isFirstLoad=false;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
