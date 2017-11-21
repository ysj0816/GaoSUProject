package com.chuyu.gaosuproject.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/7/14. 经营检查项目单位
 */

public class ManagementCheckProjectCompany {
    public List<Row> rows;
    public String total;

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<Row> getRows() {
        return rows;
    }

    public void setRows(List<Row> rows) {
        this.rows = rows;
    }
    public class Row{
        public String CreateTime;
        public String CreateUser;

        @Override
        public String toString() {
            return "Row{" +
                    "CreateTime='" + CreateTime + '\'' +
                    ", CreateUser='" + CreateUser + '\'' +
                    ", DeptId='" + DeptId + '\'' +
                    ", Id='" + Id + '\'' +
                    ", ModifyTime='" + ModifyTime + '\'' +
                    ", ModifyUser='" + ModifyUser + '\'' +
                    ", UnitCategory='" + UnitCategory + '\'' +
                    ", UnitName='" + UnitName + '\'' +
                    '}';
        }

        public String DeptId;
        public String Id;
        public String ModifyTime;
        public String ModifyUser;
        public String UnitCategory;
        public String UnitName;

        public String getCreateTime() {
            return CreateTime;
        }

        public void setCreateTime(String createTime) {
            CreateTime = createTime;
        }

        public String getCreateUser() {
            return CreateUser;
        }

        public void setCreateUser(String createUser) {
            CreateUser = createUser;
        }

        public String getDeptId() {
            return DeptId;
        }

        public void setDeptId(String deptId) {
            DeptId = deptId;
        }

        public String getId() {
            return Id;
        }

        public void setId(String id) {
            Id = id;
        }

        public String getModifyTime() {
            return ModifyTime;
        }

        public void setModifyTime(String modifyTime) {
            ModifyTime = modifyTime;
        }

        public String getModifyUser() {
            return ModifyUser;
        }

        public void setModifyUser(String modifyUser) {
            ModifyUser = modifyUser;
        }

        public String getUnitCategory() {
            return UnitCategory;
        }

        public void setUnitCategory(String unitCategory) {
            UnitCategory = unitCategory;
        }

        public String getUnitName() {
            return UnitName;
        }

        public void setUnitName(String unitName) {
            UnitName = unitName;
        }
    }
}
