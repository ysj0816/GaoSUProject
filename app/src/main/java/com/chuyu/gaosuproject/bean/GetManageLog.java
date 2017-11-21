package com.chuyu.gaosuproject.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/15. 获取管理员日志
 */

public class GetManageLog {
    public List<Logrow> rows;
    public String total;
    public boolean  success;

    public List<Logrow> getRows() {
        return rows;
    }

    public void setRows(List<Logrow> rows) {
        this.rows = rows;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
    public class Logrow{
        public String AuthorUserID;
        public String AuthorUserName;
        public String Category;
        public String CreateTime;
        public String DeptId;
        public String DeptName;
        public String FinishWork;
        public String NeedAssistWork;

        @Override
        public String toString() {
            return "{" +
                    "AuthorUserID='" + AuthorUserID + '\'' +
                    ", AuthorUserName='" + AuthorUserName + '\'' +
                    ", Category='" + Category + '\'' +
                    ", CreateTime='" + CreateTime + '\'' +
                    ", DeptId='" + DeptId + '\'' +
                    ", DeptName='" + DeptName + '\'' +
                    ", FinishWork='" + FinishWork + '\'' +
                    ", NeedAssistWork='" + NeedAssistWork + '\'' +
                    ", ReceiveUserID='" + ReceiveUserID + '\'' +
                    ", ReceiveUsername='" + ReceiveUsername + '\'' +
                    ", Remark='" + Remark + '\'' +
                    ", UnFinishWork='" + UnFinishWork + '\'' +
                    ", WorkDiaryID='" + WorkDiaryID + '\'' +
                    '}';
        }

        public String ReceiveUserID;
        public String ReceiveUsername;
        public String Remark;

        public String getAuthorUserID() {
            return AuthorUserID;
        }

        public void setAuthorUserID(String authorUserID) {
            AuthorUserID = authorUserID;
        }

        public String getAuthorUserName() {
            return AuthorUserName;
        }

        public void setAuthorUserName(String authorUserName) {
            AuthorUserName = authorUserName;
        }

        public String getCategory() {
            return Category;
        }

        public void setCategory(String category) {
            Category = category;
        }

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String createTime) {
            CreateTime = createTime;
        }

        public String getDeptId() {
            return DeptId;
        }

        public void setDeptId(String deptId) {
            DeptId = deptId;
        }

        public String getDeptName() {
            return DeptName;
        }

        public void setDeptName(String deptName) {
            DeptName = deptName;
        }

        public String getFinishWork() {
            return FinishWork;
        }

        public void setFinishWork(String finishWork) {
            FinishWork = finishWork;
        }

        public String getNeedAssistWork() {
            return NeedAssistWork;
        }

        public void setNeedAssistWork(String needAssistWork) {
            NeedAssistWork = needAssistWork;
        }

        public String getReceiveUserID() {
            return ReceiveUserID;
        }

        public void setReceiveUserID(String receiveUserID) {
            ReceiveUserID = receiveUserID;
        }

        public String getReceiveUsername() {
            return ReceiveUsername;
        }

        public void setReceiveUsername(String receiveUsername) {
            ReceiveUsername = receiveUsername;
        }

        public String getRemark() {
            return Remark;
        }

        public void setRemark(String remark) {
            Remark = remark;
        }

        public String getUnFinishWork() {
            return UnFinishWork;
        }

        public void setUnFinishWork(String unFinishWork) {
            UnFinishWork = unFinishWork;
        }

        public String getWorkDiaryID() {
            return WorkDiaryID;
        }

        public void setWorkDiaryID(String workDiaryID) {
            WorkDiaryID = workDiaryID;
        }

        public String UnFinishWork;
        public String WorkDiaryID;
    }
}
