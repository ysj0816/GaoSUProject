package com.chuyu.gaosuproject.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/19.
 */

public class ListJudgeAdminBean {
//    private List<JudgeAdmin> adminList;
//
//    public List<JudgeAdmin> getAdminList() {
//        return adminList;
//    }
//
//    public void setAdminList(List<JudgeAdmin> adminList) {
//        this.adminList = adminList;
//    }

    private String Address;

    private String Category;

    private String DeptCode;

    private String DeptId;

    private String DeptName;

    private String Descript;

    private boolean HasUser;

    private boolean HasUserContracts;

    private int Latitude;

    private int Longitude;

    private String ParentDept;

    private String Remark;

    private String Sort;

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getAddress() {
        return this.Address;
    }

    public void setCategory(String Category) {
        this.Category = Category;
    }

    public String getCategory() {
        return this.Category;
    }

    public void setDeptCode(String DeptCode) {
        this.DeptCode = DeptCode;
    }

    public String getDeptCode() {
        return this.DeptCode;
    }

    public void setDeptId(String DeptId) {
        this.DeptId = DeptId;
    }

    public String getDeptId() {
        return this.DeptId;
    }

    public void setDeptName(String DeptName) {
        this.DeptName = DeptName;
    }

    public String getDeptName() {
        return this.DeptName;
    }

    public void setDescript(String Descript) {
        this.Descript = Descript;
    }

    public String getDescript() {
        return this.Descript;
    }

    public void setHasUser(boolean HasUser) {
        this.HasUser = HasUser;
    }

    public boolean getHasUser() {
        return this.HasUser;
    }

    public void setHasUserContracts(boolean HasUserContracts) {
        this.HasUserContracts = HasUserContracts;
    }

    public boolean getHasUserContracts() {
        return this.HasUserContracts;
    }

    public void setLatitude(int Latitude) {
        this.Latitude = Latitude;
    }

    public int getLatitude() {
        return this.Latitude;
    }

    public void setLongitude(int Longitude) {
        this.Longitude = Longitude;
    }

    public int getLongitude() {
        return this.Longitude;
    }

    public void setParentDept(String ParentDept) {
        this.ParentDept = ParentDept;
    }

    public String getParentDept() {
        return this.ParentDept;
    }

    public void setRemark(String Remark) {
        this.Remark = Remark;
    }

    public String getRemark() {
        return this.Remark;
    }

    public void setSort(String Sort) {
        this.Sort = Sort;
    }

    public String getSort() {
        return this.Sort;
    }
}
