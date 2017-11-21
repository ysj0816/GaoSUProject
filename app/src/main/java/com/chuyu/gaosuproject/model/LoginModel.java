package com.chuyu.gaosuproject.model;

import android.graphics.Bitmap;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.chuyu.gaosuproject.bean.LoginBean;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.model.interfacemodel.ILoginModel;
import com.chuyu.gaosuproject.network.JsonCallback;
import com.chuyu.gaosuproject.network.MyCallback;
import com.chuyu.gaosuproject.network.model.LoginMode;
import com.chuyu.gaosuproject.network.reponse.BaseResponse;
import com.chuyu.gaosuproject.util.ToastUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.convert.StringConvert;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.Response;

/**
 * Created by wo on 2017/7/13.
 *  登录功能
 *
 *  mvp模式中model
 */

public class LoginModel implements ILoginModel {
    private LoginModel() {
    }

    public static LoginModel loginModel;
    private static Gson gson;

    public static LoginModel getInsance() {
        if (null == loginModel) {
            loginModel = new LoginModel();
            gson = new Gson();
        }
        return loginModel;
    }



    @Override
    public void login(String username, String password, final LoginLisenter loginLisenter) {
        //top1 判断是否为空
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            loginLisenter.textEmpty();
        } else {

            OkGo.post(UrlConstant.formatUrl(UrlConstant.LOGINURL))
                    .connTimeOut(10000)
                    .params("username", username)
                    .params("password", password)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            try{
                                Log.i("test","登录："+s);
                                LoginBean loginBean = gson.fromJson(s, LoginBean.class);
                                loginLisenter.loginSuccess(loginBean);
                            }catch (Exception e){
                                loginLisenter.netWorkExpetion();
                            }

                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            Log.i("test","onError");
                            loginLisenter.loginFailed();
                            //long time = System.currentTimeMillis() - lastTime;

                        }
                    });
        }
    }

}
