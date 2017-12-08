package com.chuyu.gaosuproject.presenter;

import com.chuyu.gaosuproject.base.BasePresenter;
import com.chuyu.gaosuproject.model.LogManageModel;
import com.chuyu.gaosuproject.model.interfacemodel.ILogManageModel;
import com.chuyu.gaosuproject.view.ILogManageView;

/**
 * Created by Administrator on 2017/12/7.
 */

public class LogManagePresenter extends BasePresenter<ILogManageView> {

	//提交日志
	public void submitLog(String userid, String createTime, String finishwork, String unfinishwork, String needassistwork,
						  String remark, String category) {
		if (isViewAttached()) {

		} else {
			return;
		}
		final ILogManageView view = getView();
		view.showWaiting();
		LogManageModel.getInstance().submitLogData(userid, createTime, finishwork, unfinishwork, needassistwork, remark, category, new ILogManageModel.LogManageListener() {

			@Override
			public void LoadSuccess(boolean success) {
				view.closeWaiting();
				view.submitSuccess(success);
			}

			@Override
			public void LoadFailed() {
				view.closeWaiting();
				view.submitFailed();
			}
		});
	}

}
