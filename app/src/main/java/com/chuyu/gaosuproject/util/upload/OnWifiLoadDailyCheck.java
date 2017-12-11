package com.chuyu.gaosuproject.util.upload;

import android.content.Context;
import android.util.Log;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.chuyu.gaosuproject.bean.dailycheck.DailyCheck;
import com.chuyu.gaosuproject.bean.daobean.LeaveDataBean;
import com.chuyu.gaosuproject.bean.logmanagebean.ManageLog;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.dao.DBManager;
import com.chuyu.gaosuproject.model.DailyCheckListFillModel;
import com.chuyu.gaosuproject.model.interfacemodel.IDailyCheckListFillModel;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/4.
 */

public class OnWifiLoadDailyCheck {
	public static OnWifiLoadDailyCheck onWifiLoadDailyCheck;
	private DBManager<DailyCheck> dailyCheckDBManager;
	private List<File> listfile = new ArrayList<File>();

	/**
	 * 是否继续执行
	 */

	private OnWifiLoadDailyCheck() {
		dailyCheckDBManager = new DBManager<>(DailyCheck.class);
	}

	/**
	 * 单例
	 *
	 * @return
	 */
	public static OnWifiLoadDailyCheck getInstance() {
		if (null == onWifiLoadDailyCheck) {
			synchronized (OnWifiLoadDailyCheck.class) {
				if (null == onWifiLoadDailyCheck) {
					onWifiLoadDailyCheck = new OnWifiLoadDailyCheck();
				}
			}
		}
		return onWifiLoadDailyCheck;
	}

	/**
	 * 获取数据库管理类
	 *
	 * @return
	 */
	public DBManager<DailyCheck> getDbManager() {
		if (dailyCheckDBManager == null) {
			dailyCheckDBManager = new DBManager<>(DailyCheck.class);
		}
		return dailyCheckDBManager;
	}

	/**
	 * 查询数据库中请假数据
	 *
	 * @return
	 */
	public List<DailyCheck> queryData() {
		List<DailyCheck> dailycheckBean = dailyCheckDBManager.queryAllList(dailyCheckDBManager.getQueryBuiler());
		Log.i("test","查询检查数据1："+dailycheckBean.size());
		if (dailycheckBean != null) {
			return dailycheckBean;
		}
		return null;
	}

	/**
	 * 删除所有数据
	 */
	public void deleteALL(){
		dailyCheckDBManager.deleteAll();
	}








}
