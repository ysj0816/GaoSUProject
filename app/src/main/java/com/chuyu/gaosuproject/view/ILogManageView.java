package com.chuyu.gaosuproject.view;

import com.chuyu.gaosuproject.base.BaseView;

/**
 * Created by Administrator on 2017/12/7.
 */

public interface ILogManageView extends BaseView{
	//等待窗口
	void showWaiting();
	//关闭窗口
	void closeWaiting();
	//提交失败
	void submitFailed();
	//请假成功
	void submitSuccess(boolean success);
}
