package com.chuyu.gaosuproject.bean;

/**
 * Created by wo on 2017/7/17.
 */

public class SignQIanBean {
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
        return "SignQIanBean{" +
                "success=" + success +
                ", msg='" + msg + '\'' +
                '}';
    }
}
