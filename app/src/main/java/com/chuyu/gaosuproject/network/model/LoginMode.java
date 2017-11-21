package com.chuyu.gaosuproject.network.model;

import com.chuyu.gaosuproject.bean.LoginBean;

import java.io.Serializable;

/**
 * Created by wo on 2017/7/13.
 */

public class LoginMode implements Serializable {
    private static final long serialVersionUID = -828322761336296999L;

    private LoginBean data;

    public LoginBean getData() {
        return data;
    }

    public void setData(LoginBean data) {
        this.data = data;
    }
}
