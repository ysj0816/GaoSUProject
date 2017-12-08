package com.chuyu.gaosuproject.bean.daobean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.NotNull;
import org.greenrobot.greendao.annotation.Generated;

/**
 * @author 杨仕俊
 * @description 用保存到数据库的签到和请假数据bean
 * Created by wo on 2017/12/8.
 */
@Entity
public class SignAndLeaveData {
    /**
     * 主键自增
     */
    @Id(autoincrement = true)
    Long id;
    private String UserId;
    //打卡时间
    private String DutyDate;
    //签到特请  请假类型（请假 休假）
    private String Type;
    //签到类型  上下班 请假
    private int DutyType;
    private String Location;
    private String Lng;
    private String lat;
    private String StartDate;
    private String EndDate;
    private String rebark;
    @NotNull
    private String Imgage;

    @Generated(hash = 1741178523)
    public SignAndLeaveData(Long id, String UserId, String DutyDate, String Type,
            int DutyType, String Location, String Lng, String lat, String StartDate,
            String EndDate, String rebark, @NotNull String Imgage) {
        this.id = id;
        this.UserId = UserId;
        this.DutyDate = DutyDate;
        this.Type = Type;
        this.DutyType = DutyType;
        this.Location = Location;
        this.Lng = Lng;
        this.lat = lat;
        this.StartDate = StartDate;
        this.EndDate = EndDate;
        this.rebark = rebark;
        this.Imgage = Imgage;
    }

    @Generated(hash = 838493861)
    public SignAndLeaveData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return UserId;
    }

    public void setUserId(String userId) {
        UserId = userId;
    }

    public String getDutyDate() {
        return DutyDate;
    }

    public void setDutyDate(String dutyDate) {
        DutyDate = dutyDate;
    }

    public String getType() {
        return Type;
    }

    public void setType(String type) {
        Type = type;
    }

    public int getDutyType() {
        return DutyType;
    }

    public void setDutyType(int dutyType) {
        DutyType = dutyType;
    }

    public String getLocation() {
        return Location;
    }

    public void setLocation(String location) {
        Location = location;
    }

    public String getLng() {
        return Lng;
    }

    public void setLng(String lng) {
        Lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getRebark() {
        return rebark;
    }

    public void setRebark(String rebark) {
        this.rebark = rebark;
    }

    public String getImgage() {
        return Imgage;
    }

    public void setImgage(String imgage) {
        Imgage = imgage;
    }

    @Override
    public String toString() {
        return "SignAndLeaveData{" +
                "id=" + id +
                ", UserId='" + UserId + '\'' +
                ", DutyDate='" + DutyDate + '\'' +
                ", Type='" + Type + '\'' +
                ", DutyType=" + DutyType +
                ", Location='" + Location + '\'' +
                ", Lng='" + Lng + '\'' +
                ", lat='" + lat + '\'' +
                ", StartDate='" + StartDate + '\'' +
                ", EndDate='" + EndDate + '\'' +
                ", rebark='" + rebark + '\'' +
                ", Imgage='" + Imgage + '\'' +
                '}';
    }
}
