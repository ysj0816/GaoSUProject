package com.chuyu.gaosuproject.util.upload;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.chuyu.gaosuproject.bean.daobean.LeaveDataBean;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.dao.DBManager;
import com.chuyu.gaosuproject.model.LeaveModel;
import com.chuyu.gaosuproject.model.interfacemodel.ILeaveModel;
import com.chuyu.gaosuproject.util.NetworkUtils;

import java.util.Iterator;
import java.util.List;

/**
 * @author 杨仕俊
 * @description wifi状态下自动提交请假数据的工具类
 * Created by wo on 2017/12/4.
 */

public class OnWifiLoadLeave {
    /**
     * 数据库管理类
     */
    public DBManager<LeaveDataBean> dbManager;
    /**
     *
     */
    private static OnWifiLoadLeave onWifiLoadLeave;
    /**
     * 迭代器 iterator
     */
    private Iterator<LeaveDataBean> leaveIterator;
    /**
     * 是否继续执行
     */
    private static boolean isContinueExectue = true;
    /**
     * handler
     */
    private MyHandler myHandler;

    /**
     * 构造方法
     */
    private OnWifiLoadLeave() {
        /**
         * 数据库操作类
         */
        dbManager = new DBManager<>(LeaveDataBean.class);
        myHandler = new MyHandler();
    }

    /**
     * 单例
     *
     * @return
     */
    public static OnWifiLoadLeave getInstance() {
        if (null == onWifiLoadLeave) {
            synchronized (OnWifiLoadLeave.class) {
                if (null == onWifiLoadLeave) {
                    onWifiLoadLeave = new OnWifiLoadLeave();
                }
            }
        }
        return onWifiLoadLeave;
    }

    /**
     * 获取数据库管理类
     *
     * @return
     */
    public DBManager<LeaveDataBean> getDbManager() {
        if (dbManager == null) {
            dbManager = new DBManager<>(LeaveDataBean.class);
        }
        return dbManager;
    }

    /**
     * 查询数据库中请假数据
     *
     * @return
     */
    private List<LeaveDataBean> queryLeaveData() {
        List<LeaveDataBean> leaveDataBean = dbManager.queryAllList(dbManager.getQueryBuiler());
        if (leaveDataBean != null) {
            return leaveDataBean;
        }
        return null;
    }

    /**
     * 接受到广播后上传签到数据
     */
    public void upLoadLeaveData() {
        if (queryLeaveData() != null) {
            //判断能否请假
            leaveIterator = queryLeaveData().iterator();
            startUpload(leaveIterator);
        }
    }

    /**
     * 迭代器中执行
     *
     * @param leaveIterator
     */
    private void startUpload(Iterator<LeaveDataBean> leaveIterator) {
        while (isContinueExectue && leaveIterator.hasNext()) {
            LeaveDataBean leaveDataBean = leaveIterator.next();
            isAbleToLeave(leaveDataBean);
            isContinueExectue = false;
        }
    }


    /**
     * 判断是否能够请假
     *
     * @param leaveDataBean
     */
    private void isAbleToLeave(final LeaveDataBean leaveDataBean) {
        LeaveModel.getInstance().ableToleave(leaveDataBean.getId(),leaveDataBean.getUserid(), leaveDataBean.getDutyType(),
                leaveDataBean.getNowDate(),
                new ILeaveModel.ResLeaveListener() {
                    @Override
                    public void isLeave() {
                        Log.i("test","可以请假");
                        //可以请假
                        commitLeave(leaveDataBean);
                    }

                    @Override
                    public void notLeave() {
                        Log.i("test","不可以请假");
                        //不能请假
                        myHandler.sendEmptyMessage(0);
                        cancal(leaveDataBean.getId());
                        //删除数据
                        deleteLeaveById(leaveDataBean.getId());
                    }

                    @Override
                    public void shwoExpretion(String msg) {
                        Log.i("test","请假检查异常");
                        myHandler.sendEmptyMessage(0);
                        cancal(leaveDataBean.getId());
                        //deleteLeaveById(leaveDataBean.getId());
                    }
                });
    }

    /**
     * 提交数据库中的请假数据
     * @param leaveDataBean 数据库中的
     *
     */
    private void commitLeave(final LeaveDataBean leaveDataBean) {
        LeaveModel.getInstance().onReceiverLeave(leaveDataBean.getId(),leaveDataBean.getUserid(), leaveDataBean.getDutyType(), leaveDataBean.getStartDate(),
                leaveDataBean.getEndData(), leaveDataBean.getReason(), leaveDataBean.getLeaveType(), new ILeaveModel.LeaveListener() {
                    @Override
                    public void submitSuccess() {
                        cancal(leaveDataBean.getId());
                        //请假成功
                        myHandler.sendEmptyMessage(0);
                        //删除数据
                        deleteLeaveById(leaveDataBean.getId());
                        Log.i("test","请假成功");

                    }

                    @Override
                    public void submitFaile() {
                        cancal(leaveDataBean.getId());
                        //请假失败
                        myHandler.sendEmptyMessage(0);
                        //删除数据
                        deleteLeaveById(leaveDataBean.getId());
                        Log.i("test","请假失败");

                    }

                    @Override
                    public void shwoExpretion(String msg) {
                        cancal(leaveDataBean.getId());
                        //请假异常
                        myHandler.sendEmptyMessage(0);
                        //删除数据
                        //deleteLeaveById(leaveDataBean.getId());
                        Log.i("test","请假异常");
                    }
                });
    }


    private class MyHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            isContinueExectue = true;
            if (leaveIterator.hasNext()) {
                //继续执行上传
                startUpload(leaveIterator);
            }
        }
    }

    private void deleteLeaveById(Long id){
        Log.i("test", "删除请假：" + id);
        dbManager.deleteByID(id);
    }

    private void cancal(Object tag){
        LeaveModel.getInstance().cancelOKGO(tag);
    }
}
