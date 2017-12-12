package com.chuyu.gaosuproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.adapter.MainGrideAdapter;
import com.chuyu.gaosuproject.base.MVPBaseActivity;
import com.chuyu.gaosuproject.bean.QrCodeBean;
import com.chuyu.gaosuproject.bean.urlip.IPPath;
import com.chuyu.gaosuproject.bean.weather.WeatherInfo;
import com.chuyu.gaosuproject.bean.weather.Weather_data;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.dao.DBManager;
import com.chuyu.gaosuproject.presenter.MainPresenter;
import com.chuyu.gaosuproject.receviver.NetCheckReceiver;
import com.chuyu.gaosuproject.service.UpLoadSercvice;
import com.chuyu.gaosuproject.util.AppManager;
import com.chuyu.gaosuproject.util.IPPathDb;
import com.chuyu.gaosuproject.util.NetworkUtils;
import com.chuyu.gaosuproject.util.OtherUtils;
import com.chuyu.gaosuproject.util.PermissionsChecker;
import com.chuyu.gaosuproject.util.SPUtils;
import com.chuyu.gaosuproject.util.ToastUtils;
import com.chuyu.gaosuproject.util.observer.NetChangeObserver;
import com.chuyu.gaosuproject.view.IMainView;
import com.dalong.marqueeview.MarqueeView;
import com.google.gson.Gson;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.zbar.lib.CaptureActivity;

import junit.framework.Test;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 主页面
 */
public class MainActivity extends MVPBaseActivity<IMainView, MainPresenter>
        implements IMainView {


    @BindView(R.id.view_page)
    ViewPager view_pager;
    @BindView(R.id.NewsTitle)
    TextView newsTitle;
    @BindView(R.id.dotgroup)
    LinearLayout dotgroup;
    @BindView(R.id.graidview)
    GridView graidview;
    @BindView(R.id.wetherIcon)
    ImageView wetherIcon;
    @BindView(R.id.wetherShiText)
    TextView wetherShiText;
    @BindView(R.id.wetherText)
    TextView wetherText;
    @BindView(R.id.wetherTemtext)
    TextView wetherTemtext;
    @BindView(R.id.citytext)
    TextView citytext;
    @BindView(R.id.imgrefresh)
    ImageView imgrefresh;
    @BindView(R.id.img_setting)
    ImageView imgSetting;
    @BindView(R.id.mMarqueeView)
    MarqueeView mMarqueeView;


    // 需检查的全部敏感权限
    static final String[] PERMISSIONS = new String[]{
            "android.permission.CAMERA"
    };

    private MainPresenter mainPresenter;
    private int imgResIds[] = new int[]{R.mipmap.banner_01, R.mipmap.banner_02,
            R.mipmap.banner_03, R.mipmap.banner_01};
    //存储5张图片
    private String textview[] = new String[]{"", ""
            , "", ""};
    //存储5个目录
    private int curIndex = 0;
    //用来记录当前滚动的位置
    PicsAdapter picsAdapter;
    private Animation rotate;
    //图标
    private int imgSrc[] = new int[]{R.mipmap.main_1, R.mipmap.main_2, R.mipmap.main_3,
            R.mipmap.main_4,
            R.mipmap.main_5, R.mipmap.main_6, R.mipmap.main_7, R.mipmap.main_8};

    private String itemStr[] = new String[]{"日常检查", "移动考勤", "应急管理",
            "工作日志", "扫码检查", "出行导航",
            "设施查询", "路况提醒"};

    private ScheduledExecutorService scheduledExecutorService;
    private PermissionsChecker mPermissionsChecker;
    private static final int APP_HANDLER = 0x1;
    private static final long SPLASH_DELAY_MILLIS = 500;
    private static final int REQUEST_CODE = 0; // 请求码 请求权限
    private static final int REQUEST_CODE_CMERA = 3; // 请求码 请求权限
    private static final int APP_QUIT = 12;
    private static final int REQUEST_CODE_WRITE_SETTINGS = 1;//写入设置
    String category;
    private String usertype;
    private Intent intent;
    @Override
    protected int initContent() {
        return R.layout.activity_main;
    }

    /**
     *
     */
    @Override
    protected void initView() {
        String userid = (String) SPUtils.get(getApplicationContext(), SPConstant.USERID, "");
        usertype = (String) SPUtils.get(getApplicationContext(), SPConstant.USERTYPE, "");
        mMarqueeView.setText("生于夏花之绚烂，死于秋叶之静美");
        int px = OtherUtils.dip2px(this, 10);
        mMarqueeView.setPadding(0,25,0,px);

        //图片轮播图
        picsAdapter = new PicsAdapter(); // 创建适配器
        picsAdapter.setData(imgResIds);
        view_pager.setAdapter(picsAdapter); // 设置适配器
        view_pager.addOnPageChangeListener(new MyPageChangeListener());
        initPoints(imgResIds.length); // 初始化图片小圆点
        startAutoScroll(); // 开启自动播放
        //grideview
        MainGrideAdapter grideAdapter = new MainGrideAdapter(this, imgSrc, itemStr);
        graidview.setAdapter(grideAdapter);

        graidview.setOnItemClickListener(lisenter);
    }

    @Override
    protected void initData() {
        //先找ip
        if (UrlConstant.IP==null||UrlConstant.PORT==null){
            //设置ip
            DBManager<IPPath> dbManager = IPPathDb.getInstace().getDbManager();
            List<IPPath> ipPaths = dbManager.queryAllList(dbManager.getQueryBuiler());
            IPPath ipPath = ipPaths.get(0);
            UrlConstant.setIP(ipPath.getIp());
            UrlConstant.setPORT(ipPath.getPort());
        }

        //添加activity
        AppManager.getAppManager().addActivity(this);
        //获取定位城市
        String city = getLocationCity();
        Log.i("test","city:"+city);
        //获取天气
        mainPresenter.getWeather(city, "json", "94Tmshjhp03oul7xy95Gu3wwHkjGZvkk");
        rotate = AnimationUtils.loadAnimation(this, R.anim.roate_refresh);
        LinearInterpolator lin = new LinearInterpolator();
        rotate.setInterpolator(lin);

        //网络状态观察者
       NetChangeObserver netChangeObserver= new NetChangeObserver() {
            @Override
            public void onNetConnected(NetworkUtils.NetworkType type) {
                if (type== NetworkUtils.NetworkType.NETWORK_WIFI){
                    Intent intent = new Intent(MainActivity.this, UpLoadSercvice.class);
                    startService(intent);
                }

            }

            @Override
            public void onNetDisConnect() {
                Intent intent = new Intent(MainActivity.this, UpLoadSercvice.class);
                stopService(intent);
            }
        };
        //注册一个广播状态接收者
        NetCheckReceiver.registerObserver(netChangeObserver);



    }

    private String getLocationCity() {
        String city = (String) SPUtils.get(this, SPConstant.CITYNAME, "");

        if (!TextUtils.isEmpty(city)) {

        } else {
            city = "武汉";
        }
        citytext.setText(city);
        return city;
    }

    @Override
    protected MainPresenter initPresenter() {
        mainPresenter = new MainPresenter();
        return mainPresenter;
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //回复状态
        //scrollText.onRestoreInstanceState(parcelable);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMarqueeView.startScroll();
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void hintLoading() {

    }

    @Override
    public void showError() {

    }

    @Override
    public void setWeather(WeatherInfo weatherInfo) {
        Weather_data weather_data = weatherInfo.getResults().get(0).getWeather_data().get(0);

        //天气图片
        Glide.with(this).load(weather_data.getDayPictureUrl()).into(wetherIcon);
        //实时温度
        //获取当天温度
        String dataWen = weather_data.getDate();
        int left = dataWen.indexOf('：');
        int right=dataWen.indexOf(')');
        //截取
        String subdataWen = dataWen.substring(left+1, right);
        wetherShiText.setText(subdataWen);
        //实时天气
        wetherText.setText(weather_data.getWeather());
        //温度区间
        wetherTemtext.setText(weather_data.getTemperature());
    }


    /**
     * @param count
     */
    // 初始化图片轮播的小圆点和目录
    private void initPoints(int count) {
        int px = OtherUtils.dip2px(this, 7);

        for (int i = 0; i < count; i++) {

            ImageView iv = new ImageView(this);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                    px, px);
            params.setMargins(0, 0, px, 0);
            iv.setLayoutParams(params);

            iv.setImageResource(R.mipmap.dot1);

            dotgroup.addView(iv);

        }
        ((ImageView) dotgroup.getChildAt(curIndex))
                .setImageResource(R.mipmap.dot2);

        newsTitle.setText(textview[curIndex]);
    }

    // 自动播放
    private void startAutoScroll() {
        scheduledExecutorService = Executors
                .newSingleThreadScheduledExecutor();
        // 每隔4秒钟切换一张图片
        ViewPagerTask pagerTask = new ViewPagerTask();
        scheduledExecutorService.scheduleWithFixedDelay(pagerTask, 5,
                4, TimeUnit.SECONDS);
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    imgrefresh.clearAnimation();
                    ToastUtils.show(MainActivity.this, "天气已刷新");
                    break;
                default:
                    break;
            }
        }
    };


    @OnClick({R.id.img_setting, R.id.imgrefresh})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_setting:
                Intent intent = new Intent(this, LogoutActivity.class);
                startActivity(intent);
                break;
            case R.id.imgrefresh:
                //刷新
                //imgrefresh.startAnimation(rotate);
                if (rotate != null) {
                    imgrefresh.startAnimation(rotate);
                    handler.sendEmptyMessageDelayed(0, 2000);
                }
                break;
            default:
                break;
        }
    }




    /**
     * 首页轮播图adapter
     */
    class PicsAdapter extends PagerAdapter {

        private List<ImageView> views = new ArrayList<ImageView>();

        @Override
        public int getCount() {
            if (views == null) {
                return 0;
            }
            return views.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        public void setData(int[] imgResIds) {
            for (int i = 0; i < imgResIds.length; i++) {
                ImageView iv = new ImageView(MainActivity.this);
                ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT);
                iv.setLayoutParams(params);
                iv.setScaleType(ImageView.ScaleType.FIT_XY);
                //设置ImageView的属性
                iv.setImageResource(imgResIds[i]);
                views.add(iv);
            }
        }

        public Object getItem(int position) {
            if (position < getCount())
                return views.get(position);
            return null;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            if (position < views.size())
                ((ViewPager) container).removeView(views.get(position));
        }

        @Override
        public int getItemPosition(Object object) {
            return views.indexOf(object);
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            if (position < views.size()) {
                final ImageView imageView = views.get(position);
                ((ViewPager) container).addView(imageView);
                return views.get(position);
            }
            return null;
        }
    }


    // 切换图片任务
    private class ViewPagerTask implements Runnable {
        @Override
        public void run() {

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    int count = picsAdapter.getCount();
                    view_pager.setCurrentItem((curIndex + 1) % count);
                }
            });
        }
    }

    // 定义ViewPager控件页面切换监听器
    class MyPageChangeListener implements ViewPager.OnPageChangeListener {

        @Override
        public void onPageScrolled(int position, float positionOffset,
                                   int positionOffsetPixels) {
        }

        @Override
        public void onPageSelected(int position) {
            ImageView imageView1 = (ImageView) dotgroup.getChildAt(position);
            ImageView imageView2 = (ImageView) dotgroup.getChildAt(curIndex);
            if (imageView1 != null) {
                imageView1.setImageResource(R.mipmap.dot2);
            }
            if (imageView2 != null) {
                imageView2.setImageResource(R.mipmap.dot1);
            }
            curIndex = position;
            newsTitle.setText(textview[curIndex]);
        }

        boolean b = false;

        @Override
        public void onPageScrollStateChanged(int state) {
            //这段代码可不加，主要功能是实现切换到末尾后返回到第一张
            switch (state) {
                case 1:// 手势滑动
                    b = false;
                    break;
                case 2:// 界面切换中
                    b = true;
                    break;
                case 0:// 滑动结束，即切换完毕或者加载完毕
                    // 当前为最后一张，此时从右向左滑，则切换到第一张
                    if (view_pager.getCurrentItem() == view_pager.getAdapter()
                            .getCount() - 1 && !b) {
                        view_pager.setCurrentItem(0);
                    }
                    // 当前为第一张，此时从左向右滑，则切换到最后一张
                    else if (view_pager.getCurrentItem() == 0 && !b) {
                        view_pager.setCurrentItem(view_pager.getAdapter()
                                .getCount() - 1);
                    }
                    break;

                default:
                    break;
            }
        }
    }


    /**
     * grdiview 点击监听
     */
    AdapterView.OnItemClickListener lisenter = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            switch (position) {
                //日常管理
                case 0:
                    if ("3".equals(usertype)) {
                        Intent dailyintent = new Intent(MainActivity.this, DailyCheckActivity.class);
                        dailyintent.putExtra("isadmin","1");
                        dailyintent.putExtra("isshow","6");
                        startActivity(dailyintent);
                    } else {
                        Intent intent = new Intent(MainActivity.this, DailyCheckServiceareaProjectFillActivity.class);
                        startActivity(intent);
                    }
                    break;
                //移动考勤
                case 1:
                    Intent intent = new Intent(MainActivity.this, MobileSignActivity.class);
                    startActivity(intent);
                    break;
                //应急管理

                //工作日志
                case 3:
                    Intent logmanageintent = new Intent(MainActivity.this, LogManageActivity.class);
                    logmanageintent.putExtra("tempflag", "1");
                    startActivity(logmanageintent);
                    break;
                //扫码检查
                case 4:
                    //首先判断权限
                    initPermission();
                    break;
                case 5:
                    //首先判断权限
                    Intent intenttest = new Intent(MainActivity.this, TestActivity.class);
                    startActivity(intenttest);
                    break;
                default:
                    ToastUtils.show(MainActivity.this,"暂未开放此功能！");
                    break;

            }
        }
    };
    /**
     * 重写返回键退出程序
     */
    // 定义一个变量，来标识是否退出
    private static boolean isExit = false;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    int i = 1;
    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case APP_QUIT:
                    isExit = false;
                    break;
                case APP_HANDLER:

                    Intent intent = new Intent(MainActivity.this, CaptureActivity.class);
                    startActivityForResult(intent, REQUEST_CODE_CMERA);
                    //ToastUtils.show(MainActivity.this,"可以拍照！");
                    break;
            }

        }
    };

    private void exit() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(getApplicationContext(), "再按一次返回键退出应用",
                    Toast.LENGTH_SHORT).show();
            // 利用handler延迟发送更改状态信息
            mHandler.sendEmptyMessageDelayed(APP_QUIT, 2000);
        } else {
            AppManager.getAppManager().finishAllActivity();
            System.exit(0);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mMarqueeView!=null){
            mMarqueeView.stopScroll();
        }
        //停止执行任务
        scheduledExecutorService.shutdown();
        AppManager.getAppManager().finishActivity(this);
    }


    /**
     * 检查敏感权限
     */
    private void initPermission() {
        mPermissionsChecker = new PermissionsChecker(this);
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            //请求
            startPermissionsActivity();
            //
        } else {
            mHandler.sendEmptyMessageDelayed(APP_HANDLER, SPLASH_DELAY_MILLIS);
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            ToastUtils.showSure(this, "缺少主要权限，可能影响某些功能的使用！");

        } else if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
//            if (Settings.System.canWrite(this)) {
//                Log.i("test", "onActivityResult write settings granted");
//            } else {
//                Log.i("test", "onActivityResult write settings unGranted");
//            }

            mHandler.sendEmptyMessageDelayed(APP_HANDLER, SPLASH_DELAY_MILLIS);
        } else {
            if (requestCode == REQUEST_CODE) {
                mHandler.sendEmptyMessageDelayed(APP_HANDLER, SPLASH_DELAY_MILLIS);
            }

        }

        //扫描结果回调
        if (requestCode == REQUEST_CODE_CMERA && data != null) { //RESULT_OK = -1
            AppManager.getAppManager().finishActivity(CaptureActivity.class);
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString("qr_scan_result");
            Log.i("test", "结果：" + scanResult);
            try {
                QrCodeBean qrCodeBean = new Gson().fromJson(scanResult, QrCodeBean.class);
                //跳转到检查页面
                Intent intent = new Intent(this, DailyCheckActivity.class);
                intent.putExtra("codeBean", qrCodeBean);
                startActivity(intent);
            } catch (Exception e) {
                ToastUtils.show(this, "非检查二维码！请重新扫描");
            }
        }
    }
}
