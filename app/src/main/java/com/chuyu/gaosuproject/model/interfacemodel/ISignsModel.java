package com.chuyu.gaosuproject.model.interfacemodel;

import java.io.File;

/**
 * Created by wo on 2017/7/13.
 */

public interface ISignsModel {
    //三个接口

    /**
     * top1 判断签到流程是否正确
     * top2 判断是否重复签到
     * top3 提交签到
     * <p>
     * 参数包括 userid  签到时间 签到地点 经纬度 特情 备注 照片
     */
    void SignSubmit(String UserId, String DutyDate, String DutyType, String Location, String Lng, String lat,
                    String Type, String rebark, File file, SignSubmitListener signSubmitListener);

    /**
     * 判断是否重复签到
     *
     * @param userid
     * @param DutyDate
     * @param DutyType
     * @param SignListener
     */
    void rePreSign(String userid, String DutyDate, int DutyType, SignListener SignListener);

    /**
     * 广播中判断是否重复签到
     * @param userid
     * @param DutyDate
     * @param DutyType
     * @param onReceiverIsSignListener
     */
    void recevicerIsSign(String userid, String DutyDate, int DutyType, ReceiverIsSignListener onReceiverIsSignListener);

    /**
     * 在接受到广播后提交数据
     * @param UserId
     * @param DutyDate
     * @param DutyType
     * @param Location
     * @param Lng
     * @param lat
     * @param Type
     * @param rebark
     * @param file
     * @param onReceiverSubmitSignListener
     */
    void SubmitOnReceive(String UserId, String DutyDate, String DutyType, String Location, String Lng, String lat,
                         String Type, String rebark, File file,OnReceiverSubmitSignListener onReceiverSubmitSignListener);

    public interface SignSubmitListener {
        void submitSuccess();

        void submitFailed();

        void textEmpty();

        void submitExpretion(String msg);
    }

    /**
     * 判断是否重复签到监听
     */
    interface SignListener {
        void Success(String meg);

        void Failed(String meg);

        void NetWorKException(String msg);

    }

    /**
     * 广播中判断是否重复签到的监听
     */
    interface ReceiverIsSignListener {
        /**
         * 请求成功
         */
        void Success();

        /**
         * 请求失败
         */
        void Failed();

        /**
         * 网络异常
         */
        void NetWorKException();

    }

    /**
     * 广播后提交签到数据
     */
    interface OnReceiverSubmitSignListener {
        /**
         * 请求成功
         */
        void onSubmitSuccess();

        /**
         * 请求失败
         */
        void onSubmitFailed();

        /**
         * 网络异常
         */
        void NetWorKException();

    }
}
