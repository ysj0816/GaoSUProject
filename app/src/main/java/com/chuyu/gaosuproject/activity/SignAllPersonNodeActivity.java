package com.chuyu.gaosuproject.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.chuyu.gaosuproject.util.SVP.SVProgressHUD;
import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.util.SPUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 所有人员考勤记录
 */
public class SignAllPersonNodeActivity extends AppCompatActivity {

    @BindView(R.id.webview)
    WebView webview;
    private SVProgressHUD svProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_all_person_node);
        ButterKnife.bind(this);
        initData();
        initView();
    }

    private void initView() {
        svProgressHUD.showWithStatus("请稍等...");
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDefaultTextEncodingName("utf-8");
        //主要处理js对话框、图标、页面标题等
        webview.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                if (newProgress == 100) {
                    handler.sendEmptyMessage(1);// 如果全部载入,隐藏进度对话框
                }
                super.onProgressChanged(view, newProgress);
                // 载入进度改变而触发
            }
        });
        //主要用于处理webView的控制问题，如加载、关闭、错误处理等
        webview.setWebViewClient(new WebViewClient(){
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
        });
        webview.addJavascriptInterface(this,"JavaScriptInterface");
        webview.loadUrl("file:///android_asset/html/sign_allPerson.html");
        handler.sendEmptyMessageDelayed(0,15000);
    }

    protected void initData() {
        //根据不同的值加载不同的界面
        Intent intent = getIntent();
        //flag用来标记需要跳转的html页面
        //flag = intent.getStringExtra("flag");
        svProgressHUD = new SVProgressHUD(this);
    }
    Handler handler = new Handler() {
        public void handleMessage(Message msg) {// 定义一个Handler，用于处理下载线程与UI间通讯
            if (!Thread.currentThread().isInterrupted()){
                switch (msg.what) {
                    case 0:

                        break;
                    case 1:
                        break;
                    case 2:
                        svProgressHUD.showWithStatus("请稍等...");
                        break;
                    case 3:
                        svProgressHUD.dismissImmediately();
                        break;
                }
            }
            super.handleMessage(msg);
        }
    };

    @android.webkit.JavascriptInterface
    public void showToast(String toast) {
        //Toast.makeText(this, toast, Toast.LENGTH_SHORT).show();
        Log.i("test",toast);
    }

    @android.webkit.JavascriptInterface
    public void getH5(String str){
        svProgressHUD.dismissImmediately();
    }
    @android.webkit.JavascriptInterface
    public String getUserId(){
        String userid = (String) SPUtils.get(this, SPConstant.USERID, "");
        Log.i("test","调用此方法:"+userid);
        return userid;
    }
    @android.webkit.JavascriptInterface
    public void setShowPB() {

        handler.sendEmptyMessage(2);// 如果全部载入,隐藏进度对话框
    }
    @android.webkit.JavascriptInterface
    public void setHintPB() {

        handler.sendEmptyMessage(3);// 如果全部载入,隐藏进度对话框
    }
    @android.webkit.JavascriptInterface
    public String getIp(){
        String url = UrlConstant.formatUrl(UrlConstant.ALLPRESONURL);
        return url;
    }
    @android.webkit.JavascriptInterface
    public String getPath(){
        String path = UrlConstant.HTTP+UrlConstant.getIP()+":"+UrlConstant.getPORT();
        return path;
    }

    @android.webkit.JavascriptInterface
    public void finishBack(){
        finish();
    }
}
