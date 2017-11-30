package com.chuyu.gaosuproject.bean.daobean;

import org.greenrobot.greendao.annotation.Convert;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.NotNull;

import java.io.File;

/**
 * @author 杨仕俊
 * @description 签到数据库操作
 * Created by wo on 2017/11/28.
 */
@Entity
public class SignDataDao {
    /**
     * 主键自增
     */
    @Id(autoincrement = true)
    Long id;
    private String UserId;
    //打卡时间
    private String DutyDate;
    //日期
    private String Date;
    //特请
    private String teqingType;
    //签到类型  上下班 请假
    private int DutyType;
    private String Location;
    private String Lng;
    private String lat;
    private String rebark;
    @NotNull
    private String filePath;

    @Generated(hash = 703468959)
    public SignDataDao(Long id, String UserId, String DutyDate, String Date,
            String teqingType, int DutyType, String Location, String Lng,
            String lat, String rebark, @NotNull String filePath) {
        this.id = id;
        this.UserId = UserId;
        this.DutyDate = DutyDate;
        this.Date = Date;
        this.teqingType = teqingType;
        this.DutyType = DutyType;
        this.Location = Location;
        this.Lng = Lng;
        this.lat = lat;
        this.rebark = rebark;
        this.filePath = filePath;
    }

    @Generated(hash = 458044818)
    public SignDataDao() {
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

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getTeqingType() {
        return teqingType;
    }

    public void setTeqingType(String teqingType) {
        this.teqingType = teqingType;
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

    public String getRebark() {
        return rebark;
    }

    public void setRebark(String rebark) {
        this.rebark = rebark;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String toString() {
        return "SignDataDao{" +
                "id=" + id +
                ", UserId='" + UserId + '\'' +
                ", DutyDate='" + DutyDate + '\'' +
                ", Date='" + Date + '\'' +
                ", teqingType='" + teqingType + '\'' +
                ", DutyType=" + DutyType +
                ", Location='" + Location + '\'' +
                ", Lng='" + Lng + '\'' +
                ", lat='" + lat + '\'' +
                ", rebark='" + rebark + '\'' +
                ", filePath='" + filePath + '\'' +
                '}';
    }
}
