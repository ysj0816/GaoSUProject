package com.chuyu.gaosuproject.util;

import android.content.Context;
import android.util.Log;
import android.view.View;

import java.util.HashMap;

/**
 * 防止Button的频繁点击,多次执行点击事件
 * <p/>
 * Created by zhenguo on 4/16/16.
 */
public class OnClickUtil {

    static HashMap hashMap=new HashMap<Integer,Long>();
    /**
     * 判断是否重复点击
     * @return
     */
    static long lastTime = 0;

    public static boolean isFaseDoubleClick(View view) {
        boolean flag = false;
        int id = view.getId();
        Object o = hashMap.get(id);
        if (null==o){
            lastTime=0;
        }else {
            lastTime= (long) o;
        }
        long time = System.currentTimeMillis() - lastTime;
        if (time > 2000) {
            flag = true;
        }
        lastTime = System.currentTimeMillis();
        hashMap.put(id,lastTime);
        return flag;
    }
}
