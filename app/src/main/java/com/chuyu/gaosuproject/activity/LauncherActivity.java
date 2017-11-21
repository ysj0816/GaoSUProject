package com.chuyu.gaosuproject.activity;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.util.AppManager;
import com.chuyu.gaosuproject.util.LocationCityUtil;
import com.chuyu.gaosuproject.util.PermissionsChecker;
import com.chuyu.gaosuproject.util.SPUtils;
import com.chuyu.gaosuproject.util.ToastUtils;

/**
 * app 启动页
 */
public class LauncherActivity extends Activity {

    private static final int APP_HANDLER = 0x1;
    private static final long SPLASH_DELAY_MILLIS = 3000;
    private static final int REQUEST_CODE = 0; // 请求码

    // 需检查的全部敏感权限
    static final String[] PERMISSIONS = new String[]{
            "android.permission.READ_PHONE_STATE",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.CAMERA"
    };

    // 权限检测器
    private PermissionsChecker mPermissionsChecker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);
        initIPPATH();
        initPermission();
        initCity();
        AppManager.getAppManager().addActivity(this);
    }

    private void initCity() {
        AMapLocationClient mLocationClient;
        AMapLocationClientOption mLocationOption;
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //获取当前城市 和经纬度
        LocationCityUtil.getInstance().getNowLocaiton(mLocationClient, mLocationOption, new LocationCityUtil.LocationListener() {
            @Override
            public void locationSuccess(AMapLocation amapLocation) {
                //城市
                String city = amapLocation.getCity();
                String cityname=city.substring(0,city.length()-1);
                //保存城市名字
                SPUtils.put(LauncherActivity.this,SPConstant.CITYNAME,cityname);
            }

            @Override
            public void locationFaile(AMapLocation amapLocation) {

            }
        });
    }

    /**
     * 初始化整个app的ip和端口
     */
    private void initIPPATH() {
        String ip = (String) SPUtils.get(this, SPConstant.URLIP, "");
        String port = (String) SPUtils.get(this, SPConstant.PORT, "");
        if (TextUtils.isEmpty(ip)&&TextUtils.isEmpty(port)){
            //如果第一次进来，没有IP和端口　　就设置默认值并保存
            SPUtils.put(this,SPConstant.URLIP,"219.139.79.56");
            SPUtils.put(this,SPConstant.PORT,"8000");
            //赋值
            UrlConstant.setIP("219.139.79.56");
            UrlConstant.setPORT("8000");
        }else{
            //如果保存了  直接设置ip
            UrlConstant.IP=ip;
            UrlConstant.PORT=port;
        }
    }

    /**
     * 检查敏感权限
     */
    private void initPermission() {
        mPermissionsChecker = new PermissionsChecker(this);
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            startPermissionsActivity();
        } else {
            mHandler.sendEmptyMessageDelayed(APP_HANDLER, SPLASH_DELAY_MILLIS);
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case APP_HANDLER:
                    jumpNext();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 跳转界面
     */
    private void jumpNext() {
        //不是第一次进入
        if ((Boolean) SPUtils.get(this,SPConstant.FIRST_JOIN,true)){
            startActivity(new Intent(this,GuideActivity.class));
        }else if (!(Boolean)SPUtils.get(this,SPConstant.IS_LOGIN,false)){
            //未登录
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }else {
            //主界面
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        finish();


    }



    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            ToastUtils.showSure(this,"缺少主要权限，可能影响某些功能的使用！");
            requestWriteSettings();
        } else if(requestCode == REQUEST_CODE_WRITE_SETTINGS){
            if (Settings.System.canWrite(this)) {

                Log.i("test", "onActivityResult write settings granted" );
            } else {
                Log.i("test", "onActivityResult write settings unGranted" );
            }
            mHandler.sendEmptyMessageDelayed(APP_HANDLER, SPLASH_DELAY_MILLIS);
        } else {
            mHandler.sendEmptyMessageDelayed(APP_HANDLER, SPLASH_DELAY_MILLIS);
            //requestWriteSettings();
        }
//        mHandler.sendEmptyMessageDelayed(APP_HANDLER, SPLASH_DELAY_MILLIS);
    }

    private static final int REQUEST_CODE_WRITE_SETTINGS = 1;
    private static final String LOGTAG = "WRITE_SETTINGS";
    private void requestWriteSettings() {
        //可修改系统设置
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, REQUEST_CODE_WRITE_SETTINGS );
    }
}
