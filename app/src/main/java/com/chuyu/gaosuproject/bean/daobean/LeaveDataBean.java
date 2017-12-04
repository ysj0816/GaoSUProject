package com.chuyu.gaosuproject.bean.daobean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author 杨仕俊
 * @description  请假缓存的bean
 * Created by wo on 2017/12/4.
 */
@Entity
public class LeaveDataBean {
    @Id(autoincrement = true)
    Long id;
    String userid;
    @NotNull
    String startDate;
    @NotNull
    String endData;
    @NotNull
    String reason;
    String nowDate;
    /**
     * 签到类型 3 代表请假
     */
    int dutyType;
    /**
     * 请假类型 1 调休；2请假（病事假）
     */
    int leaveType;


    @Generated(hash = 1989027244)
    public LeaveDataBean(Long id, String userid, @NotNull String startDate,
            @NotNull String endData, @NotNull String reason, String nowDate,
            int dutyType, int leaveType) {
        this.id = id;
        this.userid = userid;
        this.startDate = startDate;
        this.endData = endData;
        this.reason = reason;
        this.nowDate = nowDate;
        this.dutyType = dutyType;
        this.leaveType = leaveType;
    }

    @Generated(hash = 1820125645)
    public LeaveDataBean() {
    }


    public String getNowDate() {
        return nowDate;
    }

    public void setNowDate(String nowDate) {
        this.nowDate = nowDate;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndData() {
        return endData;
    }

    public void setEndData(String endData) {
        this.endData = endData;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public int getDutyType() {
        return dutyType;
    }

    public void setDutyType(int dutyType) {
        this.dutyType = dutyType;
    }

    public int getLeaveType() {
        return leaveType;
    }

    public void setLeaveType(int leaveType) {
        this.leaveType = leaveType;
    }
}
