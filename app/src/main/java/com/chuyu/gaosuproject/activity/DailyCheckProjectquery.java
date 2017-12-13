package com.chuyu.gaosuproject.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.chuyu.gaosuproject.util.SVP.SVProgressHUD;
import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.util.SPUtils;
import com.chuyu.gaosuproject.util.SystemBarTintManager;
import com.chuyu.gaosuproject.util.SystemStatusBar;
import com.chuyu.gaosuproject.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 日常检查公益项目和经营项目查询
 */
public class DailyCheckProjectquery extends AppCompatActivity {

    @BindView(R.id.dailycheckprojectquery_webview)
    WebView dailycheckprojectqueryWebview;
    private String userid;
    private String path;
    SVProgressHUD svProgressHUD;
    private String plusishow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_check_projectquery);
        ButterKnife.bind(this);
        initDate();
    }

    private void initDate() {
        svProgressHUD = new SVProgressHUD(this);
        svProgressHUD.showWithStatus("正在加载...");
        plusishow = getIntent().getStringExtra("plusishow");
        path = UrlConstant.HTTP+UrlConstant.getIP()+":"+UrlConstant.getPORT();
        userid = (String) SPUtils.get(getApplicationContext(), SPConstant.USERID, "");
        dailycheckprojectqueryWebview.getSettings().setJavaScriptEnabled(true);
        dailycheckprojectqueryWebview.getSettings().setDefaultTextEncodingName("utf-8");
        dailycheckprojectqueryWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    handler.sendEmptyMessage(1);// 如果全部载入,隐藏进度对话框
                }
                super.onProgressChanged(view, newProgress);
                // 载入进度改变而触发
            }
        });
        dailycheckprojectqueryWebview.setWebViewClient(new MyWebViewClient());
//       String  jsuserid = "5662DB0D-A5B8-4F36-9BFF-5EF77B65828E";
        dailycheckprojectqueryWebview.addJavascriptInterface(new Projectquery(), "jsproject");
        dailycheckprojectqueryWebview.loadUrl("file:///android_asset/html/check.html");
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

    class Projectquery {
        @JavascriptInterface
        public String backPath() {
            return path;
        }

        @JavascriptInterface
        public String plusishow() {
            return plusishow;
        }

        @JavascriptInterface
        public void onbackup() {
            finish();
        }

        @JavascriptInterface
        public String getUserID() {
            return userid;
        }

        @JavascriptInterface
        public void Jump() {
            Intent intetn = new Intent(DailyCheckProjectquery.this, DailyCheckServiceareaProjectFillActivity.class);
            startActivity(intetn);
        }

        @JavascriptInterface
        public void setDetailData(String CheckDate, String CheckId, String DeptCode, String DeptName
                , String showType, String curType) {
            Intent intent = new Intent(getApplicationContext(), DailyCheckActivity.class);
            intent.putExtra("flag", "2");
            intent.putExtra("CheckDate", CheckDate);
            intent.putExtra("CheckId", CheckId);
            intent.putExtra("DeptCode", DeptCode);
            intent.putExtra("DeptName", DeptName);
            intent.putExtra("showType", showType);
            intent.putExtra("curType", curType);
            intent.putExtra("isadmin", "2");
            startActivity(intent);
        }
    }


}
