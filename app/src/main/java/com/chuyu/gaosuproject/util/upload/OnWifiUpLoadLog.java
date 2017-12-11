package com.chuyu.gaosuproject.util.upload;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.chuyu.gaosuproject.activity.AddManagerLogActivity;
import com.chuyu.gaosuproject.activity.LogManageActivity;
import com.chuyu.gaosuproject.bean.logmanagebean.ManageLog;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.dao.DBManager;
import com.chuyu.gaosuproject.model.LogManageModel;
import com.chuyu.gaosuproject.model.interfacemodel.ILogManageModel;
import com.chuyu.gaosuproject.util.NetworkUtils;
import com.chuyu.gaosuproject.util.SPUtils;
import com.chuyu.gaosuproject.util.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/11/30.
 */

public class OnWifiUpLoadLog {
	private static OnWifiUpLoadLog onWifiUpLoadLog;
	/**
	 * 日志数据库操作类
	 */
	private DBManager<ManageLog> dbmanageLog;

	private OnWifiUpLoadLog() {
		dbmanageLog = new DBManager<>(ManageLog.class);
	}

	public static OnWifiUpLoadLog getInstace() {
		if (null == onWifiUpLoadLog) {
			synchronized (OnWifiUpLoadLog.class) {
				if (null == onWifiUpLoadLog) {
					onWifiUpLoadLog = new OnWifiUpLoadLog();
				}
			}
		}
		return onWifiUpLoadLog;
	}

	/**
	 * @return 获取数据库操作类
	 */
	public DBManager<ManageLog> getDbManager() {
		if (dbmanageLog == null) {
			dbmanageLog = new DBManager<>(ManageLog.class);
		}
		return dbmanageLog;
	}

	/**
	 * 删除所有数据
	 */
	public void deleteALL() {
		dbmanageLog.deleteAll();
	}

	/**
	 * @return 查询当天写是否写过日志
	 */
	public List<ManageLog> queryData() {
		List<ManageLog> manageLoglist = dbmanageLog.queryAllList(dbmanageLog.getQueryBuiler());
		if (manageLoglist != null) {
			return manageLoglist;
		}
		return null;
	}


}
