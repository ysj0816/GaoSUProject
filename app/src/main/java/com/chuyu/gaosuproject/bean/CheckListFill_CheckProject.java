package com.chuyu.gaosuproject.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14. 检查表填报(公益检查项目)
 */

public class CheckListFill_CheckProject {
    public List<CheckProject> getData() {
        return data;
    }

    public void setData(List<CheckProject> data) {
        this.data = data;
    }

    public List<CheckProject> data;

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

    public class CheckProject {
        public String oid;

        @Override
        public String toString() {
            return "CheckProject{" +
                    "oid='" + oid + '\'' +
                    ", projectid='" + projectid + '\'' +
                    ", subproject='" + subproject + '\'' +
                    ", isNewRecord='" + isNewRecord + '\'' +
                    '}';
        }

        public String projectid;

        public String getOid() {
            return oid;
        }

        public void setOid(String oid) {
            this.oid = oid;
        }

        public String getProjectid() {
            return projectid;
        }

        public void setProjectid(String projectid) {
            this.projectid = projectid;
        }

        public String getSubproject() {
            return subproject;
        }

        public void setSubproject(String subproject) {
            this.subproject = subproject;
        }

        public String getIsNewRecord() {
            return isNewRecord;
        }

        public void setIsNewRecord(String isNewRecord) {
            this.isNewRecord = isNewRecord;
        }

        public String subproject;
        public String isNewRecord;



        public boolean isSelected;

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean selected) {
            isSelected = selected;
        }


    }
}
