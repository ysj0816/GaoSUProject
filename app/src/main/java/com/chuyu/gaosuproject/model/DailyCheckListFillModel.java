package com.chuyu.gaosuproject.model;

import android.util.Log;

import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.model.interfacemodel.IDailyCheckListFillModel;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/7/26.
 */

public class DailyCheckListFillModel {
    private DailyCheckListFillModel(){

    }
    public static DailyCheckListFillModel dailyCheckListFillModel;
    private static Gson gson;
    public static DailyCheckListFillModel getInsance() {
        if (null == dailyCheckListFillModel) {
            dailyCheckListFillModel = new DailyCheckListFillModel();
            gson=new Gson();
        }
        return dailyCheckListFillModel;
    }
    public void submitcheckfill(String checkunit, String checkproject, String userid, String checkresult, String content
            , String deductpoint, String checkid, List<File> file, final IDailyCheckListFillModel.FillListener fillListener){
        Log.i("test","有网图片:"+file);
        OkGo.post(UrlConstant.formatUrl(UrlConstant.updateCheckInforURL))
                .params("CheckUnit", checkunit)
                .params("CheckProject", checkproject)
                .params("UserId", userid)
                .params("CheckResult", checkresult)
                .params("Content", content)
                .params("DeductPoint", deductpoint)
                .params("CheckId", checkid)
                .addFileParams("file",file)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
                            JSONObject json = new JSONObject(s);
                            boolean success = json.getBoolean("success");
                            String msg = json.getString("message");
                            if (success) {
                                fillListener.submitSuccess();
                            }else{
                                fillListener.showExpretion(msg);
                            }
                        } catch (JSONException e) {

                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        fillListener.submitFaile();
                    }
                });
    }
}
