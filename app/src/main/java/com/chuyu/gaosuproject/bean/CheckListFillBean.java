package com.chuyu.gaosuproject.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14. 检查表填报公益检查单位
 */

public class CheckListFillBean {
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String message;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public boolean success;

    public List<CheckListFillDate> getData() {
        return data;
    }

    public void setData(List<CheckListFillDate> data) {
        this.data = data;
    }

    public List<CheckListFillDate> data;

   public static class CheckListFillDate

    {
        private boolean isSelected = false;

        @Override
        public String toString() {
            return "CheckListFillDate{" +
                    "checkproject='" + checkproject + '\'' +
                    ", oid='" + oid + '\'' +
                    ", isNewRecord='" + isNewRecord + '\'' +
                    '}';
        }

        public String checkproject;
        public String oid;

        public String getCheckproject() {
            return checkproject;
        }

        public void setCheckproject(String checkproject) {
            this.checkproject = checkproject;
        }

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getIsNewRecord() {
            return isNewRecord;
        }

        public void setIsNewRecord(String isNewRecord) {
            this.isNewRecord = isNewRecord;
        }

        public String isNewRecord;

        public boolean isSelected() {
            return isSelected;
        }
        public void setSelected(boolean isSelected) {
            this.isSelected = isSelected;
        }

    }
}
