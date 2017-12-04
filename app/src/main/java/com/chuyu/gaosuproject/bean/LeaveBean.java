package com.chuyu.gaosuproject.bean;

/**
 * Created by wo on 2017/7/15.
 */

public class LeaveBean {
    private boolean success;
    private String msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }


    @Override
    public String toString() {
        return "LeaveDataBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                '}';
    }
}
