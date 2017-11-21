package com.chuyu.gaosuproject.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;

import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.util.SPUtils;
import com.chuyu.gaosuproject.util.ToastUtils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * app 程序启动引导页
 */
public class GuideActivity extends AppCompatActivity {


    @BindView(R.id.guide_page)
    ViewPager guidePage;
    @BindView(R.id.bt_login_guide)
    Button btLoginGuide;


    private List<View> viewList;
    private static final int GUIDE_PAGE_COUNT = 3;
    private int[] imgArr = new int[]{R.mipmap.guide1, R.mipmap.guide2, R.mipmap.guide3};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //设置状态栏透明
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            setTranslucentStatus(true);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            this.getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        //改变状态栏颜色
        MIUISetStatusBarLightMode(getWindow(), true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);

        initData();
        initView();
    }

    /**
     * 设置状态栏透明
     */

    @TargetApi(19)
    protected void setTranslucentStatus(boolean on) {
        //状态栏透明
        // 4.4以上系统状态栏透明
        Window win = getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        final int bits = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    //完成各页面初始化数据添加
    private void initData() {
        //引导页 下部的图片示意点
        viewList = new ArrayList<View>(GUIDE_PAGE_COUNT);
        for (int i = 0; i < GUIDE_PAGE_COUNT; i++) {
            //界面填充
            View view = LayoutInflater.from(this).inflate(R.layout.item_gudie_imag, null);
            //界面设置背景
            view.setBackgroundResource(R.color.colorWihte);
            //找到imgview  并设置图片
            ((ImageView) view.findViewById(R.id.page_img)).setBackgroundResource(imgArr[i]);
            //集合中添加界面
            viewList.add(view);

        }
    }

    //页面数目
    private int currentPosition = 0;

    private void initView() {
        guidePage.setAdapter(new PagerAdapter() {
            @Override
            public int getCount() {
                return viewList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                //移除
                container.removeView(viewList.get(position));
            }

            //显示
            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(viewList.get(position));
                return viewList.get(position);
            }
        });
        //添加页面改变监听
        guidePage.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {


            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                currentPosition = position;

            }

            @Override
            public void onPageSelected(int position) {
                //setIndicator(position);
                //第三个bt显示
                if (position==2){
                    btLoginGuide.setVisibility(View.VISIBLE);
                }else {
                    btLoginGuide.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        guidePage.setOnTouchListener(new View.OnTouchListener() {
            //几个参数
            float startX;
            float startY;
            float endX;
            float endY;

            @Override
            public boolean onTouch(View v, MotionEvent event) {

                switch (event.getAction()) {
                    //点击
                    case MotionEvent.ACTION_DOWN:
                        startX = event.getX();
                        startY = event.getY();
                        break;
                    //滑动
                    case MotionEvent.ACTION_MOVE:
                        break;
                    //离开
                    case MotionEvent.ACTION_UP:
                        endX = event.getX();
                        endY = event.getY();
                        //窗口管理者
                        WindowManager windowManager = (WindowManager) getApplicationContext().getSystemService(WINDOW_SERVICE);
                        //获取屏幕宽度
                        Point point = new Point();
                        //走一遭
                        windowManager.getDefaultDisplay().getSize(point);
                        int width = point.x;
                        //首先要确定的是，是否到了最后一页，
                        // 然后判断是否向左滑动，并且滑动距离是否符合，
                        // 我这里的判断距离是屏幕宽度的4分之一（这里可以适当控制）
                        //Log.i("test","startX:"+startX+"\n"+"startY:"+startY+"\n"+"endX:"+endX+"\n"+"endY:"+endY);
                        if (currentPosition == imgArr.length - 1 && startX - endX >= (width / 6)) {
                            //  Log.i("test","进入了触摸");
                            //gotoMainActivity();
                            //页面翻动效果
                            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_in_left);
                        }

                        break;
                }
                return false;
            }
        });


    }

    private void gotoMainActivity() {
        //设置是否第一次进入
        SPUtils.put(this, SPConstant.FIRST_JOIN, false);
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    /**
     * 设置状态栏字体图标为深色，需要MIUIV6以上
     *
     * @param window 需要设置的窗口
     * @param dark   是否把状态栏字体及图标颜色设置为深色
     * @return boolean 成功执行返回true
     */
    public static boolean MIUISetStatusBarLightMode(Window window, boolean dark) {
        boolean result = false;
        if (window != null) {
            Class clazz = window.getClass();
            try {
                int darkModeFlag = 0;
                Class layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
                Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
                darkModeFlag = field.getInt(layoutParams);
                Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
                if (dark) {
                    extraFlagField.invoke(window, darkModeFlag, darkModeFlag);//状态栏透明且黑色字体
                } else {
                    extraFlagField.invoke(window, 0, darkModeFlag);//清除黑色字体
                }
                result = true;
            } catch (Exception e) {

            }
        }
        return result;
    }


    @OnClick(R.id.bt_login_guide)
    public void onClick() {
        //ToastUtils.show(this,"点击登录");
        gotoMainActivity();
    }
}
