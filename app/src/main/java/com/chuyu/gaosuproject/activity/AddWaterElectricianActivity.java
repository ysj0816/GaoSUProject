package com.chuyu.gaosuproject.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bigkoo.svprogresshud.SVProgressHUD;
import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.bean.logmanagebean.ManageLog;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.dao.DBManager;
import com.chuyu.gaosuproject.util.NetworkUtils;
import com.chuyu.gaosuproject.util.OtherUtils;
import com.chuyu.gaosuproject.util.SPUtils;
import com.chuyu.gaosuproject.util.ToastUtils;
import com.chuyu.gaosuproject.util.upload.OnWifiUpLoadLog;
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
 * 添加水电工日志
 */
public class AddWaterElectricianActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.layout_back)
    LinearLayout layoutBack;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_watersupplysituation)
    MyEditText tvWatersupplysituation;
    @BindView(R.id.tv_powersupplysituation)
    MyEditText tvPowersupplysituation;
    @BindView(R.id.tv_repairsituation)
    MyEditText tvRepairsituation;
    @BindView(R.id.tv_other)
    MyEditText tvOther;
    @BindView(R.id.bt_submit)
    Button btSubmit;
    @BindView(R.id.tv_waterlog)
    TextView tvWaterlog;
    @BindView(R.id.remarkLenth1)
    TextView remarkLenth1;
    @BindView(R.id.remarkLenth2)
    TextView remarkLenth2;
    @BindView(R.id.remarkLenth3)
    TextView remarkLenth3;
    @BindView(R.id.remarkLenth4)
    TextView remarkLenth4;
    private String tag;
    private String workdiaryid;
    private String authoruserid;
    private String finishwork;
    private String unnfinishwork;
    private String needassistwork;
    private String remark;
    private String userid;
    private SVProgressHUD svProgressHUD;
    private DBManager<ManageLog> dbManager;
    private String edtWatersupplysituation;
    private String edtPowersupplysituation;
    private String edtRepairsituation;
    private String edtOther;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_water_electrician);
        ButterKnife.bind(this);
        initview();
        initdata();
    }

    private void initdata() {
        setEdittextLenth();
        tvWatersupplysituation.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        tvPowersupplysituation.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        tvRepairsituation.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        tvOther.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
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
        tvWatersupplysituation.addTextChangedListener(tw);
        tvPowersupplysituation.addTextChangedListener(tw);
        tvRepairsituation.addTextChangedListener(tw);
        tvOther.addTextChangedListener(tw);
    }

    /**
     * 设置edittext 的长度
     */
    private void setEdittextLenth() {
        int length1 = tvWatersupplysituation.getText().toString().trim().length();
        int length2= tvPowersupplysituation.getText().toString().trim().length();
        int length3 = tvRepairsituation.getText().toString().trim().length();
        int length4 = tvOther.getText().toString().trim().length();


        remarkLenth1.setText(length1+"/100");
        remarkLenth2.setText(length2+"/100");
        remarkLenth3.setText(length3+"/100");
        remarkLenth4.setText(length4+"/100");
    }

    private void initview() {
        svProgressHUD = new SVProgressHUD(this);
        userid = (String) SPUtils.get(getApplicationContext(), SPConstant.USERID, "");
        tag = getIntent().getStringExtra("tag");
        if (tag.equals("one")) {
            String currenttime = OtherUtils.GetcurrentTime();
            Log.i("test", "当前时间:" + currenttime);
            tvTime.setText(currenttime);
//            tvWaterlog.setText("首页");
        } else if (tag.equals("two")) {
//            tvWaterlog.setText("日志管理");
            workdiaryid = getIntent().getStringExtra("workdiaryid");
            //用户ID
            authoruserid = getIntent().getStringExtra("authoruserid");
            //供水情况
            finishwork = getIntent().getStringExtra("finishwork");
            //供电情况
            unnfinishwork = getIntent().getStringExtra("unnfinishwork");
            //维修保养情况
            needassistwork = getIntent().getStringExtra("needassistwork");
            //其它
            remark = getIntent().getStringExtra("remark");
            String creatime = getIntent().getStringExtra("creatime");//时间
            if (!finishwork.equals("")) {
                tvWatersupplysituation.setText(finishwork);
            }
            if (!unnfinishwork.equals("")) {
                tvPowersupplysituation.setText(unnfinishwork);
            }
            if (!needassistwork.equals("")) {
                tvRepairsituation.setText(needassistwork);
            }
            if (!remark.equals("")) {
                tvOther.setText(remark);
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

                finish();
                break;
            case R.id.bt_submit:
                SubmitWaterElectrician();
                break;
            default:
                break;
        }
    }

    private void SubmitWaterElectrician() {
        //供水
        edtWatersupplysituation = tvWatersupplysituation.getText().toString();
        //供电
        edtPowersupplysituation = tvPowersupplysituation.getText().toString();
        //维修
        edtRepairsituation = tvRepairsituation.getText().toString();
        //其它
        edtOther = tvOther.getText().toString();
        if (TextUtils.isEmpty(edtWatersupplysituation) && TextUtils.isEmpty(edtPowersupplysituation)
                && TextUtils.isEmpty(edtRepairsituation) && TextUtils.isEmpty(edtOther)) {
            ToastUtils.show(getApplicationContext(), "上报信息不能为空");
            return;
        }
        if (tag.equals("one")) {
            if (NetworkUtils.isConnected()) {
                if (NetworkUtils.getNetworkType() == NetworkUtils.NetworkType.NETWORK_WIFI) {
                    Log.i("test", "水电工数据已提交");
                    submitwaterLog();
                } else {
                    new AlertDialog(this)
                            .builder()
                            .setMsg("当前网络不是wifi,将使用流量,确认提交吗?")
                            .setTitle("确认提交")
                            .setPositiveButton("确认", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    submitwaterLog();
                                }
                            })
                            .setNegativeButton("取消", new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    /**
                                     * 取消后，提示数据缓存
                                     */
                                    cacheSignData();
                                    svProgressHUD.showInfoWithStatus("数据已缓存，将在WiFi状态下自动提交！");

                                }


                            })
                            .show();
                }
            } else {
                cacheSignData();
                svProgressHUD.showInfoWithStatus("无网络，数据已缓存，将在WiFi状态下自动提交！");
            }

        } else if (tag.equals("two")) {
            //修改水电工日志
            OkGo.post(UrlConstant.formatUrl(UrlConstant.UpdateDairyURL))
                    .params("WorkDiaryID", workdiaryid)
                    .params("AuthorUserID", authoruserid)
                    .params("FinishWork", edtWatersupplysituation)
                    .params("UnFinishWork", edtPowersupplysituation)
                    .params("NeedAssistWork", edtRepairsituation)
                    .params("Remark", edtOther)
                    .execute(new StringCallback() {
                        @Override
                        public void onSuccess(String s, Call call, Response response) {
                            Log.i("test", "sss:" + s);
                            svProgressHUD.dismissImmediately();
                            try {
                                JSONObject jsonObject = new JSONObject(s);
                                String msg = jsonObject.getString("msg");
                                boolean success = jsonObject.getBoolean("success");
                                if (success) {
                                    ToastUtils.show(getApplicationContext(), msg);
                                    Intent intent = new Intent(AddWaterElectricianActivity.this, LogManageActivity.class);
                                    intent.putExtra("tempflag", "2");
                                    startActivity(intent);
                                    finish();
                                }
                            } catch (JSONException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
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
                            svProgressHUD.showErrorWithStatus("修改失败...");
                        }
                    });
        }
    }

    /**
     * 离线缓存数据
     */
    private void cacheSignData() {
        String userId = (String) SPUtils.get(this, SPConstant.USERID, "");
        String time = OtherUtils.GetcurrentTime();
        ManageLog manageLog = new ManageLog(null, userId, time, tvWatersupplysituation.getText().toString(),
                tvPowersupplysituation.getText().toString(),
                tvRepairsituation.getText().toString(),
                tvOther.getText().toString(), "2");

        OnWifiUpLoadLog.getInstace().getDbManager().insertObj(manageLog);

    }

    private void submitwaterLog() {
        //上报水电工日志
        OkGo.post(UrlConstant.formatUrl(UrlConstant.AddmManagerLogUrL))
                .params("AuthorUserID", userid)
                .params("CreateTime", tvTime.getText().toString())
                .params("FinishWork", edtWatersupplysituation)
                .params("UnFinishWork", edtPowersupplysituation)
                .params("NeedAssistWork", edtRepairsituation)
                .params("Remark", edtOther)
                .params("Category", "2")
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        Log.i("test", "sss:" + s);
                        svProgressHUD.dismissImmediately();
                        try {
                            JSONObject jsonObject = new JSONObject(s);
                            String msg = jsonObject.getString("msg");
                            boolean success = jsonObject.getBoolean("success");
                            if (success) {
                                ToastUtils.show(getApplicationContext(), msg);
                                Intent intent = new Intent(AddWaterElectricianActivity.this, LogManageActivity.class);
                                intent.putExtra("tempflag", "2");
                                startActivity(intent);
                                finish();
                            } else {
                                ToastUtils.show(getApplicationContext(), msg);
                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
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
                        svProgressHUD.showErrorWithStatus("提交失败...");
                    }
                });
    }
}
