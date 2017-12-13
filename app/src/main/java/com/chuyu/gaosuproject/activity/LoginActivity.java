package com.chuyu.gaosuproject.activity;

import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


import com.chuyu.gaosuproject.util.SVP.SVProgressHUD;
import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.base.MVPBaseActivity;
import com.chuyu.gaosuproject.bean.LoginBean;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.presenter.LoginPresenter;
import com.chuyu.gaosuproject.util.AppManager;
import com.chuyu.gaosuproject.util.SPUtils;
import com.chuyu.gaosuproject.util.ToastUtils;
import com.chuyu.gaosuproject.view.ILoginView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登录界面的activity
 * <p>
 * mvp 模式中的view
 */
public class LoginActivity extends
        MVPBaseActivity<ILoginView, LoginPresenter> implements ILoginView {


    @BindView(R.id.username)
    EditText username;
    @BindView(R.id.password)
    EditText password;
    @BindView(R.id.login)
    Button login;

    private LoginPresenter loginPresenter;

    private SVProgressHUD svProgressHUD;
    private String usernames;
    private String pwd;

    @Override
    public void showWaiting() {
        svProgressHUD.showWithStatus("登录中...");
    }

    @Override
    public void showFaile() {
        svProgressHUD.showErrorWithStatus("登录失败！");
    }

    @Override
    public void closeWaiting() {
        svProgressHUD.dismissImmediately();
    }

    @Override
    public void showExpetion(String i) {
        ToastUtils.show(this, i);
    }

    @Override
    public void loginSuccess(LoginBean loginBean) {
        //判断
        if (loginBean.isLogin()) {
            //保存标记 已登录
            SPUtils.put(this, SPConstant.IS_LOGIN, true);
            //保存数据
            SPUtils.put(this, SPConstant.USERNAME, loginBean.getUsername());
            SPUtils.put(this, SPConstant.USERCODE, loginBean.getUsercode());
            SPUtils.put(this, SPConstant.USERROLE, loginBean.getUserrole());
            SPUtils.put(this, SPConstant.USERID, loginBean.getUserid());
            SPUtils.put(this, SPConstant.USERTYPE, loginBean.getType());
            SPUtils.put(this,SPConstant.PASSWORD,pwd);
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            ToastUtils.show(this, loginBean.getMsg() + "，请重新输入账号！");
        }

    }

    @Override
    protected LoginPresenter initPresenter() {
        loginPresenter = new LoginPresenter();
        return loginPresenter;
    }

    @Override
    protected int initContent() {
        AppManager.getAppManager().addActivity(this);
        return R.layout.activity_login;
    }

    @Override
    protected void initView() {
        svProgressHUD = new SVProgressHUD(this);

    }

    @Override
    protected void initData() {
        //初始化用户名和密码
        String name= (String) SPUtils.get(this,SPConstant.USERCODE,"");
        String pwd=(String) SPUtils.get(this,SPConstant.PASSWORD,"");
        username.setText(name);
        password.setText(pwd);
        username.setSelection(name.length());

    }


    @OnClick({R.id.login, R.id.text_service})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.login:
                usernames = username.getText().toString().trim();
                pwd = password.getText().toString().trim();
                loginPresenter.logIn(usernames, pwd);
                /*Log.i("test","地址："+ UrlConstant.formatUrl(UrlConstant.LOGINURL));
                ToastUtils.show(this,"地址："+ UrlConstant.formatUrl(UrlConstant.LOGINURL));
                boolean ping = NetworkUtils.isAvailableByPing(UrlConstant.IP);
                Log.i("test","ping："+ UrlConstant.IP+"\n是否ping:"+ping);
                ToastUtils.show(this,"地址："+ UrlConstant.IP+"\n是否ping:"+ping);*/
                break;
            case R.id.text_service:
                startActivity(new Intent(this, ServiceIPActivity.class));
                break;
            default:
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exit();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void exit() {
        AppManager.getAppManager().finishAllActivity();
        System.exit(0);

    }
}
