package com.chuyu.gaosuproject.service;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;

import com.chuyu.gaosuproject.bean.dailycheck.DailyCheck;
import com.chuyu.gaosuproject.bean.daobean.SignAndLeaveData;
import com.chuyu.gaosuproject.bean.logmanagebean.ManageLog;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.util.PictureUtil;
import com.chuyu.gaosuproject.util.upload.OnWifiLoadDailyCheck;
import com.chuyu.gaosuproject.util.upload.OnWifiUpLoadLog;
import com.chuyu.gaosuproject.util.upload.SignLeaveDao;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;


import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

import static com.chuyu.gaosuproject.constant.UrlConstant.WIFIIPLOAD;

/**
 * @author 杨仕俊
 * @description 打卡服务上传缓存数据
 * @email yangshijun156@foxmail.com
 * @date 2017/11/28 16:48:12
 * Created by wo on 2017/12/8.
 */

public class UpLoadSercvice extends Service {

    private MyHandle myHandle;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private List<DailyCheck> dailyChecks;
    private List<SignAndLeaveData> signAndLeaveDatas;
    List<ManageLog> manageLogs;

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        dailyChecks = OnWifiLoadDailyCheck.getInstance().queryData();
        //查询数据库
        signAndLeaveDatas = SignLeaveDao.getInstace().queryData();
        manageLogs = OnWifiUpLoadLog.getInstace().queryData();
        myHandle = new MyHandle();
        MyThread myThread = new MyThread();
        if (signAndLeaveDatas != null || manageLogs != null || dailyChecks != null) {
            if (!signAndLeaveDatas.isEmpty() || !manageLogs.isEmpty() || !dailyChecks.isEmpty()) {
                Log.i("test", "启动服务 签到：" + signAndLeaveDatas.size()
                        + "\n日志：" + manageLogs.size()
                        + "\n日常检查：" + dailyChecks.size()
                );
                myThread.start();
            }
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * @param listcheck
     */
    private void commitData(List<DailyCheck> listcheck) {
        List<SignAndLeaveData> leaveDatas = initSignData(signAndLeaveDatas);
        List<ManageLog> logList = initLogDatas(manageLogs);
        if (signAndLeaveDatas != null && manageLogs != null && dailyChecks != null) {
            //编辑json
            Gson gson = new Gson();
            HashMap<Object, Object> mHashMap = new HashMap<>();
            mHashMap.put("jsonEveBeanList", leaveDatas);
            mHashMap.put("jsonWorkBeanList", logList);
            mHashMap.put("jsonCheckBeanList", listcheck);
            String toJson = gson.toJson(mHashMap);
            commitCacheData(toJson);
        }
    }

    /**
     * 提交缓存数据
     * @param toJson
     */
    private void commitCacheData(String toJson) {
        String encode = "";
        byte[] bytes = new byte[0];
        try {
            encode = URLEncoder.encode(toJson, "UTF-8");
            bytes = encode.getBytes();
        } catch (Exception e) {
            e.printStackTrace();
        }

        OkGo.post(UrlConstant.formatUrl(WIFIIPLOAD))
                .connTimeOut(300000)
                .upBytes(bytes)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject object = new JSONObject(s);
                            boolean success = object.getBoolean("success");
                            Log.i("test","后台上传成功！");
                            if (success){
                                //后台上传成功
                                delete();

                            }else{

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        e.printStackTrace();
                        Log.e("test","失败了");
                        //delete();
                    }
                });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }



    public class MyHandle extends Handler {
        @Override
        public void handleMessage(Message msg) {
            List<DailyCheck> checkList = (List<DailyCheck>) msg.obj;
            commitData(checkList);
        }
    }


    public class MyThread extends Thread {
        @Override
        public void run() {
            List<DailyCheck> checkList = initCheckData(UpLoadSercvice.this.dailyChecks);
            Message message = new Message();
            message.obj = checkList;
            myHandle.sendMessage(message);
        }
    }

    /**
     * 删除数据库表中所有数据
     */
    public void delete(){
        OnWifiLoadDailyCheck.getInstance().deleteALL();
        SignLeaveDao.getInstace().deleteALL();
        OnWifiUpLoadLog.getInstace().deleteALL();
    }

    /**
     * 日常检查的数据
     * @param dailyChecks
     * @return
     */
    private List<DailyCheck> initCheckData(List<DailyCheck> dailyChecks) {
        List<DailyCheck> listCheck = new ArrayList<>();
        int size = dailyChecks.size();
        List<List<String>> listsImg = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            List<String> listStrImg = new ArrayList<>();
            List<String> images = dailyChecks.get(i).getImages();
            int a=images.size();
            for (int j = 0; j < a; j++) {
                String base64 = PictureUtil.bitmapToBase64(images.get(j));
                listStrImg.add(base64);
            }
            listsImg.add(listStrImg);
        }

        for (int i = 0; i < size; i++) {
            DailyCheck check = new DailyCheck(
                    dailyChecks.get(i).getUserid(),
                    dailyChecks.get(i).getCheckunit(),
                    dailyChecks.get(i).getCheckproject(),
                    dailyChecks.get(i).getCheckresult(),
                    dailyChecks.get(i).getContent(),
                    dailyChecks.get(i).getDeductpoint(),
                    dailyChecks.get(i).getCheckid(),
                    listsImg.get(i)
            );
            listCheck.add(check);
        }

        return listCheck;
    }

    /**
     * 日志数据
     * @param manageLogs
     * @return
     */
    private List<ManageLog> initLogDatas(List<ManageLog> manageLogs) {
        List<ManageLog> list = new ArrayList<>();
        int size = manageLogs.size();
        for (int i = 0; i < size; i++) {
            ManageLog manageLog = new ManageLog(manageLogs.get(i).getAuthoruserid(),
                    manageLogs.get(i).getCreatetime(),
                    manageLogs.get(i).getFinishwork(),
                    manageLogs.get(i).getUnfinishwork(),
                    manageLogs.get(i).getNeedassistwork(),
                    manageLogs.get(i).getRemark(),
                    manageLogs.get(i).getCategory());
            list.add(manageLog);
        }

        return list;
    }

    /**
     * 签到请假的数据
     * @param signAndLeaveDatas
     * @return
     */
    private List<SignAndLeaveData> initSignData(List<SignAndLeaveData> signAndLeaveDatas) {
        List<SignAndLeaveData> list = new ArrayList<>();
        int size = signAndLeaveDatas.size();
        for (int i = 0; i < size; i++) {
            SignAndLeaveData leaveData = new SignAndLeaveData(signAndLeaveDatas.get(i).getUserid(),
                    signAndLeaveDatas.get(i).getDutydate(),
                    signAndLeaveDatas.get(i).getType(),
                    signAndLeaveDatas.get(i).getDutytype(),
                    signAndLeaveDatas.get(i).getLocation(),
                    signAndLeaveDatas.get(i).getLng(),
                    signAndLeaveDatas.get(i).getLat(),
                    signAndLeaveDatas.get(i).getStartdate(),
                    signAndLeaveDatas.get(i).getEnddate(),
                    signAndLeaveDatas.get(i).getRebark(),
                    signAndLeaveDatas.get(i).getLeavereason(),
                    signAndLeaveDatas.get(i).getImage());
            list.add(leaveData);
        }
        return list;
    }
}
