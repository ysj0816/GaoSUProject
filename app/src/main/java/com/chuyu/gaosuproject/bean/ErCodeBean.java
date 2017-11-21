package com.chuyu.gaosuproject.bean;

/**
 * Created by wo on 2017/7/19.
 */

public class ErCodeBean {
    private boolean flag;

    private Msg msg;

   class Msg {
        private String DeptCode;

        private String ProjectId;

        private String checktype;

        private String checklevel;

    public void setDeptCode(String DeptCode){
        this.DeptCode = DeptCode;
    }
    public String getDeptCode(){
        return this.DeptCode;
    }
    public void setProjectId(String ProjectId){
        this.ProjectId = ProjectId;
    }
    public String getProjectId(){
        return this.ProjectId;
    }
    public void setChecktype(String checktype){
        this.checktype = checktype;
    }
    public String getChecktype(){
        return this.checktype;
    }
    public void setChecklevel(String checklevel){
        this.checklevel = checklevel;
    }
    public String getChecklevel(){
        return this.checklevel;
    }

}
}
