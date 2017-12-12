package com.chuyu.gaosuproject.activity;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.bean.urlip.IPPath;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.dao.DBManager;
import com.chuyu.gaosuproject.util.IPPathDb;
import com.chuyu.gaosuproject.util.SPUtils;
import com.chuyu.gaosuproject.util.ToastUtils;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wo on 2017/11/7.
 * 服务器IP设置
 */

public class ServiceIPActivity extends AppCompatActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.edit_ip)
    EditText editIp;
    @BindView(R.id.edit_port)
    EditText editPort;
    @BindView(R.id.bt_recover)
    Button btRecover;
    @BindView(R.id.bt_save)
    Button btSave;

    private SVProgressHUD svProgressHUD;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_ip);
        ButterKnife.bind(this);
        svProgressHUD = new SVProgressHUD(this);
        DBManager<IPPath> dbManager = IPPathDb.getInstace().getDbManager();
        List<IPPath> ipPaths = dbManager.queryAllList(dbManager.getQueryBuiler());
        Log.i("test","IP地址："+ipPaths.toString()+"\n几个："+ipPaths.size());
        IPPath ipPath = ipPaths.get(0);
        editIp.setText(ipPath.getIp());
        editPort.setText(ipPath.getPort());
        editIp.setSelection(ipPath.getIp().length());
    }

    @OnClick({R.id.iv_back, R.id.bt_recover, R.id.bt_save})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.bt_recover:
                //恢复默认设置
                editIp.setText("219.139.79.56");
                editPort.setText("8000");
                String ips = editIp.getText().toString().trim();
                String ports=editPort.getText().toString().trim();
                //赋值
                UrlConstant.IP=ips;
                UrlConstant.PORT=ports;
                //保存
                IPPathDb pathDb = IPPathDb.getInstace();
                IPPath ipPath = new IPPath(null,ips, ports);
                pathDb.updateIP(ipPath);
                break;
            case R.id.bt_save:
                //保存修改后的IP
                String ip = editIp.getText().toString().trim();
                String port=editPort.getText().toString().trim();
                if (TextUtils.isEmpty(ip)||TextUtils.isEmpty(port)){
                    //提示
                    svProgressHUD.showInfoWithStatus("IP和端口不能为空");
                }else {
                    //赋值
                    UrlConstant.IP=ip;
                    UrlConstant.PORT=port;
                    //保存
                    boolean ip1 = isIP(ip);
                    IPPathDb pathDbtace = IPPathDb.getInstace();
                    Long i= Long.valueOf(1);
                    IPPath ipPaths = new IPPath(i,ip, port);
                    pathDbtace.updateIP(ipPaths);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            svProgressHUD.showSuccessWithStatus("保存成功！");
                        }
                    },800);
                }

                break;
            default:
                break;
        }
    }

    public static boolean isIP(String ip){
        Pattern compile = Pattern.compile("^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
                + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$");
        Matcher m = compile.matcher(ip);
        return m.matches();
    }
}
