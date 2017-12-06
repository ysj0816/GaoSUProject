package com.chuyu.gaosuproject.util.upload;

import android.util.Log;

import com.chuyu.gaosuproject.bean.logmanagebean.ManageLog;
import com.chuyu.gaosuproject.dao.DBManager;
import com.chuyu.gaosuproject.util.NetworkUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2017/12/4.
 */

public class OnWifiUpLoadWaterLog {
	private static OnWifiUpLoadWaterLog onWifiUpLoadWaterLog;
	private DBManager dbmanageLog;
	public int tag = 0;

	private OnWifiUpLoadWaterLog() {
		dbmanageLog = new DBManager<>(ManageLog.class);
	}

	public static OnWifiUpLoadWaterLog getInstace() {
		if (null == onWifiUpLoadWaterLog) {
			synchronized (OnWifiUpLoadWaterLog.class) {
				if (null == onWifiUpLoadWaterLog) {
					onWifiUpLoadWaterLog = new OnWifiUpLoadWaterLog();
				}
			}
		}
		return onWifiUpLoadWaterLog;
	}

	public List<ManageLog> allmanageLogs;


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
	public void upLoadLog(List<ManageLog> list) {
		allmanageLogs = new ArrayList<>();
		List<ManageLog> manageLogs = list;
		allmanageLogs = manageLogs;
		Log.i("test", "allmanageLogs大小:" + allmanageLogs.size());
		CheckLogData(allmanageLogs);

	}

	private void CheckLogData(List<ManageLog> allmanageLogs) {
		if (allmanageLogs.size() > 0) {

			++tag;
			Log.i("test", "tag:" + tag);
			ManageLog manageLog = allmanageLogs.get(0);
			querymanagelog(manageLog);
		}
	}

	private void querymanagelog(final ManageLog manageLog) {
		String createTime = manageLog.getCreateTime();
		String[] split = createTime.split(" ");
		Log.i("test", "水电工广播提交时:" + manageLog.toString());
		boolean availableByPing = NetworkUtils.isAvailableByPing("192.168.11.9");
		Log.i("test", "PINg:" + availableByPing);
		OkGo.post("http://192.168.11.9:8088/GS/a/mobile/WorkDiary/MobileGetWorkDairy?")
				.params("UserID", manageLog.getUserId())
				.params("SearchDate", split[0])
				.params("Category", manageLog.getCategory())
				.execute(new StringCallback() {
					@Override
					public void onSuccess(String s, Call call, Response response) {
						Log.i("test", "水电工是否有日志" + s.toString());
						try {
							JSONObject object = new JSONObject(s.toString());
							int total = object.getInt("total");
							//可以提交日志
							if (total == 0) {
								Log.i("test", "total:" + total);
								Log.i("test", "当前category:" + manageLog.getCategory());
								onReceivewaterlog(manageLog.getId(), manageLog.getUserId(), manageLog.getCreateTime(),
										manageLog.getFinishWork(), manageLog.getUnFinishWork(), manageLog.getNeedAssistWork(),
										manageLog.getRemark(), manageLog.getCategory());

							}
							//有当天记录删除这条记录
							else {
								Log.i("test", "水电工有数据");
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
						Log.i("test", "水电工查询错误:");
					}
				});

	}

	private void deleteSingData(Long id) {
		dbmanageLog.deleteByID(id);
	}

	/**
	 * 请求水电工日志
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
	private void onReceivewaterlog(final Long id, String userId, String createTime, String finishWork, String unFinishWork, String needAssistWork, String remark, String category) {
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
						Log.i("test", "水电工ttttttttttttt" + s.toString());
						//请求成功删除当前数据库的内容和集合中内容
						deleteSingData(id);
						allmanageLogs.remove(0);
						CheckLogData(allmanageLogs);

					}


					@Override
					public void onError(Call call, Response response, Exception e) {
						super.onError(call, response, e);
						Log.i("test", "水电工提交失败");
						//请求失败删除当前数据库的内容和集合中内容
						deleteSingData(id);
						allmanageLogs.remove(0);
						CheckLogData(allmanageLogs);
					}
				});
	}
}
