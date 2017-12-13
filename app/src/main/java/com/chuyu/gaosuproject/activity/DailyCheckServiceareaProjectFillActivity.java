package com.chuyu.gaosuproject.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chuyu.gaosuproject.util.SVP.SVProgressHUD;
import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.util.SPUtils;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 日常检查服务区项目上报
 */
public class DailyCheckServiceareaProjectFillActivity extends AppCompatActivity {

    @BindView(R.id.servicearea_webview)
    WebView serviceareaWebview;
    private String userid;
    private String username;
    private String path;
    //    private String jsuserid;
    SVProgressHUD svProgressHUD;
    private String isshow = "1";
    private String usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_check_servicearea_project_fill);
        ButterKnife.bind(this);
        iniDate();
    }

    private void iniDate() {
        svProgressHUD = new SVProgressHUD(this);
        svProgressHUD.showWithStatus("正在加载...");
        path = UrlConstant.HTTP+UrlConstant.getIP()+":"+UrlConstant.getPORT();
        username = (String) SPUtils.get(getApplicationContext(), SPConstant.USERNAME, "");
        userid = (String) SPUtils.get(getApplicationContext(), SPConstant.USERID, "");
        usertype = (String) SPUtils.get(getApplicationContext(), SPConstant.USERTYPE, "");
        serviceareaWebview.getSettings().setJavaScriptEnabled(true);
        serviceareaWebview.getSettings().setDefaultTextEncodingName("utf-8");
        serviceareaWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    handler.sendEmptyMessage(1);// 如果全部载入,隐藏进度对话框
                }
                super.onProgressChanged(view, newProgress);
                // 载入进度改变而触发
            }
        });
        serviceareaWebview.setWebViewClient(new MyWebViewClient());
//        jsuserid = "5662DB0D-A5B8-4F36-9BFF-5EF77B65828E";
        serviceareaWebview.addJavascriptInterface(new ServiceareaFill(), "jsproject");
        serviceareaWebview.loadUrl("file:///android_asset/html/checkMainadd.html");
        handler.sendEmptyMessageDelayed(0, 3000);
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

    class ServiceareaFill {
        @JavascriptInterface
        public String backpath() {
            return path;
        }

        @JavascriptInterface
        public String backUsername() {
            return username;
        }

        @JavascriptInterface
        public void onbackup() {
            finish();
        }

        @JavascriptInterface
        public void StartActivity() {
            Intent intents = new Intent(getApplicationContext(), DailyCheckProjectquery.class);
            intents.putExtra("plusishow","1");
            startActivity(intents);
        }

        @JavascriptInterface
        public String showType() {
            return usertype;
        }

        @JavascriptInterface
        public String getUserID() {
            return userid;
        }

        @JavascriptInterface
        public void showMessage(String deptcode, String curType, String showType, String CheckDate, String CheckId, String DeptName) {
            Log.i("test", curType + "\n" + DeptName + "\n" + CheckId);

            Intent intent = new Intent(getApplicationContext(), DailyCheckActivity.class);
            intent.putExtra("flag", "1");
            intent.putExtra("Deptcode", deptcode);
            intent.putExtra("curType", curType);
            intent.putExtra("showType", showType);
            intent.putExtra("CheckDate", CheckDate);
            intent.putExtra("CheckId", CheckId);
            intent.putExtra("DeptName", DeptName);
            intent.putExtra("isshow", isshow);
            startActivity(intent);
        }
    }
}
