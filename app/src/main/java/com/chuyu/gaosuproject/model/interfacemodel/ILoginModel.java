package com.chuyu.gaosuproject.model.interfacemodel;

import com.chuyu.gaosuproject.bean.LoginBean;

/**
 * 登录
 * Created by wo on 2017/7/13.
 */

public interface ILoginModel {
    interface LoginLisenter{
        void textEmpty();
        void netWorkExpetion();
        void loginSuccess(LoginBean loginBean);
        void loginFailed();
    }

    /**
     * 登录方法
     * @param username
     * @param password
     * @param loginLisenter 登录监听
     */
    void login(String username,String password,LoginLisenter loginLisenter);
}
