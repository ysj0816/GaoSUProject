package com.chuyu.gaosuproject.util.upload;

import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.chuyu.gaosuproject.bean.daobean.SignDataDao;
import com.chuyu.gaosuproject.dao.DBManager;
import com.chuyu.gaosuproject.model.SignsModel;
import com.chuyu.gaosuproject.model.interfacemodel.ISignsModel;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author 杨仕俊
 * @description 在wifi状态下提交签到数据工具类
 * @class com.chuyu.gaosuproject.util.upload.OnWifiUpLoadSign
 * @date 2017/11/28
 * Created by ysj on 2017/11/28.
 */

public class OnWifiUpLoadSign {

    private static OnWifiUpLoadSign onWifiSign;
    /**
     * 签到的数据库操作类
     */
    private DBManager<SignDataDao> dbManager;
    private MyHandler myHandler;

    private OnWifiUpLoadSign() {
        dbManager = new DBManager<>(SignDataDao.class);
        myHandler = new MyHandler();
    }

    /**
     * 是否继续执行
     */
    public static boolean isContinueExecetue = true;
    /**
     * 上午的签到数据
     */
    public List<SignDataDao> amSignData = new ArrayList<>();
    /**
     * 下午的签到数据
     */
    public List<SignDataDao> pmSignData = new ArrayList<>();

    public Iterator<SignDataDao> iteratorAM;

    public Iterator<SignDataDao> iteratorPM;

    /**
     * 代表上午签到的iteratorAM第几次迭代
     */
    public static int TAG=0;

    public static OnWifiUpLoadSign getInstace() {
        if (null == onWifiSign) {
            synchronized (OnWifiUpLoadSign.class) {
                if (null == onWifiSign) {
                    onWifiSign = new OnWifiUpLoadSign();
                }
            }
        }
        return onWifiSign;
    }

    /**
     * 获取数据库操作类
     *
     * @return
     */
    public DBManager<SignDataDao> getDbManager() {
        if (dbManager == null) {
            dbManager = new DBManager<>(SignDataDao.class);
        }
        return dbManager;
    }

    /**
     * 广播中调用上传签到数据
     */
    public void upLoadSignData() {
        if (queryData() != null) {
            //数据不为空
            //首先判断是上午的否能够签到
            amSingDataCheck(queryData());
        }
    }

    /**
     * 上午签到数据检测
     *
     * @param signDataDaos
     */
    private void amSingDataCheck(List<SignDataDao> signDataDaos) {

        for (int i = 0; i < signDataDaos.size(); i++) {
            int dutyType = signDataDaos.get(i).getDutyType();
            if (1 == dutyType) {
                //上午的签到
                amSignData.add(signDataDaos.get(i));
                //上午签到的数据
                //SignDataDao signDataDao = signDataDaos.get(i);
                //判断
                // receiverNetworkIsSign(signDataDao);
            } else if (2 == dutyType) {
                //下午的签到
                pmSignData.add(signDataDaos.get(i));
            }
        }
        /**
         * 获取迭代器iterator
         */
        iteratorAM = amSignData.iterator();
        iteratorPM = pmSignData.iterator();
        if (iteratorAM.hasNext()) {
            /**
             *  上午签到集合有数据
             *  执行上午的
             */
            iteratorSubmit(iteratorAM);
            isContinueExecetue = false;
            //调用第一次
            TAG=1;
        } else {
            //只有下午签到数据
            iteratorSubmit(iteratorPM);
            isContinueExecetue = false;
        }
    }

    /**
     * 迭代执行
     *
     * @param iterSign
     */
    public void iteratorSubmit(Iterator<SignDataDao> iterSign) {
        /**
         * 获取迭代器
         */
        while (isContinueExecetue && iterSign.hasNext()) {
            //
            SignDataDao signDataDao = iterSign.next();
            //判断能否提交
            receiverNetworkIsSign(signDataDao);
            isContinueExecetue = false;
        }

    }

    /**
     * 查询有无签到数据
     *
     * @return
     */
    private List<SignDataDao> queryData() {
        List<SignDataDao> signDataList = dbManager.queryAllList(dbManager.getQueryBuiler());
        if (signDataList != null) {
            return signDataList;
        }
        return null;
    }

    /**
     * 接受到广播中网络状态变化后，判断是否重复提交
     * 注意class 空指针
     */
    private synchronized void receiverNetworkIsSign(final SignDataDao signDataDao) {
        SignsModel.getInstance().recevicerIsSign(signDataDao.getUserId(), signDataDao.getDutyDate(),
                signDataDao.getDutyType(), new ISignsModel.ReceiverIsSignListener() {

                    @Override
                    public void Success() {
                        Log.i("test", "可以提交数据");
                        //可以提交数据
                        isFileExists(signDataDao);
                        //继续执行提交任务
                    }

                    @Override
                    public void Failed() {
                        Log.i("test", "不可以提交数据");
                        //不能提交删除数据
                        deleteSingData(signDataDao.getId());
                        //执行队列下一个任务
                        myHandler.sendEmptyMessage(0);
                    }

                    @Override
                    public void NetWorKException() {
                        Log.i("test", "检测异常");
                        //执行队列下一个任务
                        myHandler.sendEmptyMessage(0);
                    }
                });

    }

    /**
     * 检测文件是否存在
     *
     * @param signDataDao
     */
    private void isFileExists(SignDataDao signDataDao) {
        /**
         * 判断文件是否存在
         */
        String filePath = signDataDao.getFilePath();
        File file = new File(filePath);
        if (!file.exists()) {
            //删掉数据  没有图片就删除数据
            deleteSingData(signDataDao.getId());
        } else {
            Long id = signDataDao.getId();
            String userId = signDataDao.getUserId();
            String dutyDate = signDataDao.getDutyDate();
            //打卡类型
            int dutyType = signDataDao.getDutyType();
            String location = signDataDao.getLocation();
            String lng = signDataDao.getLng();
            String lat = signDataDao.getLat();
            //特情
            String type = signDataDao.getTeqingType();
            String rebark = signDataDao.getRebark();
            onReceiveUpLoad(id, userId, dutyDate, dutyType + "", location, lng, lat, type, rebark, file);
        }
    }


    /**
     * 上传
     *
     * @param UserId
     * @param DutyDate
     * @param DutyType
     * @param Location
     * @param Lng
     * @param lat
     * @param Type
     * @param rebark
     * @param file
     */
    private void onReceiveUpLoad(final Long Id, String UserId, String DutyDate, String
            DutyType, String Location, String Lng, String lat, String
                                         Type, String rebark, File file) {
        SignsModel.getInstance().SubmitOnReceive(UserId, DutyDate, DutyType, Location, Lng, lat,
                Type, rebark, file, new ISignsModel.OnReceiverSubmitSignListener() {
                    @Override
                    public void onSubmitSuccess() {
                        //删除
                        deleteSingData(Id);
                        Log.i("test", "提交成功");
                        //执行队列下一个任务
                        myHandler.sendEmptyMessage(0);
                    }

                    @Override
                    public void onSubmitFailed() {
                        //删除
                        deleteSingData(Id);
                        Log.i("test", "提交失败");
                        //执行队列下一个任务
                        myHandler.sendEmptyMessage(0);

                    }

                    @Override
                    public void NetWorKException() {
                        Log.i("test", "异常");
                        //执行队列下一个任务
                        myHandler.sendEmptyMessage(0);
                    }
                });
    }

    /**
     * 通过主键删除数据
     *
     * @param id
     */
    private void deleteSingData(Long id) {
        Log.i("test", "删除：" + id);
        dbManager.deleteByID(id);
    }

    public class MyHandler extends Handler {

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            //继续发送
            isContinueExecetue = true;
            //继续迭代
            if (iteratorAM.hasNext()){
                //有上午的签到数据
                //上传上午的数据
                if (TAG<=amSignData.size()){
                    //继续传上午的数据
                    iteratorSubmit(iteratorAM);
                    TAG++;
                }else{
                    //下午签到的数据
                    iteratorSubmit(iteratorPM);
                }

            }else {
                iteratorSubmit(iteratorPM);
            }


        }
    }

}
