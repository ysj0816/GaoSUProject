package com.chuyu.gaosuproject.model.interfacemodel;

/**
 * Created by wo on 2017/7/14.
 */

public interface ILeaveModel {

    /**请假提交
     * @param UserId
     * @param DutyType
     * @param StartDate
     * @param EndDate
     * @param LeaveReason
     * @param type
     * @param leaveListener
     */
    void submitLeave(String UserId,int DutyType,String StartDate,
                     String EndDate,String LeaveReason, int type,LeaveListener leaveListener);

    interface LeaveListener{
        void submitSuccess();
        void submitFaile();
        void shwoExpretion(String msg);
    }

    interface ResLeaveListener{
        void isLeave();
        void notLeave();
        void shwoExpretion(String msg);
    }

    /**
     * 判断是否重复请假
     * @param UserId
     * @param DutyType
     * @param DutyDate
     * @param resLeaveListener
     */
    void judgeLeave(String UserId,int DutyType,String DutyDate,ResLeaveListener resLeaveListener);
}
