package com.chuyu.gaosuproject.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.chuyu.gaosuproject.util.SVP.SVProgressHUD;
import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.base.MVPBaseActivity;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.presenter.SignNodePresenter;
import com.chuyu.gaosuproject.view.ISignNodeView;


import java.text.SimpleDateFormat;
import java.util.Date;
import butterknife.BindView;

/***
 * 查看某人的签到记录
 */

public class SignNodeWebActivity extends MVPBaseActivity<ISignNodeView, SignNodePresenter> implements ISignNodeView {

    @BindView(R.id.webview)
    WebView webview;

    private SignNodePresenter signNodePresenter;
    private String userid;
    private SVProgressHUD svProgressHUD;
    private Intent intent;
    @Override
    protected SignNodePresenter initPresenter() {
        signNodePresenter = new SignNodePresenter();
        return signNodePresenter;
    }

    @Override
    protected int initContent() {
        return R.layout.activity_sign_node_web;
    }

    @Override
    protected void initView() {
        //根据不同的值加载不同的界面
        intent = getIntent();
        //flag用来标记需要跳转的html页面
        userid = intent.getStringExtra("userid");
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
        webview.loadUrl("file:///android_asset/html/sign_record.html");
        handler.sendEmptyMessageDelayed(0,15000);
    }


    @Override
    protected void initData() {


        svProgressHUD = new SVProgressHUD(this);
    }

    @Override
    public void showWaiting() {
        svProgressHUD.showWithStatus("请稍等...");
    }

    @Override
    public void colseWaiting() {
        svProgressHUD.dismissImmediately();
    }

    @Override
    public void onSuccess(String data) {
        Log.i("test","data:"+data.toString());

        //个人考勤记录

    }

    @Override
    public void onFailed() {
        svProgressHUD.showErrorWithStatus("加载错误！");
    }

    @Override
    public void showExpcetion(String msg) {
        Log.i("test","msg:"+msg);
    }

    private String getNowDate(){
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        String str = formatter.format(curDate);
        return str;
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
        Log.i("test","调用ID"+userid);
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
        String url = UrlConstant.formatUrl(UrlConstant.PERSONDATEURL);
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
