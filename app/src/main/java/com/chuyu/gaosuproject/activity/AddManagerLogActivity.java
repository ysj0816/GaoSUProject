package com.chuyu.gaosuproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.base.MVPBaseActivity;
import com.chuyu.gaosuproject.bean.logmanagebean.ManageLog;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.presenter.LogManagePresenter;
import com.chuyu.gaosuproject.util.NetworkUtils;
import com.chuyu.gaosuproject.util.OtherUtils;
import com.chuyu.gaosuproject.util.SPUtils;
import com.chuyu.gaosuproject.util.SVP.SVProgressHUD;
import com.chuyu.gaosuproject.util.ToastUtils;
import com.chuyu.gaosuproject.util.observer.NetChangeObserver;
import com.chuyu.gaosuproject.util.upload.OnWifiUpLoadLog;
import com.chuyu.gaosuproject.view.ILogManageView;
import com.chuyu.gaosuproject.widget.AlertDialog;
import com.chuyu.gaosuproject.widget.MyEditText;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 添加管理员日志
 */
public class AddManagerLogActivity extends MVPBaseActivity<ILogManageView, LogManagePresenter>
        implements ILogManageView, View.OnClickListener {
    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_managementchecksituation)
    MyEditText tvManagementchecksituation;
    @BindView(R.id.tv_commonwealchecksituation)
    MyEditText tvCommonwealchecksituation;
    @BindView(R.id.tv_ondutyrecord)
    MyEditText tvOndutyrecord;
    @BindView(R.id.tv_remarks)
    MyEditText tvRemarks;
    @BindView(R.id.bt_submit)
    Button btSubmit;
    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.tv_managelog)
    TextView tvManagelog;
    @BindView(R.id.remarkLenth1)
    TextView remarkLenth1;
    @BindView(R.id.remarkLenth2)
    TextView remarkLenth2;
    @BindView(R.id.remarkLenth3)
    TextView remarkLenth3;
    @BindView(R.id.remarkLenth4)
    TextView remarkLenth4;
    private String flag;
    private String workdiaryid;
    private String authoruserid;
    private String finishwork;
    private String unnfinishwork;
    private String needassistwork;
    private String remark;
    private String userid;
    private SVProgressHUD svProgressHUD;
    private NetChangeObserver observer;//网络观察者
    private String edtManagementchecksituation;
    private String edtCommonwealchecksituation;
    private String edtOndutyrecord;
    private String edtRemarks;
    private boolean isfirst = true;
    private LogManagePresenter logManagePresenter;


    @Override
    protected int initContent() {
        return R.layout.activity_add_manager_log;
    }

    @Override
    protected void initView() {
        svProgressHUD = new SVProgressHUD(this);
        userid = (String) SPUtils.get(this, SPConstant.USERID, "");
//		flag = getIntent().getStringExtra("flag");
        layoutBack.setOnClickListener(this);
        btSubmit.setOnClickListener(this);

    }

    @Override
    protected void initData() {
        setEdittextLenth();
        initdata();
        flag = getIntent().getStringExtra("flag");
        if (flag.equals("one")) {
            String currenttime = OtherUtils.GetcurrentTime();
            Log.i("test", "当前时间:" + currenttime);
            tvTime.setText(currenttime);
//            tvManagelog.setText("首页");
        } else if (flag.equals("two")) {
//            tvManagelog.setText("日志管理");
            workdiaryid = getIntent().getStringExtra("workdiaryid");
            authoruserid = getIntent().getStringExtra("authoruserid");//用户ID
            finishwork = getIntent().getStringExtra("finishwork");//经营检查情况
            unnfinishwork = getIntent().getStringExtra("unnfinishwork");//公益检查情况
            needassistwork = getIntent().getStringExtra("needassistwork");//值班记录
            remark = getIntent().getStringExtra("remark");//备注
            String creatime = getIntent().getStringExtra("creatime");//时间
            Log.i("listPath", "remark111111111:" + remark + "\n" + "creatimesss:" + creatime);
            if (!finishwork.equals("")) {
                tvManagementchecksituation.setText(finishwork);
            }
            if (finishwork.equals("")) {
                tvManagementchecksituation.setText("");
            }
            if (!unnfinishwork.equals("")) {
                tvCommonwealchecksituation.setText(unnfinishwork);
            }
            if (unnfinishwork.equals("")) {
                tvCommonwealchecksituation.setText("");
            }
            if (!needassistwork.equals("")) {
                tvOndutyrecord.setText(needassistwork);
            }
            if (needassistwork.equals("")) {
                tvOndutyrecord.setText("");
            }
            if (!remark.equals("")) {
                tvRemarks.setText(remark);
            }
            if (remark.equals("")) {
                tvRemarks.setText("");
            }
            if (!creatime.equals("")) {
                tvTime.setText(creatime);
            }
            if (creatime.equals("")) {
                String currenttime = OtherUtils.GetcurrentTime();
                Log.i("test", "当前时间:" + currenttime);
                tvTime.setText(currenttime);
            }
        }


    }

    private void initdata() {
        setEdittextLenth();
        tvManagementchecksituation.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        tvCommonwealchecksituation.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        tvOndutyrecord.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        tvRemarks.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        /**
         * 长度监听
         */
        TextWatcher tw = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                setEdittextLenth();
            }
        };
        tvManagementchecksituation.addTextChangedListener(tw);
        tvCommonwealchecksituation.addTextChangedListener(tw);
        tvOndutyrecord.addTextChangedListener(tw);
        tvRemarks.addTextChangedListener(tw);
    }

    /**
     * 设置edittext 的长度
     */
    private void setEdittextLenth() {
        int length1 = tvManagementchecksituation.getText().toString().trim().length();
        int length2= tvCommonwealchecksituation.getText().toString().trim().length();
        int length3 = tvOndutyrecord.getText().toString().trim().length();
        int length4 = tvRemarks.getText().toString().trim().length();


        remarkLenth1.setText(length1+"/100");
        remarkLenth2.setText(length2+"/100");
        remarkLenth3.setText(length3+"/100");
        remarkLenth4.setText(length4+"/100");
    }
    @Override
    protected LogManagePresenter initPresenter() {
        logManagePresenter = new LogManagePresenter();
        return logManagePresenter;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:

                finish();
                break;
            case R.id.bt_submit:
                SubmitManagerLog();
                break;
            default:
                break;
        }
    }

    private void SubmitManagerLog() {
        //经营检查情况
        edtManagementchecksituation = tvManagementchecksituation.getText().toString();
        //公益检查情况
        edtCommonwealchecksituation = tvCommonwealchecksituation.getText().toString();
        //值班记录
        edtOndutyrecord = tvOndutyrecord.getText().toString();
        //备注
        edtRemarks = tvRemarks.getText().toString();
        if (TextUtils.isEmpty(edtManagementchecksituation) && TextUtils.isEmpty(edtCommonwealchecksituation)
                && TextUtils.isEmpty(edtOndutyrecord) && TextUtils.isEmpty(edtRemarks)) {
            ToastUtils.show(getApplicationContext(), "上报信息不能为空");
            return;
        }
        Log.i("test", "进来了");
        if (flag.equals("one")) {
            if (NetworkUtils.isConnected()) {
                if (NetworkUtils.getNetworkType() == NetworkUtils.NetworkType.NETWORK_WIFI) {
                    Log.i("test", "当前wifi网络");
                    logManagePresenter.submitLog(userid, tvTime.getText().toString(), edtManagementchecksituation
                            , edtCommonwealchecksituation, edtOndutyrecord, edtRemarks, "1");
                } else {
                    new AlertDialog(this)
                            .builder()
                            .setMsg("当前网络不是wifi,将使用流量,确认提交吗?")
                            .setTitle("确认提交")
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    logManagePresenter.submitLog(userid, tvTime.getText().toString(), edtManagementchecksituation
                                            , edtCommonwealchecksituation, edtOndutyrecord, edtRemarks, "1");
                                }
                            })
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    /**
                                     * 取消后，提示数据缓存
                                     */
                                    cacheSignData();
                                    svProgressHUD.showInfoWithStatus("数据已缓存，将在WiFi状态下自动提交！",2000);
                                }
                            })
                            .show();
                }
            } else {
                cacheSignData();
                svProgressHUD.showInfoWithStatus("数据已缓存，将在WiFi状态下自动提交！",2000);
            }

        } else if (flag.equals("two")) {
            //修改管理员日志
            OkGo.post(UrlConstant.formatUrl(UrlConstant.UpdateDairyURL))
                    .params("WorkDiaryID", workdiaryid)
                    .params("AuthorUserID", authoruserid)
                    .params("FinishWork", edtManagementchecksituation)
                    .params("UnFinishWork", edtCommonwealchecksituation)
                    .params("NeedAssistWork", edtOndutyrecord)
                    .params("Remark", edtRemarks)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.i("test", s.toString());
                            svProgressHUD.dismissImmediately();
                            try {
                                JSONObject json = new JSONObject(s);
                                boolean success = json.getBoolean("success");
                                String msg = json.getString("msg");
                                if (success) {
                                    svProgressHUD.showSuccessWithStatus(msg);
                                    Intent intent = new Intent(AddManagerLogActivity.this, LogManageActivity.class);
                                    intent.putExtra("tempflag", "1");
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {

                            }
                        }

                        @Override
                        public void onBefore(BaseRequest request) {
                            super.onBefore(request);
                            svProgressHUD.showWithStatus("正在修改...");
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            svProgressHUD.dismissImmediately();
                            svProgressHUD.showErrorWithStatus("修改失败");
                        }
                    });
        }

    }

    private void cacheSignData() {
        String userId = (String) SPUtils.get(this, SPConstant.USERID, "");
        String nowtime = OtherUtils.GetcurrentTime();
        ManageLog lognamage = new ManageLog(null, userId, nowtime, tvManagementchecksituation.getText().toString(),
                tvCommonwealchecksituation.getText().toString(), tvOndutyrecord.getText().toString(),
                tvRemarks.getText().toString(), "1");
        OnWifiUpLoadLog.getInstace().getDbManager().insertObj(lognamage);

    }


    @Override
    public void showWaiting() {
        svProgressHUD.showWithStatus("正在提交");
    }

    @Override
    public void closeWaiting() {
        svProgressHUD.dismissImmediately();
    }

    @Override
    public void submitFailed() {
        svProgressHUD.showErrorWithStatus("提交失败");
    }

    @Override
    public void submitSuccess(boolean success) {
        if (success) {
            svProgressHUD.showSuccessWithStatus("提交成功");
            Intent intent = new Intent(AddManagerLogActivity.this, LogManageActivity.class);
            intent.putExtra("tempflag", "1");
            startActivity(intent);
            finish();
        }
    }

}
