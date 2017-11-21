package com.chuyu.gaosuproject.bean;

/**
 * Created by wo on 2017/7/15.
 */

public class IsSignBean {
    private boolean success;
    private int total;
    private String msg;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getTotal() {
        return total;
    }

    public void setTotal(int total) {
        this.total = total;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "IsSignBean{" +
                "success=" + success +
                ", total=" + total +
                ", msg='" + msg + '\'' +
                '}';
    }
}
