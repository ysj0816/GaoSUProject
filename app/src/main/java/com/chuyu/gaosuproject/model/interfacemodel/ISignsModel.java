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
     *
     * 参数包括 userid  签到时间 签到地点 经纬度 特情 备注 照片
     */
    void SignSubmit(String UserId, String DutyDate, String DutyType, String Location, String Lng, String lat,
                     String Type, String rebark, File file, SignSubmitListener signSubmitListener);


    public interface SignSubmitListener{
        void submitSuccess();
        void submitFailed();
        void textEmpty();
        void submitExpretion(String msg);
    }

    /**
     * 判断是否重复签到监听
     */
    interface SignListener{
        void Success(String meg);
        void Failed(String meg);
        void NetWorKException(String msg);

    }
    void rePreSign(String userid,String DutyDate,int DutyType,SignListener SignListener);
}
