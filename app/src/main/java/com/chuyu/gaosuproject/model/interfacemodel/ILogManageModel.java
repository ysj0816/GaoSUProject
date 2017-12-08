package com.chuyu.gaosuproject.model.interfacemodel;

/**
 * Created by Administrator on 2017/12/7.
 */

public interface ILogManageModel {
	/**
	 * 提交日志
	 *
	 * @param userid
	 * @param createTime
	 * @param finishwork
	 * @param unfinishwork
	 * @param needassistwork
	 * @param remark
	 * @param category
	 * @param logManageListener
	 */
	void submitLogData(String userid, String createTime, String finishwork, String unfinishwork, String needassistwork,
					   String remark, String category, LogManageListener logManageListener);

	interface LogManageListener {
		void LoadSuccess(boolean success);

		void LoadFailed();

	}

	//广播中查询日志
	interface ReceiveLogManageListener {
		void receiveSuccess(int total);

		void receiveFailed();
	}

	//广播中提交日志
	interface OnReceiveSubmitLog {
		void LoadLogSuccess(boolean success);

		void LoadLodFailed();
	}
}
