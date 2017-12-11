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
	 * 接受到广播后上传签到数据
	 */
	public void upLoadLeaveData(List<DailyCheck> list) {
		Log.i("test", "我进来了");
		tolist = new ArrayList<>();
		List<DailyCheck> list1 = list;
		tolist = list1;
		upLoadailycheck(tolist);

	}

	private void upLoadailycheck(List<DailyCheck> tolist) {
		if (tolist.size()>0){
			DailyCheck dailyCheck = tolist.get(0);
			//判断能否请假
			startUpload(dailyCheck);
		}
	}
	/**
	 * 删除所有数据
	 */
	public void deleteALL(){
		dailyCheckDBManager.deleteAll();
	}


	private void deleteSingData(Long id) {
		dailyCheckDBManager.deleteByID(id);
	}

	private void startUpload(final DailyCheck dailyCheck) {
		if (!listfile.isEmpty()) {
			listfile.clear();
		}
		Log.i("test","CheckUnit:"+dailyCheck.getCheckunit());
		Log.i("test","Userid():"+dailyCheck.getUserid());
		//http://192.168.11.9:8088/GS/a/mobile/check/AddCheckInfor /GS/a/mobile/check/AddCheckInfor
		OkGo.post("http://192.168.11.9:8088/GS/a/mobile/check/AddCheckInfor")
				.connTimeOut(10000)
				.tag(dailyCheck.getId())
				.params("CheckUnit", dailyCheck.getCheckunit())
				.params("CheckProject", dailyCheck.getCheckproject())
				.params("UserId", dailyCheck.getUserid())
				.params("CheckResult", dailyCheck.getCheckresult())
				.params("Content", dailyCheck.getContent())
				.params("DeductPoint", dailyCheck.getDeductpoint())
				.params("CheckId", dailyCheck.getCheckid())
				.addFileParams("file", listfile)
				.execute(new StringCallback() {
					@Override
					public void onSuccess(String s, Call call, Response response) {
						Log.i("test","成功:"+s.toString());
						try {
							JSONObject json = new JSONObject(s);
							boolean success = json.getBoolean("success");
							String msg = json.getString("message");
							OkGo.getInstance().cancelTag(dailyCheck.getId());
							deleteSingData(dailyCheck.getId());
							tolist.remove(0);
							upLoadailycheck(tolist);
						} catch (JSONException e) {

						}
					}


					@Override
					public void onError(Call call, Response response, Exception e) {
						super.onError(call, response, e);
						Log.i("test","上报失败...");
						OkGo.getInstance().cancelTag(dailyCheck.getId());
//						deleteSingData(dailyCheck.getId());
						tolist.remove(0);
						upLoadailycheck(tolist);
					}
				});
	}

	public List<DailyCheck> tolist;


}
