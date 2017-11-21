package com.chuyu.gaosuproject.model.interfacemodel;

/**
 * 打卡记录model
 * Created by wo on 2017/7/15.
 */

public interface ISignNodeModel {

    /**
     * 查看个人签到记录
     * @param userid
     * @param getSignNodeListener
     */
    void getOneselfNode(String userid,GetSignNodeListener getSignNodeListener);

    /**
     * 查看个人的单日考勤记录
     * @param userid
     * @param dutyDate
     * @param getOneSelfNodeDateListener
     */
    void getOneSelfNodeDate(String userid,String dutyDate,GetOneSelfNodeDateListener getOneSelfNodeDateListener);


    /**
     * 获取所有人的签到记录
     * @param userid
     * @param getAllPresonNodeListener
     */
    void getAllPresonNode(String userid,GetAllPresonNodeListener getAllPresonNodeListener);


    /**
     * 删除当日考勤记录
     * @param userid
     * @param dutyType
     * @param currentDate
     * @param deleteSignNodeListener
     */
    void deleteSignNodeDate(String userid,int dutyType,String currentDate,DeleteSignNodeListener deleteSignNodeListener);

    /**
     * 获取未打卡考勤监听
     * @param userid
     * @param date
     * @param notSignListener
     */
    void notSignNode(String userid,String date,NotSignListener notSignListener);
    /**
     * 获取签到记录监听
     */
    interface GetSignNodeListener{
        void getDateSuccess(String s);
        void getFaile();
        void shwoExpcetion(String msg);

    }

    /**
     * 获取个人的单日考勤记录监听
     */
    interface GetOneSelfNodeDateListener{
        void getDateSuccess(String s);
        void getFaile();
        void shwoExpcetion(String msg);
    }

    /**
     * 获取所有人的签到记录监听
     */
    interface GetAllPresonNodeListener{
        void getDateSuccess(String s);
        void getFaile();
        void shwoExpcetion(String msg);
    }

    /**
     *删除当日考勤记录监听
     */
    interface DeleteSignNodeListener{
        void getDateSuccess(String s);
        void getFaile();
        void shwoExpcetion(String msg);
    }

    /**
     * 获取未打卡监听
     */
    interface NotSignListener{
        void getDateSuccess(String s);
        void getFaile();
        void shwoExpcetion(String msg);
    }
}
