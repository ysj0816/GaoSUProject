package com.chuyu.gaosuproject.model;

import android.content.Intent;
import android.util.Log;

import com.chuyu.gaosuproject.activity.AddManagerLogActivity;
import com.chuyu.gaosuproject.activity.LogManageActivity;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.model.interfacemodel.ILogManageModel;
import com.chuyu.gaosuproject.util.ToastUtils;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.json.JSONException;
import org.json.JSONObject;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/7.
 */

public class LogManageModel implements ILogManageModel {
	private static LogManageModel logManageModel;
	private static Gson gson;

	private LogManageModel() {
	}

	public static LogManageModel getInstance() {
		if (logManageModel == null) {
			logManageModel = new LogManageModel();
			gson = new Gson();
		}
		return logManageModel;
	}

	@Override
	public void submitLogData(String userid, String createTime, String finishwork, String unfinishwork, String needassistwork, String remark, String category, final LogManageListener logManageListener) {
		OkGo.post(UrlConstant.formatUrl(UrlConstant.AddmManagerLogUrL))
				.params("AuthorUserID", userid)
				.params("CreateTime", createTime)
				.params("FinishWork", finishwork)
				.params("UnFinishWork", unfinishwork)
				.params("NeedAssistWork", needassistwork)
				.params("Remark", remark)
				.params("Category", category)
				.execute(new StringCallback() {
					@Override
					public void onSuccess(String s, Call call, Response response) {
						Log.i("test", s.toString());
						try {
							JSONObject json = new JSONObject(s);
							boolean success = json.getBoolean("success");
							String msg = json.getString("msg");
							logManageListener.LoadSuccess(success);
						} catch (JSONException e) {

						}
					}


					@Override
					public void onError(Call call, Response response, Exception e) {
						super.onError(call, response, e);
						logManageListener.LoadFailed();
					}
				});
	}

	//广播中查询是否有日志
	public void isLogData(String userid, String time, String category, final ReceiveLogManageListener receiveLogManageListener) {
		OkGo.post(UrlConstant.formatUrl(UrlConstant.GetWorkDairyURL))
				.params("UserID", userid)
				.params("SearchDate", time)
				.params("Category", category)
				.execute(new StringCallback() {
					@Override
					public void onSuccess(String s, Call call, Response response) {
						Log.i("test", "是否有日志" + s.toString());
						try {
							JSONObject object = new JSONObject(s.toString());
							int total = object.getInt("total");
							receiveLogManageListener.receiveSuccess(total);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onError(Call call, Response response, Exception e) {
						super.onError(call, response, e);
						Log.i("test", "查询日志失败");
						receiveLogManageListener.receiveFailed();
					}
				});
	}

	//广播中提交日志
	public void onreceivesubmitlog(Long id, String userId, String createTime, String finishWork, String unFinishWork, String needAssistWork, String remark, String category, final OnReceiveSubmitLog onReceiveSubmitLog) {
		OkGo.post(UrlConstant.formatUrl(UrlConstant.AddmManagerLogUrL))
				.params("AuthorUserID", userId)
				.params("CreateTime", createTime)
				.params("FinishWork", finishWork)
				.params("UnFinishWork", unFinishWork)
				.params("NeedAssistWork", needAssistWork)
				.params("Remark", remark)
				.params("Category", category)
				.execute(new StringCallback() {
					@Override
					public void onSuccess(String s, Call call, Response response) {
						Log.i("test", "提交成功:" + s.toString());
						try {
							JSONObject object = new JSONObject(s);
							boolean success = object.getBoolean("success");
							onReceiveSubmitLog.LoadLogSuccess(success);
						} catch (JSONException e) {
							e.printStackTrace();
						}
					}

					@Override
					public void onError(Call call, Response response, Exception e) {
						super.onError(call, response, e);
						Log.i("test", "提交失败");
						onReceiveSubmitLog.LoadLodFailed();
					}
				});
	}
}
