package com.chuyu.gaosuproject.bean;

import java.util.List;

/**
 * Created by wo on 2017/7/10.
 */

public class SignBean {
    private int total;
    private boolean success;
    private List<Rows> rows;

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public List<Rows> getRows() {
        return rows;
    }

    public void setRows(List<Rows> rows) {
        this.rows = rows;
    }

    public class Rows {
        private String OID;

        private String UserId;

        private String UserName;

        private String DutyDate;

        private String DutyType;

        private String ImgUrl;

        private String Location;

        private String Lng;

        private String Lat;

        private String StartDate;

        private String EndDate;

        private String LeaveReason;

        private String Type;

        public void setOID(String OID){
            this.OID = OID;
        }
        public String getOID(){
            return this.OID;
        }
        public void setUserId(String UserId){
            this.UserId = UserId;
        }
        public String getUserId(){
            return this.UserId;
        }
        public void setUserName(String UserName){
            this.UserName = UserName;
        }
        public String getUserName(){
            return this.UserName;
        }
        public void setDutyDate(String DutyDate){
            this.DutyDate = DutyDate;
        }
        public String getDutyDate(){
            return this.DutyDate;
        }
        public void setDutyType(String DutyType){
            this.DutyType = DutyType;
        }
        public String getDutyType(){
            return this.DutyType;
        }
        public void setImgUrl(String ImgUrl){
            this.ImgUrl = ImgUrl;
        }
        public String getImgUrl(){
            return this.ImgUrl;
        }
        public void setLocation(String Location){
            this.Location = Location;
        }
        public String getLocation(){
            return this.Location;
        }
        public void setLng(String Lng){
            this.Lng = Lng;
        }
        public String getLng(){
            return this.Lng;
        }
        public void setLat(String Lat){
            this.Lat = Lat;
        }
        public String getLat(){
            return this.Lat;
        }
        public void setStartDate(String StartDate){
            this.StartDate = StartDate;
        }
        public String getStartDate(){
            return this.StartDate;
        }
        public void setEndDate(String EndDate){
            this.EndDate = EndDate;
        }
        public String getEndDate(){
            return this.EndDate;
        }
        public void setLeaveReason(String LeaveReason){
            this.LeaveReason = LeaveReason;
        }
        public String getLeaveReason(){
            return this.LeaveReason;
        }
        public void setType(String Type){
            this.Type = Type;
        }
        public String getType(){
            return this.Type;
        }

        @Override
        public String toString() {
            return "Rows{" +
                    "OID='" + OID + '\'' +
                    ", UserId='" + UserId + '\'' +
                    ", UserName='" + UserName + '\'' +
                    ", DutyDate='" + DutyDate + '\'' +
                    ", DutyType='" + DutyType + '\'' +
                    ", ImgUrl='" + ImgUrl + '\'' +
                    ", Location='" + Location + '\'' +
                    ", Lng='" + Lng + '\'' +
                    ", Lat='" + Lat + '\'' +
                    ", StartDate='" + StartDate + '\'' +
                    ", EndDate='" + EndDate + '\'' +
                    ", LeaveReason='" + LeaveReason + '\'' +
                    ", Type='" + Type + '\'' +
                    '}';
        }
    }

    @Override
    public String toString() {
        return "SignBean{" +
                "total=" + total +
                ", success=" + success +
                ", rows=" + rows +
                '}';
    }
}
