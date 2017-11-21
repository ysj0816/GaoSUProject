package com.chuyu.gaosuproject.network;

import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.convert.StringConvert;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by wo on 2017/7/15.
 */

public abstract class MyCallback extends AbsCallback<String> {

    private String mS;

    @Override
    public abstract void onSuccess(String s, Call call, Response response);

    @Override
    public String convertSuccess(Response response) throws Exception {
        mS = StringConvert.create().convertSuccess(response);

        response.close();
        return mS;
    }

    @Override
    public void onError(Call call, Response response, Exception e) {
        super.onError(call, response, e);
        MyOnError(call, response, e, mS);
    }

    public abstract void MyOnError(Call call, Response response, Exception e, String errorMsg);
}



