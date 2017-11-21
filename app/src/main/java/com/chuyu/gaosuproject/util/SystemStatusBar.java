package com.chuyu.gaosuproject.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Build;
import android.view.Window;
import android.view.WindowManager;

/**
 * Created by Administrator on 2017/8/1.
 */

public class SystemStatusBar {
    public  Activity context;

    public SystemStatusBar(Activity context){
        this.context=context;
    }
    /**
     * 设置状态栏透明
     */

    @TargetApi(19)
    public void setTranslucentStatus(boolean on) {
        //状态栏透明
        // 4.4以上系统状态栏透明
        Window win = context.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
}
