package com.chuyu.gaosuproject.util.observer;

import com.chuyu.gaosuproject.util.NetworkUtils;

/**
 * @author 杨仕俊
 * @class com.chuyu.gaosuproject.util.observer.NetChangeObserver
 * @description 网络改变观察者.
 * Created by wo on 2017/11/28.
 */

public interface NetChangeObserver {
    /**
     * 网络连接回调
     * @param type 网络连接类型
     */
    void onNetConnected(NetworkUtils.NetworkType type);

    /**
     * 没有网络
     */
    void onNetDisConnect();
}
