package com.chuyu.gaosuproject.activity;

import android.annotation.TargetApi;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.util.OtherUtils;
import com.chuyu.gaosuproject.util.SPUtils;
import com.chuyu.gaosuproject.util.SystemBarTintManager;
import com.chuyu.gaosuproject.util.SystemStatusBar;
import com.chuyu.gaosuproject.util.ToastUtils;
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
public class AddManagerLogActivity extends AppCompatActivity implements View.OnClickListener {
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
    private String flag;
    private String workdiaryid;
    private String authoruserid;
    private String finishwork;
    private String unnfinishwork;
    private String needassistwork;
    private String remark;
    private String userid;
    private SVProgressHUD svProgressHUD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_manager_log);
        ButterKnife.bind(this);
        initview();
    }

    private void initview() {
        svProgressHUD = new SVProgressHUD(this);
        userid = (String) SPUtils.get(this, SPConstant.USERID, "");
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
        layoutBack.setOnClickListener(this);
        btSubmit.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.layout_back:
//                if (flag.equals("one")){
//                    Intent intent=new Intent(AddManagerLogActivity.this,MainActivity.class);
//                    startActivity(intent);
//                }else if (flag.equals("two")){
//                    finish();
//                }
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
        String edtManagementchecksituation = tvManagementchecksituation.getText().toString();//经营检查情况
        String edtCommonwealchecksituation = tvCommonwealchecksituation.getText().toString();//公益检查情况
        String edtOndutyrecord = tvOndutyrecord.getText().toString();//值班记录
        String edtRemarks = tvRemarks.getText().toString();//备注
        if (TextUtils.isEmpty(edtManagementchecksituation) && TextUtils.isEmpty(edtCommonwealchecksituation)
                && TextUtils.isEmpty(edtOndutyrecord) && TextUtils.isEmpty(edtRemarks)) {
            ToastUtils.show(getApplicationContext(), "上报信息不能为空");
            return;
        }
        Log.i("test", "进来了");
        if (flag.equals("one")) {
            //上报管理员日志
            OkGo.post(UrlConstant.formatUrl(UrlConstant.AddmManagerLogUrL))
                    .params("AuthorUserID", userid)
                    .params("CreateTime", tvTime.getText().toString())
                    .params("FinishWork", edtManagementchecksituation)
                    .params("UnFinishWork", edtCommonwealchecksituation)
                    .params("NeedAssistWork", edtOndutyrecord)
                    .params("Remark", edtRemarks)
                    .params("Category", "1")
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
                                    ToastUtils.show(getApplicationContext(), msg);
                                    Intent intent = new Intent(AddManagerLogActivity.this, LogManageActivity.class);
                                    intent.putExtra("tempflag", "1");
                                    startActivity(intent);
                                    finish();
                                } else {
                                    ToastUtils.show(getApplicationContext(), msg);
                                }
                            } catch (JSONException e) {

                            }
                        }

                        @Override
                        public void onBefore(BaseRequest request) {
                            super.onBefore(request);
                            svProgressHUD.showWithStatus("正在提交...");
                        }

                        @Override
                        public void onError(Call call, Response response, Exception e) {
                            super.onError(call, response, e);
                            svProgressHUD.dismissImmediately();
                            svProgressHUD.showErrorWithStatus("提交失败");
                        }
                    });
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
                                    ToastUtils.show(getApplicationContext(), msg);
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

}
