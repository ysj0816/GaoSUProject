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
    private String userid;
    //打卡时间
    private String dutydate;
    //签到特请  请假类型（请假 休假）
    private String type;
    //签到类型  上下班 请假
    private int dutytype;
    private String location;
    private String lng;
    private String lat;
    private String startdate;
    private String enddate;
    private String rebark;
    @NotNull
    private String image;

    @Generated(hash = 1459645009)
    public SignAndLeaveData(Long id, String userid, String dutydate, String type,
            int dutytype, String location, String lng, String lat, String startdate,
            String enddate, String rebark, @NotNull String image) {
        this.id = id;
        this.userid = userid;
        this.dutydate = dutydate;
        this.type = type;
        this.dutytype = dutytype;
        this.location = location;
        this.lng = lng;
        this.lat = lat;
        this.startdate = startdate;
        this.enddate = enddate;
        this.rebark = rebark;
        this.image = image;
    }

    public SignAndLeaveData( String userid, String dutydate, String type,
                            int dutytype, String location, String lng, String lat, String startdate,
                            String enddate, String rebark, @NotNull String image) {
        this.userid = userid;
        this.dutydate = dutydate;
        this.type = type;
        this.dutytype = dutytype;
        this.location = location;
        this.lng = lng;
        this.lat = lat;
        this.startdate = startdate;
        this.enddate = enddate;
        this.rebark = rebark;
        this.image = image;
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

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getDutydate() {
        return dutydate;
    }

    public void setDutydate(String dutydate) {
        this.dutydate = dutydate;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getDutytype() {
        return dutytype;
    }

    public void setDutytype(int dutytype) {
        this.dutytype = dutytype;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getLng() {
        return lng;
    }

    public void setLng(String lng) {
        this.lng = lng;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getRebark() {
        return rebark;
    }

    public void setRebark(String rebark) {
        this.rebark = rebark;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
