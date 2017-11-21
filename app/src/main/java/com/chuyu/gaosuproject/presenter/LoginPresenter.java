package com.chuyu.gaosuproject.presenter;

import com.chuyu.gaosuproject.base.BasePresenter;
import com.chuyu.gaosuproject.bean.LoginBean;
import com.chuyu.gaosuproject.model.LoginModel;
import com.chuyu.gaosuproject.model.interfacemodel.ILoginModel;
import com.chuyu.gaosuproject.view.ILoginView;

/**
 * Created by wo on 2017/7/13.
 * 登录功能
 * mvp中 presenter
 */

public class LoginPresenter extends BasePresenter<ILoginView> {

    public void logIn(String username,String password){
        if(isViewAttached()){

        }else {
            return;
        }
        final ILoginView view = getView();
        view.showWaiting();
        LoginModel.getInsance().login(username, password, new ILoginModel.LoginLisenter() {
            @Override
            public void textEmpty() {
                view.closeWaiting();
                view.showExpetion("账号或密码为空");
            }

            @Override
            public void netWorkExpetion() {
                view.closeWaiting();
                view.showExpetion("网络异常，请检查网络");
            }

            @Override
            public void loginSuccess(LoginBean loginBean) {
                view.closeWaiting();
                view.loginSuccess(loginBean);
            }

            @Override
            public void loginFailed() {
                view.closeWaiting();
                view.showFaile();
            }
        });
    }


}
