package com.chuyu.gaosuproject.receviver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import com.chuyu.gaosuproject.util.NetworkUtils;
import com.chuyu.gaosuproject.util.observer.NetChangeObserver;

import java.util.ArrayList;


/**
 * @author 杨仕俊
 * @class com.chuyu.gaosuproject.recevier.NetCheckReceiver
 * @description 注册网络状态改变接收广播
 * @date 2017/11/28 09:31:22
 * @email yangshijun156@foxmail.com
 * <p>
 * Created by ysj on 2017/11/28.
 */

public class NetCheckReceiver extends BroadcastReceiver {
    /**
     * 常量  网络变化
     */
    public final static String CUSTOM_ANDROID_NET_CHANGE_ACTION = "com.yangshijun.api.netstatus.CONNECTIVITY_CHANGE";

    /**
     * 常量 网络连接状态
     */
    private final static String ANDROID_NET_CHANGE_ACTION = "android.net.conn.CONNECTIVITY_CHANGE";

    /**
     * 网络连接类型
     */
    private static NetworkUtils.NetworkType mNetType;

    /**
     * 网络是否连接可用
     */
    private static boolean isNetAvailable = false;

    /**
     * 广播接收者
     */
    private static BroadcastReceiver mBroadcastReceiver;

    /**
     * 网络观察者集合
     */
    private static ArrayList<NetChangeObserver> mNetChangeObservers = new ArrayList<NetChangeObserver>();

    /**
     * 单例模式
     *
     * @return BroadcastReceiver
     */
    private static BroadcastReceiver getReceiver() {
        if (null == mBroadcastReceiver) {
            synchronized (NetCheckReceiver.class) {
                if (null == mBroadcastReceiver) {
                    mBroadcastReceiver = new NetCheckReceiver();
                }
            }
        }
        return mBroadcastReceiver;
    }

    /**
     * 收到广播
     *
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        mBroadcastReceiver = NetCheckReceiver.this;
        //代表网络状态有变化
        if (intent.getAction().equalsIgnoreCase(ANDROID_NET_CHANGE_ACTION) ||
                intent.getAction().equalsIgnoreCase(CUSTOM_ANDROID_NET_CHANGE_ACTION)) {
            if (!NetworkUtils.isConnected()) {
                //网络连接
                isNetAvailable = false;
            } else {
                isNetAvailable = true;
                //获取网络连接类型
                mNetType = NetworkUtils.getNetworkType();

            }
            /**
             * 防止收到两次广播
             * 通知观察者
             */
            notifyObserver();
        }
    }

    /**
     * 动态注册广播
     *
     * @param mContext
     */
    public static void registerNetworkReceiver(Context mContext) {
        IntentFilter filter = new IntentFilter();
        filter.addAction(CUSTOM_ANDROID_NET_CHANGE_ACTION);
        filter.addAction(ANDROID_NET_CHANGE_ACTION);
        mContext.getApplicationContext().registerReceiver(getReceiver(), filter);
    }

    /**
     * 清除
     *
     * @param mContext
     */
    public static void checkNetwork(Context mContext) {
        Intent intent = new Intent();
        intent.setAction(CUSTOM_ANDROID_NET_CHANGE_ACTION);
        mContext.sendBroadcast(intent);
    }

    /**
     * 反注册广播
     *
     * @param mContext
     */
    public static void unRegisterNetworkReceiver(Context mContext) {
        if (mBroadcastReceiver != null) {
            try {
                mContext.getApplicationContext().unregisterReceiver(mBroadcastReceiver);
            } catch (Exception e) {
                Log.e("receiver", "注销广播错误！");
            }

        }
    }

    /**
     * 网络是否可用
     *
     * @return true 可用;  false 不可用
     */
    public static boolean isNetworkAvailable() {
        return isNetAvailable;
    }

    /**
     * 防止接收到两次广播
     */
    public NetworkUtils.NetworkType fristNetType = null;

    /**
     * 通知观察者网络改变
     */
    public void notifyObserver() {
        if (!mNetChangeObservers.isEmpty()) {
            int size = mNetChangeObservers.size();
            for (int i = 0; i < size; i++) {
                NetChangeObserver observer = mNetChangeObservers.get(i);
                if (observer != null) {
                    if (isNetworkAvailable()) {
                        if (mNetType != fristNetType) {
                            observer.onNetConnected(mNetType);
                        }
                        fristNetType = mNetType;

                    } else {
                        observer.onNetDisConnect();
                        fristNetType = null;
                    }
                }
            }
        }
    }

    /**
     * 注册一个网络变化观察者
     *
     * @param observer 网络变化观察者
     */
    public static void registerObserver(NetChangeObserver observer) {
        if (mNetChangeObservers == null) {
            mNetChangeObservers = new ArrayList<NetChangeObserver>();
        }
        mNetChangeObservers.add(observer);
    }

    /**
     * 移除网络观察者
     *
     * @param observer
     */
    public static void removerRegisterObserver(NetChangeObserver observer) {
        if (mNetChangeObservers != null) {
            if (mNetChangeObservers.contains(observer)) {
                mNetChangeObservers.remove(observer);
            }
        }
    }
}
