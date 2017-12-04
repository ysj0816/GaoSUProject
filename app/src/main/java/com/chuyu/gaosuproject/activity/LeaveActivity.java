package com.chuyu.gaosuproject.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.IdRes;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.airsaid.pickerviewlibrary.TimePickerView;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.base.MVPBaseActivity;
import com.chuyu.gaosuproject.bean.daobean.LeaveDataBean;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.dao.DBManager;
import com.chuyu.gaosuproject.presenter.LeavePresenter;
import com.chuyu.gaosuproject.receviver.NetCheckReceiver;
import com.chuyu.gaosuproject.util.DateUtils;
import com.chuyu.gaosuproject.util.NetworkUtils;
import com.chuyu.gaosuproject.util.SPUtils;
import com.chuyu.gaosuproject.util.observer.NetChangeObserver;
import com.chuyu.gaosuproject.util.upload.OnWifiLoadLeave;
import com.chuyu.gaosuproject.view.ILeaveView;
import com.chuyu.gaosuproject.widget.AlertDialog;

import org.greenrobot.greendao.query.QueryBuilder;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by wo on 2017/7/14.
 */

public class LeaveActivity extends MVPBaseActivity<ILeaveView, LeavePresenter> implements ILeaveView {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.rbtn_leave)
    RadioButton rbtnLeave;
    @BindView(R.id.rbtn_off)
    RadioButton rbtnOff;
    @BindView(R.id.edt_starttime)
    TextView edtStarttime;
    @BindView(R.id.edt_endtime)
    TextView edtEndtime;
    @BindView(R.id.edt_leaveReason)
    EditText edtLeaveReason;
    @BindView(R.id.bt_leave_submit)
    Button btLeaveSubmit;
    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.reasonLenth)
    TextView reasonLenth;
    private LeavePresenter leavePresenter;
    //时间选择器
    private TimePickerView mTimePickerView;
    private SimpleDateFormat simpleDateFormat;//时间格式
    private SVProgressHUD svProgressHUD;
    private int leaveType = 1;//请假类型
    private InputMethodManager imm;

    private NetChangeObserver mChangeObserver;//网络观察者
    public NetworkUtils.NetworkType mNetType=NetworkUtils.getNetworkType();//网络连接类型
    public boolean isAvailable=NetworkUtils.isConnected();//网络是否连接
    private String userid;
    private int dutyType = 3;//打卡类型
    private String date;
    private OnWifiLoadLeave onWifiLoadLeave;
    private DBManager<LeaveDataBean> dbManager;
    @Override
    protected LeavePresenter initPresenter() {
        leavePresenter = new LeavePresenter();
        return leavePresenter;
    }

    @Override
    protected int initContent() {
        return R.layout.activity_leave;
    }

    @OnClick({R.id.iv_back, R.id.edt_starttime, R.id.edt_endtime, R.id.bt_leave_submit, R.id.edt_leaveReason})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.edt_starttime:
                //开始时间
                setTime(1);
                break;
            case R.id.edt_endtime:
                setTime(2);
                break;
            case R.id.edt_leaveReason:
                edtLeaveReason.setCursorVisible(true);
                break;
            case R.id.bt_leave_submit:
                userid = (String) SPUtils.get(this, SPConstant.USERID, "");
                String startDate = edtStarttime.getText().toString().trim();
                String endData = edtEndtime.getText().toString().trim();
                String reason = edtLeaveReason.getText().toString().trim();
                //现在的时间
                date = DateUtils.getNowDate();
                if (TextUtils.isEmpty(startDate)) {
                    svProgressHUD.showInfoWithStatus("开始时间不能为空");
                    return;
                } else if (TextUtils.isEmpty(endData)) {
                    svProgressHUD.showInfoWithStatus("结束时间不能为空");
                    return;
                } else if (TextUtils.isEmpty(reason)) {
                    svProgressHUD.showInfoWithStatus("请假原因不能为空");
                    return;
                } else {
                    //先判断结束时间不能小于开始时间
                    boolean compareDate = DateUtils.compare_date(startDate, endData);
                    if (compareDate) {
                        /**
                         * 提示缓存
                         */
                        if (isAvailable){
                            if (mNetType== NetworkUtils.NetworkType.NETWORK_WIFI){
                                //直接判断是否能够请假
                                leavePresenter.JuGetLeave(userid, dutyType, date);
                            }else{
                                //网络连接
                                new AlertDialog(this)
                                        .builder()
                                        .setMsg("当前网络不是wifi,将使用流量,确认提交吗?")
                                        .setTitle("确认提交")
                                        .setPositiveButton("确认", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                leavePresenter.JuGetLeave(userid, dutyType, date);
                                            }
                                        })
                                        .setNegativeButton("取消", new View.OnClickListener() {
                                            @Override
                                            public void onClick(View v) {
                                                //缓存请假的数据
                                                cacheLeaveData();
                                                svProgressHUD.showInfoWithStatus("已缓存，将在WiFi状态下自动提交！");
                                            }
                                        })
                                        .show();
                            }

                        }else{
                            /**
                             * 提示缓存
                             */
                            cacheLeaveData();
                            svProgressHUD.showInfoWithStatus("无网络，数据已缓存，将在WiFi状态下自动提交！");
                        }
                    } else {
                        svProgressHUD.showInfoWithStatus("开始时间不能大于结束时间，请重新选择时间！");
                    }
                }
                break;
            default:
                break;
        }
    }

    /**
     * 请假数据缓存
     */
    private void cacheLeaveData() {
        String startDate = edtStarttime.getText().toString().trim();
        String endData = edtEndtime.getText().toString().trim();
        String reason = edtLeaveReason.getText().toString().trim();
        int type = leaveType;
        //缓存到数据库中
        LeaveDataBean leaveDataBean = new LeaveDataBean(null, userid, startDate, endData, reason, date, dutyType, type);
        dbManager.insertObj(leaveDataBean);
    }

    /**
     * 判断开始时间和结束时间
     * @param fag
     */
    private void setTime(final int fag) {
        //时间
        String strTime;
        if (fag == 1) {
            strTime = edtStarttime.getText().toString().trim();
        } else {
            strTime = edtEndtime.getText().toString().trim();
        }
        Date date = null;
        if (!TextUtils.isEmpty(strTime)) {
            try {
                date = simpleDateFormat.parse(strTime);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            date = new Date();
        }
        // 设置选中时间
        mTimePickerView.setTime(date);
        mTimePickerView.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date) {
                if (fag == 1) {
                    edtStarttime.setText(simpleDateFormat.format(date));
                } else {
                    edtEndtime.setText(simpleDateFormat.format(date));
                }
            }
        });
        mTimePickerView.show();
    }


    @Override
    protected void initView() {
        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        svProgressHUD = new SVProgressHUD(this);
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        //时间选择器
        mTimePickerView = new TimePickerView(this, TimePickerView.Type.ALL);
        //     TimePickerView 同样有上面设置样式的方法
        // 设置是否循环
        // mTimePickerView.setCyclic(true);
        // 设置滚轮文字大小
        mTimePickerView.setTextSize(TimePickerView.TextSize.SMALL);
        // 设置时间可选范围(结合 setTime 方法使用,必须在)
        Calendar calendar = Calendar.getInstance();
        mTimePickerView.setRange(calendar.get(Calendar.YEAR) - 10, calendar.get(Calendar.YEAR) + 10);
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.rbtn_off) {
                    //调休
                    leaveType = 1;
                } else {
                    //请假（病事假）
                    leaveType = 2;
                }
            }
        });
        edtLeaveReason.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        /**
         * 输入请假原因长度监听
         */
        edtLeaveReason.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                reasonLenth.setText(length+"/100");
            }
        });



    }

    @Override
    protected void initData() {
        /**
         * 网络观察者
         */
       NetChangeObserver mNetObserver= new NetChangeObserver() {
            @Override
            public void onNetConnected(NetworkUtils.NetworkType type) {

                isAvailable = true;
                mNetType = type;
                if (mNetType== NetworkUtils.NetworkType.NETWORK_WIFI){
                    //自动提交
                    Log.i("test","连接到wifi");
                    onWifiLoadLeave.upLoadLeaveData();
                }
            }

            @Override
            public void onNetDisConnect() {
                 isAvailable=false;
                Log.i("test","网络没有连接");
            }
        };
        /**
         * 添加一个网络观察者
         */
        NetCheckReceiver.registerObserver(mNetObserver);
         onWifiLoadLeave = OnWifiLoadLeave.getInstance();
         dbManager = onWifiLoadLeave.getDbManager();
        /**
         * 查询所有请假的缓存
         */
        List<LeaveDataBean> leaveDataBeen = dbManager.queryAllList(dbManager.getQueryBuiler());
        Log.i("test","leave:"+leaveDataBeen.toString());
    }

    @Override
    public void showWaiting() {
        svProgressHUD.showWithStatus("提交中...");
    }

    @Override
    public void colseWaiting() {
        svProgressHUD.dismissImmediately();
    }

    @Override
    public void shwoExpretion(String msg) {
        svProgressHUD.showInfoWithStatus(msg);
    }

    @Override
    public void leaveSuccess() {

        svProgressHUD.showSuccessWithStatus("提交成功！");
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(LeaveActivity.this, SignOnserlfActivity.class);
                startActivity(intent);
                finish();
            }
        },800);
    }

    @Override
    public void leaveFaile() {
        svProgressHUD.showErrorWithStatus("提交失败！");
    }

    @Override
    public void isLeaved(boolean isleave) {

        if (isleave) {
            String userid = (String) SPUtils.get(this, SPConstant.USERID, "");
            String startDate = edtStarttime.getText().toString().trim();
            String endData = edtEndtime.getText().toString().trim();
            String reason = edtLeaveReason.getText().toString().trim();
            int type = leaveType;
            // ToastUtils.show(this,"可以提交请假！");
            //可以提交请假
            leavePresenter.submitLeave(userid, dutyType, startDate, endData, reason, type);
        } else {
            //ToastUtils.show(this,"你已提交过一次！");
            svProgressHUD.showErrorWithStatus("网络错误！");
        }
    }


    /**
     * 分发事件
     *
     * @param ev
     * @return
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (ev.getAction() == MotionEvent.ACTION_DOWN) {
            //获取现在的焦点，即现在点击的view
            View v = getCurrentFocus();
            if (isShouldHideKeyboard(v, ev)) {
                hideKeyboard(v.getWindowToken());
            }
        }
        return super.dispatchTouchEvent(ev);
    }


    /**
     * 根据EditText所在坐标和用户点击的坐标相对比，来判断是否隐藏键盘，因为当用户点击EditText时则不能隐藏
     *
     * @param v     点击的view
     * @param event 焦点事件 ，即焦点的位置
     * @return
     */
    private boolean isShouldHideKeyboard(View v, MotionEvent event) {
        //如果现在的view实例是edittext
        if (v != null && (v instanceof EditText)) {
            int[] l = {0, 0};
            //edittext的位置（左上角坐标）
            v.getLocationInWindow(l);
            int left = l[0],
                    top = l[1],
                    bottom = top + v.getHeight(),
                    right = left + v.getWidth();
            if (event.getX() > left && event.getX() < right
                    && event.getY() > top && event.getY() < bottom) {
                //点击的位置在输入框内部
                // 点击EditText的事件，忽略它。
                return false;
            } else {
                return true;
            }
        }
        // 如果焦点不是EditText则忽略，这个发生在视图刚绘制完，第一个焦点不在EditText上，和用户用轨迹球选择其他的焦点
        return false;
    }

    /**
     * 获取InputMethodManager，隐藏软键盘
     *
     * @param token
     */
    private void hideKeyboard(IBinder token) {
        if (token != null) {
            //隐藏软键盘
            imm.hideSoftInputFromWindow(token, InputMethodManager.HIDE_NOT_ALWAYS);
        }
    }


}
