package com.chuyu.gaosuproject.util.upload;

import android.content.Intent;
import android.text.TextUtils;
import android.util.Log;

import com.chuyu.gaosuproject.activity.AddManagerLogActivity;
import com.chuyu.gaosuproject.activity.LogManageActivity;
import com.chuyu.gaosuproject.bean.logmanagebean.ManageLog;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.dao.DBManager;
import com.chuyu.gaosuproject.util.NetworkUtils;
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
	public int tag = 0;
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
	 * 广播中上传日志
	 */
	public void  upLoadLog(List<ManageLog> list) {
		allmanageLogs = new ArrayList<>();
		List<ManageLog> manageLogs = list;
		allmanageLogs = manageLogs;
		Log.i("test", "allmanageLogs大小:" + allmanageLogs.size());
		CheckLogData(allmanageLogs);

	}

	public List<ManageLog> allmanageLogs;

	public void CheckLogData(List<ManageLog> manageLogs) {

		if (manageLogs.size() > 0) {
			++tag;
			Log.i("test", "tag:" + tag);
			ManageLog manageLog = manageLogs.get(0);
			querymanagelog(manageLog);
		}
	}

	/**
	 * @param manageLog 查询对象信息
	 */
	private void querymanagelog(ManageLog manageLog) {
		Long id = manageLog.getId();
		String userId = manageLog.getUserId();
		String createTime = manageLog.getCreateTime();
		String finishWork = manageLog.getFinishWork();
		String unFinishWork = manageLog.getUnFinishWork();
		String needAssistWork = manageLog.getNeedAssistWork();
		String remark = manageLog.getRemark();
		String category = manageLog.getCategory();
		//查找有无提交的日志
		queryisLog(manageLog);
		Log.i("test", "广播提交时:" + manageLog.toString());
	}

	/**
	 * @param manageLog 查询有无日志信息
	 */
	private void queryisLog(final ManageLog manageLog) {

		String createTime = manageLog.getCreateTime();
		String[] split = createTime.split(" ");
//		Log.i("test", "split:" + split[0]);
//		Log.i("test","Category:"+manageLog.getCategory());
//		Log.i("test","UserId:"+manageLog.getUserId());
//		Log.i("test","url:"+UrlConstant.formatUrl(UrlConstant.GetWorkDairyURL));
//		boolean availableByPing = NetworkUtils.isAvailableByPing("219.139.79.56");
//		Log.i("test","PINg:"+availableByPing);



		OkGo.post("http://192.168.11.9:8088/GS/a/mobile/WorkDiary/MobileGetWorkDairy?")
				.params("UserID", manageLog.getUserId())
				.params("SearchDate", split[0])
				.params("Category", manageLog.getCategory())
				.execute(new StringCallback() {
					@Override
					public void onSuccess(String s, Call call, Response response) {
						Log.i("test", "是否有日志" + s.toString());
						try {
							Log.i("test", "yyyyyyyyyyyyyy");
							JSONObject object = new JSONObject(s.toString());
							String total = object.getString("total");
							int total1 = object.getInt("total");
							//可以提交日志
							if (total1 == 0) {
								Log.i("test", "total1:" + total1);
								Long id = manageLog.getId();
								String userId = manageLog.getUserId();
								String createTime = manageLog.getCreateTime();
								String finishWork = manageLog.getFinishWork();
								String unFinishWork = manageLog.getUnFinishWork();
								String needAssistWork = manageLog.getNeedAssistWork();
								String remark = manageLog.getRemark();
								String category = manageLog.getCategory();
								Log.i("test","当前category:"+category);
								onReceiveUpLoad(id, userId, createTime, finishWork, unFinishWork, needAssistWork, remark, category);
							}
							//有当天记录删除这条记录
							else {
								Log.i("test", "有数据");
								deleteSingData(manageLog.getId());
								allmanageLogs.remove(0);
								CheckLogData(allmanageLogs);

							}
						} catch (JSONException e) {
							e.printStackTrace();
						}

					}


					@Override
					public void onError(Call call, Response response, Exception e) {
						super.onError(call, response, e);
						Log.i("test", "查询错误:");
//						deleteSingData(manageLog.getId());
						allmanageLogs.remove(0);
						CheckLogData(allmanageLogs);
					}
				});
	}

	/**
	 * 提交水电工日志
	 * @param id
	 * @param userId
	 * @param createTime
	 * @param finishWork
	 * @param unFinishWork
	 * @param needAssistWork
	 * @param remark
	 * @param category
	 */
	private void onReceivewaterlog(final Long id, String userId, String createTime, String finishWork, String unFinishWork, String needAssistWork, String remark, String category) {
		//上报水电工日志
		OkGo.post("http://192.168.11.9:8088/GS/a/mobile/WorkDiary/MobileAddWorkDiary?")
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
						Log.i("test", "ttttttttttttt" + s.toString());
						//请求成功删除当前数据库的内容和集合中内容
						deleteSingData(id);
						allmanageLogs.remove(0);
						CheckLogData(allmanageLogs);

					}


					@Override
					public void onError(Call call, Response response, Exception e) {
						super.onError(call, response, e);
						Log.i("test", "提交失败");
						//请求失败删除当前数据库的内容和集合中内容
						deleteSingData(id);
						allmanageLogs.remove(0);
						CheckLogData(allmanageLogs);
					}
				});
	}


	/**
	 * 提交管理员日志
	 *
	 * @param id
	 * @param userId
	 * @param createTime
	 * @param finishWork
	 * @param unFinishWork
	 * @param needAssistWork
	 * @param remark
	 * @param category
	 */
	private void onReceiveUpLoad(final Long id, String userId, String createTime, String finishWork, String unFinishWork, String needAssistWork, String remark, String category) {
		//UrlConstant.formatUrl(UrlConstant.AddmManagerLogUrL)
		//上报管理员日志
		OkGo.post("http://192.168.11.9:8088/GS/a/mobile/WorkDiary/MobileAddWorkDiary?")
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
						Log.i("test", "ttttttttttttt" + s.toString());
						//请求成功删除当前数据库的内容和集合中内容
						deleteSingData(id);
						allmanageLogs.remove(0);
						CheckLogData(allmanageLogs);

					}


					@Override
					public void onError(Call call, Response response, Exception e) {
						super.onError(call, response, e);
						Log.i("test", "提交失败");
						//请求失败删除当前数据库的内容和集合中内容
						deleteSingData(id);
						allmanageLogs.remove(0);
						CheckLogData(allmanageLogs);
					}
				});
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

	/**
	 * 通过主键删除数据
	 *
	 * @param id
	 */
	private void deleteSingData(Long id) {

		dbmanageLog.deleteByID(id);
	}
}
