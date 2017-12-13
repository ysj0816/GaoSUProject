package com.chuyu.gaosuproject.util.upload;


import com.chuyu.gaosuproject.bean.dailycheck.DailyCheck;
import com.chuyu.gaosuproject.dao.DBManager;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


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
