package com.chuyu.gaosuproject.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;

import com.chuyu.gaosuproject.R;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * Created by wo on 2017/7/17.
 */

public class TestActivity extends AppCompatActivity {


    @BindView(R.id.bt)
    Button bt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        getUserinfo();

    }

    String imgPath = "/storage/emulated/0/com.chuyu.gaosuproject/DCIM/2017-11-10-02-07-09.jpg";
    String imgPath2="file:///storage/emulated/0/news_article/8c9549696b622cd1ab2b925ce20b1159.jpg";
    String url = "http://192.168.3.84:8080/alpha/muiAuth/getPersonalInfo";
    String token = "b14069a240872a7f51c20a4645c5f7cfa25d5ed9ed01ee0e47d73747";

    String comit="http://192.168.3.84:8080/alpha/muiAuth/changePersonalInfo?";

    public void getUserinfo() {
        OkGo.post(url)
                .headers("t", token)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i("test","登录后："+s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.i("test","登录后：失败");
                    }
                });
    }

    @OnClick(R.id.bt)
    public void onClick() {
        File file = new File(imgPath2);


        OkGo.post(comit)
                .headers("t", token)
                .params("photo",file)
                .params("mobile", "172226800")
                .params("qq", "55677")
                .params("phone", "1345")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i("test","成功:"+s);
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        Log.i("test","失败");
                        e.printStackTrace();
                    }
                });






    }
}
