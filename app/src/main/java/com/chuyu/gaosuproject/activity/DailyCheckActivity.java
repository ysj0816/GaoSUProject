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
import com.chuyu.gaosuproject.bean.QrCodeBean;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.util.DateUtils;
import com.chuyu.gaosuproject.util.SPUtils;


import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 日常检查
 */
public class DailyCheckActivity extends AppCompatActivity {

    @BindView(R.id.dailycheck_webview)
    WebView dailycheckWebview;
    private String userid;
    //    String jsuserid = "";
    private String showType;
    private String curType;
    private String checkDate;
    private String checkId;
    private String deptName;
    private String deptCode;
    private boolean isLoad = false;
    private String path;
    SVProgressHUD svProgressHUD;
    private String isadminn;
    private String isshow;
    private String usertype;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("test", "oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_check);
        ButterKnife.bind(this);
        svProgressHUD = new SVProgressHUD(this);
        svProgressHUD.showWithStatus("正在加载...");
        path = UrlConstant.HTTP+UrlConstant.getIP()+":"+UrlConstant.getPORT();
        userid = (String) SPUtils.get(getApplicationContext(), SPConstant.USERID, "");
        usertype = (String) SPUtils.get(getApplicationContext(), SPConstant.USERTYPE, "");
        dailycheckWebview.getSettings().setJavaScriptEnabled(true);
        dailycheckWebview.getSettings().setDefaultTextEncodingName("utf-8");
        dailycheckWebview.setWebChromeClient(new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    handler.sendEmptyMessage(1);// 如果全部载入,隐藏进度对话框
                }
                super.onProgressChanged(view, newProgress);
                // 载入进度改变而触发
            }
        });
        dailycheckWebview.setWebViewClient(new MyWebViewClient());

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


    @Override
    protected void onResume() {
        super.onResume();
        isadminn = getIntent().getStringExtra("isadmin");
        isshow = getIntent().getStringExtra("isshow");

        Log.i("test", "isshow:" + isshow);
        dailycheckWebview.addJavascriptInterface(new show(), "jsresult");
        QrCodeBean codeBean = (QrCodeBean) getIntent().getSerializableExtra("codeBean");

        if (codeBean != null) {
            deptCode = codeBean.getD();
            showType = "2";
            curType = codeBean.getT();
            checkDate = DateUtils.getSystemDate();
            checkId = codeBean.getP();
            Log.i("test", "checkId:" + checkId);
            dailycheckWebview.loadUrl("file:///android_asset/html/checkIndexQrcode.html");
        } else {
            String flag = getIntent().getStringExtra("flag");
            //日常检查表服务区上报
            if ("1".equals(flag)) {
                deptCode = getIntent().getStringExtra("Deptcode");
                showType = getIntent().getStringExtra("showType");
                curType = getIntent().getStringExtra("curType");
                checkDate = getIntent().getStringExtra("CheckDate");
                checkId = getIntent().getStringExtra("CheckId");
                deptName = getIntent().getStringExtra("DeptName");
                Log.i("curType", "curType:" + curType);
                dailycheckWebview.loadUrl("file:///android_asset/html/checkIndex.html");
            }
            //日常检查详情查看
            else if ("2".equals(flag)) {
                checkDate = getIntent().getStringExtra("CheckDate");
                checkId = getIntent().getStringExtra("CheckId");
                deptCode = getIntent().getStringExtra("DeptCode");
                deptName = getIntent().getStringExtra("DeptName");
                showType = getIntent().getStringExtra("showType");
                curType = getIntent().getStringExtra("curType");
                dailycheckWebview.loadUrl("file:///android_asset/html/checkIndex.html");
            }
            dailycheckWebview.loadUrl("file:///android_asset/html/checkIndex.html");
            Log.i("test", "onResume");
        }
        handler.sendEmptyMessageDelayed(0, 3000);
    }


    /**
     * 自定义WebView类，重载shouldOverrideUrlLoading，改变打开方式
     */
    class MyWebViewClient extends WebViewClient {
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

    private class show {
        @JavascriptInterface
        public String backPath() {
            return path;
        }

        @JavascriptInterface
        public void onbackup() {
            finish();
        }

        @JavascriptInterface
        public String getUserid() {
            return userid;
        }

        @JavascriptInterface
        public void isShow(String data) {
            Log.i("test", "从js传过来的：" + data);

        }

        @JavascriptInterface
        public String showusertype() {
            return usertype;
        }

        @JavascriptInterface
        public String isadmin() {
            return isadminn;
        }

        @JavascriptInterface
        public String isshow() {
            return isshow;
        }

        @JavascriptInterface
        public void JumpActivity() {
            Intent intent = new Intent(DailyCheckActivity.this, DailyCheckProjectquery.class);
            intent.putExtra("plusishow", isshow);
            startActivity(intent);
        }

        @JavascriptInterface
        public String ShowType() {
            return showType;
        }

        @JavascriptInterface
        public String ShowcurType() {
            Log.i("test", "传给:" + curType);
            return curType;
        }

        @JavascriptInterface
        public String ShowQrcode() {
            return UrlConstant.formatUrl(UrlConstant.QRECODEURL);
        }

        @JavascriptInterface
        public String ShowcheckDate() {
            return checkDate;
        }

        @JavascriptInterface
        public String ShowcheckId() {
            return checkId;
        }

        @JavascriptInterface
        public String ShowdeptName() {
            return deptName;
        }

        @JavascriptInterface
        public String ShowdeptCode() {
            return deptCode;
        }

        @JavascriptInterface
        public void getCheckMessage(String curType, String showType, String checkId, String checkUnitName
                , String checkUnitId, String checkProjectName, String checkProjectId
                , String DeptCode, String CheckDate, String DeptName) {
            Intent intent = new Intent(getApplicationContext(), DailyCheckListFillActivity.class);
            intent.putExtra("checkUnitName", checkUnitName);
            intent.putExtra("checkProjectName", checkProjectName);
            intent.putExtra("checkId", checkId);
            intent.putExtra("checkUnitId", checkUnitId);
            intent.putExtra("checkProjectId", checkProjectId);
            startActivity(intent);
        }

        @JavascriptInterface
        public void LogStr(String arr) {
            Log.i("test", "arr:" + arr);
        }

    }


}


