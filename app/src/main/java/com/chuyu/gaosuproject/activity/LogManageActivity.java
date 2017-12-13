package com.chuyu.gaosuproject.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;

import com.chuyu.gaosuproject.util.SVP.SVProgressHUD;
import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.util.NetworkUtils;
import com.chuyu.gaosuproject.util.OtherUtils;
import com.chuyu.gaosuproject.util.SPUtils;
import com.chuyu.gaosuproject.util.ToastUtils;


import android.widget.PopupWindow.OnDismissListener;
import android.support.v4.view.ViewPager.LayoutParams;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 日志管理
 */
public class LogManageActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.logmanage_webview)
    WebView logmanageWebview;
    String path = "";
    String currentDate;
    @BindView(R.id.ll_back)
    LinearLayout llBack;
    @BindView(R.id.addImg)
    ImageView addImg;
    private String userid;
    private String username;
    private String currenttime;
    private String strcurrenttime;
    private String tempflag;
    SVProgressHUD svProgressHUD;
    //管理员
    public String managename;
    public String managesenddatetime;
    //水电工
    public String electricianname;
    public String electriciansenddatetime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_manage);
        ButterKnife.bind(this);
        Log.i("test", "11111111111111");
        svProgressHUD = new SVProgressHUD(this);
        svProgressHUD.showWithStatus("请稍等...");
        userid = (String) SPUtils.get(this, SPConstant.USERID, "");
        username = (String) SPUtils.get(this, SPConstant.USERNAME, "");
        currenttime = OtherUtils.GetcurrentTime();
        strcurrenttime = currenttime.substring(0, 10);
        Log.i("test", "当前时间:" + currenttime);
        Log.i("test", "username:" + username);
        Log.i("test", "userid:" + userid);
        tempflag = getIntent().getStringExtra("tempflag");
        llBack.setOnClickListener(this);
        addImg.setOnClickListener(this);
        logmanageWebview.getSettings().setJavaScriptEnabled(true);
        logmanageWebview.getSettings().setDefaultTextEncodingName("utf-8");
        //主要处理js对话框、图标、页面标题等
        logmanageWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    handler.sendEmptyMessage(1);// 如果全部载入,隐藏进度对话框
                }
                super.onProgressChanged(view, newProgress);
                // 载入进度改变而触发
            }
        });
        logmanageWebview.addJavascriptInterface(this, "jsback");
        path = UrlConstant.HTTP+UrlConstant.getIP()+":"+UrlConstant.getPORT();
        //关闭html默认打开方式选择
        logmanageWebview.setWebViewClient(new MyWebViewClient());
        logmanageWebview.loadUrl("file:///android_asset/html/log-detail.html");
        handler.sendEmptyMessageDelayed(0, 3000);

        Log.i("test","path:"+path);
    }

    Handler handler = new Handler() {
        public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
            if (!Thread.currentThread().isInterrupted()) {
                switch (msg.what) {
                    case 0:
                        svProgressHUD.dismissImmediately();
                        // pd.show();// 显示进度对话框
                        break;
                    case 1:
                        svProgressHUD.dismissImmediately();
                        //  pd.hide();// 隐藏进度对话框，不可使用dismiss()、cancel(),否则再次调用show()时，显示的对话框小圆圈不会动。
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };

    //通过tempflag判断加载管理员日志和水电工日志
    @JavascriptInterface
    public String backtempflag() {
        return tempflag;
    }

    //返回用户ID
    @JavascriptInterface
    public String GetUserID() {
        return userid;
    }

    //得到接口全路径和端口
    @JavascriptInterface
    public String GetDate() {
        return path;
    }

    @JavascriptInterface
    public void showData(String string) {
        ToastUtils.show(this, "js传参" + string);
    }

    //通过flag标记判断跳转到上报管理员日志界面
    //获取js传过来的username和time来判断是否跳转上报界面
    @JavascriptInterface
    public void showmanagelog(String name, String senddatetime) {
        Log.i("test", name + "        " + senddatetime);
        managename = name;
        managesenddatetime = senddatetime;
    }

    //通过flag标记判断跳转到上报水电工日志界面
    //获取js传过来的username和time来判断是否跳转上报界面
    @JavascriptInterface
    public void showwaterelectrician(String name, String senddatetime) {
        electricianname = name;
        electriciansenddatetime = senddatetime;
    }

    //通过flag判断修改水电工日志和管理员日志
    @JavascriptInterface
    public void getupdatelogmessage(String flag, String workdiaryid, String authoruserid, String finishwork, String unnfinishwork, String needassistwork, String remark, String creatime) {
        Log.i("listPath", "flag:" + flag + "\n" + "workdiaryid:" + workdiaryid + "\n" + "authoruserid:" + authoruserid + "\n" + "remark:" + remark + "\n" + "creatime:" + creatime);

        if (flag.equals("1")) {
            Log.i("listPath", "传输正确");
            Intent intentmanage = new Intent(getApplicationContext(), AddManagerLogActivity.class);
            intentmanage.putExtra("workdiaryid", workdiaryid);
            intentmanage.putExtra("authoruserid", authoruserid);
            intentmanage.putExtra("finishwork", finishwork);
            intentmanage.putExtra("unnfinishwork", unnfinishwork);
            intentmanage.putExtra("needassistwork", needassistwork);
            intentmanage.putExtra("remark", remark);
            intentmanage.putExtra("creatime", creatime);
            intentmanage.putExtra("flag", "two");
            startActivity(intentmanage);
        } else if (flag.equals("2")) {
            Log.i("listPath", "传输正确2");
            Intent intentwater = new Intent(getApplicationContext(), AddWaterElectricianActivity.class);
            intentwater.putExtra("workdiaryid", workdiaryid);
            intentwater.putExtra("authoruserid", authoruserid);
            intentwater.putExtra("finishwork", finishwork);
            intentwater.putExtra("unnfinishwork", unnfinishwork);
            intentwater.putExtra("needassistwork", needassistwork);
            intentwater.putExtra("remark", remark);
            intentwater.putExtra("creatime", creatime);
            intentwater.putExtra("tag", "two");
            startActivity(intentwater);
        }

    }

    //返回上一个界面
    @JavascriptInterface
    public void showback() {
        finish();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.ll_back:
                finish();
                break;
            case R.id.addImg:
                showlogPopupWindow();
                break;
        }
    }

    private void showlogPopupWindow() {
        View inflate = LayoutInflater.from(LogManageActivity.this).inflate(
                R.layout.log_popup_dialog, null);
        final PopupWindow mPopWindow = new PopupWindow(inflate,
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT, true);
        // 需要设置一下此参数，点击外边可消失
        mPopWindow.setBackgroundDrawable(new BitmapDrawable());
        // 设置点击窗口外边窗口消失
        mPopWindow.setOutsideTouchable(true);
        // 设置此参数获得焦点，否则无法点击
        mPopWindow.setFocusable(true);
        mPopWindow.setContentView(inflate);
        //点击事件
        LinearLayout layout_addmanagelog = (LinearLayout) inflate
                .findViewById(R.id.layout_addmanagelog);
        LinearLayout layout_addelectricianlog = (LinearLayout) inflate
                .findViewById(R.id.layout_addelectricianlog);
        layout_addmanagelog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (TextUtils.isEmpty(managename) && TextUtils.isEmpty(managesenddatetime)) {
                    Log.i("test", "管理员");
                    Intent intent = new Intent(getApplicationContext(), AddManagerLogActivity.class);
                    intent.putExtra("flag", "one");
                    startActivity(intent);
//                    finish();

                } else {
                    if (username.equals(managename) || managesenddatetime.equals(strcurrenttime)) {
                        ToastUtils.show(getApplicationContext(), "您今天已写过日志了!");
                    }
                }
                mPopWindow.dismiss();
            }
        });
        layout_addelectricianlog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(electricianname) && TextUtils.isEmpty(electriciansenddatetime)) {
                    Intent intent = new Intent(getApplicationContext(), AddWaterElectricianActivity.class);
                    intent.putExtra("tag", "one");
                    startActivity(intent);
//                    finish();

                } else {
                    if (username.equals(electricianname) || electriciansenddatetime.equals(strcurrenttime)) {
                        ToastUtils.show(getApplicationContext(), "您今天已写过日志了!");
                    }
                }
                mPopWindow.dismiss();
            }
        });
        // 显示PopupWindow
        mPopWindow.showAsDropDown(addImg, 0, 10);
        // 设置颜色变暗
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = 0.7f;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);//多加这一句，问题就解决了！这句的官方文档解释是：让窗口背景后面的任何东西变暗
        getWindow().setAttributes(lp);
        //此处作为点击PopupWindow之外的地方取消掉背景变暗
        mPopWindow.setOnDismissListener(new OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow().getAttributes();
                lp.alpha = 1f;
                getWindow().setAttributes(lp);
            }
        });
    }

    /**
     * 自定义WebView类，重载shouldOverrideUrlLoading，改变打开方式
     */
    private class MyWebViewClient extends WebViewClient {
        //        @Override
//        public boolean shouldOverrideUrlLoading(WebView view, String url) {
//            view.loadUrl(url);
//            return true;
//        }
        //加载
        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }

        //关闭
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
        }
    }

}
