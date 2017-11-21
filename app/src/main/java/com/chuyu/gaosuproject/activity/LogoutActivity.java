package com.chuyu.gaosuproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.util.AppManager;
import com.chuyu.gaosuproject.util.SPUtils;
import com.chuyu.gaosuproject.widget.AlertDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 退出账号界面
 */
public class LogoutActivity extends AppCompatActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.text_name)
    TextView textName;
    @BindView(R.id.log_name)
    TextView logName;
    @BindView(R.id.bt_logout)
    Button btLogout;
    @BindView(R.id.bt_quit)
    Button btQuit;
    @BindView(R.id.log_role)
    TextView logRole;
    @BindView(R.id.rl_IP_set)
    RelativeLayout rlIPSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_logouts);
        ButterKnife.bind(this);
        //设置登录名 和真实姓名
        String username = (String) SPUtils.get(this, SPConstant.USERNAME, "");
        String usercode = (String) SPUtils.get(this, SPConstant.USERCODE, "");
        String userrole = (String) SPUtils.get(this, SPConstant.USERROLE, "");
        textName.setText(username);
        logName.setText(usercode);
        logRole.setText(userrole);
    }

    @OnClick({R.id.iv_back, R.id.bt_logout, R.id.bt_quit,R.id.rl_IP_set})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_logout:
                //注销
                new AlertDialog(this).builder().setTitle("退出当前账号")
                        .setMsg("确认注销？")
                        .setPositiveButton("确认注销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //先取出用户名和密码
                                //清理所有缓存数据
                                //SPUtils.clear(LogoutActivity.this);
                                //设置不是第一次进入
                                //SPUtils.put(LogoutActivity.this, SPConstant.FIRST_JOIN, false);
                                //重新设置 未登录
                                SPUtils.put(LogoutActivity.this, SPConstant.IS_LOGIN, false);
                                Intent intent = new Intent(LogoutActivity.this, LoginActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
                break;
            case R.id.bt_quit:
                //退出
                new AlertDialog(this).builder().setTitle("退出当前程序")
                        .setMsg("确定退出？")
                        .setPositiveButton("确认退出", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                //System.exit(0);
                                AppManager.getAppManager().finishAllActivity();
                                AppManager.getAppManager().AppExit(LogoutActivity.this);
                            }
                        }).setNegativeButton("取消", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                    }
                }).show();
                break;
            case R.id.rl_IP_set:
                //IP设置
                Intent intent = new Intent(this, ServiceIPActivity.class);
                startActivity(intent);
                //finish();
                break;
        }
    }
}
