package com.chuyu.gaosuproject.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.chuyu.gaosuproject.util.SVP.SVProgressHUD;
import com.bumptech.glide.Glide;
import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.base.MVPBaseActivity;
import com.chuyu.gaosuproject.bean.dailycheck.DailyCheck;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.dao.DBManager;
import com.chuyu.gaosuproject.presenter.DailyCheckListFillPresenter;
import com.chuyu.gaosuproject.util.NetworkUtils;
import com.chuyu.gaosuproject.util.OtherUtils;
import com.chuyu.gaosuproject.util.SPUtils;
import com.chuyu.gaosuproject.util.ToastUtils;
import com.chuyu.gaosuproject.util.upload.OnWifiLoadDailyCheck;
import com.chuyu.gaosuproject.view.IDailyCheckListFillView;
import com.chuyu.gaosuproject.widget.AlertDialog;
import com.yuyh.library.imgsel.ImageLoader;
import com.yuyh.library.imgsel.ImgSelActivity;
import com.yuyh.library.imgsel.ImgSelConfig;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 日常检查表填报
 */
public class DailyCheckListFillActivity extends MVPBaseActivity<IDailyCheckListFillView,
        DailyCheckListFillPresenter> implements IDailyCheckListFillView, View.OnClickListener {


    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.tv_dailycheckunit)
    TextView tvDailycheckunit;
    @BindView(R.id.tv_dailycheckproject)
    TextView tvDailycheckproject;
    @BindView(R.id.tv_dailycheckpoints)
    EditText tvDailycheckpoints;
    @BindView(R.id.tv_dailycheckresult)
    EditText tvDailycheckresult;
    @BindView(R.id.tv_dailyspecificsituation)
    EditText tvDailyspecificsituation;
    @BindView(R.id.layout_dailyphoto)
    LinearLayout layoutDailyphoto;
    @BindView(R.id.gd_dailyimg)
    GridView gdDailyimg;
    @BindView(R.id.bt_dailysubmit)
    Button btDailysubmit;
    @BindView(R.id.remarkLenth)
    TextView remarkLenth;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    private int mColumnWidth;
    private static final int REQUEST_CODE = 0;
    private List<String> mResults;
    private GridAdapter mAdapter;
    //    private String jsuserid;
    private String checkUnitName;
    private String checkProjectName;
    private String checkId;
    private String checkUnitId;
    private String checkProjectId;
    private String userid;
    private SVProgressHUD svProgressHUD;
    private DailyCheckListFillPresenter dailyCheckListFillPresenter;
    private List<String> pathList = new ArrayList<String>();
    private String reslut;
    private String strContent;
    private String fen;

    @Override
    protected int initContent() {
        return R.layout.activity_daily_check_list_fill;
    }

    @Override
    protected void initView() {
        svProgressHUD = new SVProgressHUD(this);
        userid = (String) SPUtils.get(getApplicationContext(), SPConstant.USERID, "");
        Log.i("test", "log:" + userid);
        checkUnitName = getIntent().getStringExtra("checkUnitName");
        checkProjectName = getIntent().getStringExtra("checkProjectName");
        checkId = getIntent().getStringExtra("checkId");
        Log.i("test", "checkId:" + checkId);
        checkUnitId = getIntent().getStringExtra("checkUnitId");
        checkProjectId = getIntent().getStringExtra("checkProjectId");
        if (!"".equals(checkUnitName)) {
            tvDailycheckunit.setText(this.checkUnitName);
        }

        if (!checkProjectName.equals("")) {
            tvDailycheckproject.setText(checkProjectName);
        }
        int screenWidth = OtherUtils.getWidthInPx(getApplicationContext());
        mColumnWidth = (screenWidth - OtherUtils.dip2px(getApplicationContext(), 6)) / 4;
        ivBack.setOnClickListener(this);
        layoutDailyphoto.setOnClickListener(this);
        btDailysubmit.setOnClickListener(this);
        tvDailycheckpoints.addTextChangedListener(new TextWatcher() {
            int l = 0;////////记录字符串被删除字符之前，字符串的长度
            int location = 0;//记录光标的位置
            Pattern p = Pattern.compile(" 0~18 [1-9]\\d");
            int min = 0;
            int max = 18;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                l = s.length();
                location = tvDailycheckpoints.getSelectionStart();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Matcher m = p.matcher(s.toString());
                if (!s.toString().equals("")) {
                    int integer = Integer.valueOf(s.toString());
                    if (integer < 0) {
                        tvDailycheckpoints.setText(0 + "");
                    } else if (integer > 10) {
                        tvDailycheckpoints.setText(10 + "");
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String scorenumber = s.toString();

                if (!scorenumber.equals("")) {
                    int score = Integer.parseInt(scorenumber);
                    if (score > 10) {
                        return;
                    } else {
                        if (score == 0) {
                            tvDailycheckresult.setText("好");
                            Log.i("test", "ss:" + tvDailycheckresult.getText().toString());
                        } else if (score <= 2) {
                            tvDailycheckresult.setText("一般");
                        } else if (score <= 5) {
                            tvDailycheckresult.setText("较差");
                        } else if (score <= 10) {
                            tvDailycheckresult.setText("差");
                        }
                    }
                } else {
                    tvDailycheckresult.setText("");
                }
            }
        });
        gdDailyimg.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String path = mResults.get(position);
                Intent intent = new Intent(DailyCheckListFillActivity.this, PhotoSeeActivity.class);
                intent.putExtra("path", path);
                startActivity(intent);
            }
        });


    }

    @Override
    protected void initData() {
        tvDailyspecificsituation.setFilters(new InputFilter[]{new InputFilter.LengthFilter(100)});
        //输入框触摸事件拦截
        tvDailyspecificsituation.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_UP) {
                    scrollview.requestDisallowInterceptTouchEvent(false);//父控件吃掉点击事件
                } else {
                    scrollview.requestDisallowInterceptTouchEvent(true);//屏蔽父控件的拦截事件
                }
                return false;
            }
        });
        //输入长度监听
        tvDailyspecificsituation.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                remarkLenth.setText(length + "/100");
            }
        });
    }

    @Override
    protected DailyCheckListFillPresenter initPresenter() {
        dailyCheckListFillPresenter = new DailyCheckListFillPresenter();
        return dailyCheckListFillPresenter;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                ImgSelActivity.getInstance().destoryActivity();
                finish();
                break;
            case R.id.bt_dailysubmit:

                if (tvDailycheckpoints.getText().toString().equals("")) {
                    ToastUtils.show(getApplicationContext(), "分数不能为空");
                    return;
                } else if (tvDailycheckresult.getText().toString().equals("")) {
                    ToastUtils.show(getApplicationContext(), "检查结果不能为空");
                    return;
                } else if (listfile.size() == 0) {
                    ToastUtils.show(getApplicationContext(), "图片不能为空");
                    return;
                }
                Log.i("test", checkId + "\n" + checkUnitId + "\n" + checkProjectId);
                if (NetworkUtils.isConnected()) {
                    Log.i("test", "当前有网");
                    if (NetworkUtils.getNetworkType() == NetworkUtils.NetworkType.NETWORK_WIFI) {
                        Log.i("test", "当前wifi网络");
                        submitdailycheck();
                    } else {
                        new AlertDialog(this)
                                .builder()
                                .setMsg("当前网络不是wifi,将使用流量,确认提交吗?")
                                .setTitle("确认提交")
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        submitdailycheck();
                                    }
                                })
                                .setNegativeButton("取消", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        /**
                                         * 取消后，提示数据缓存
                                         */
                                        getEditTextString();
                                        cacheSignData();
                                        svProgressHUD.showInfoWithStatus("数据已缓存，将在WiFi状态下自动提交！",2000);
                                    }


                                })
                                .show();
                    }
                } else {
                    getEditTextString();
                    cacheSignData();
                    svProgressHUD.showInfoWithStatus("数据已缓存，将在WiFi状态下自动提交！",2000);
                }
                break;
            case R.id.layout_dailyphoto:
                ImgSelConfig config = new ImgSelConfig.Builder(DailyCheckListFillActivity.this, loader)
                        // 是否多选
                        .multiSelect(true)
                        .btnText("确定")
                        // 确定按钮背景色
//                        .btnBgColor(Color.parseColor("#ffffff"))
                        // 确定按钮文字颜色
                        .btnTextColor(Color.WHITE)
                        // 使用沉浸式状态栏
                        .statusBarColor(Color.parseColor("#3F51B5"))
                        // 返回图标ResId
                        .backResId(R.mipmap.left_back)
                        .title("Images")
                        .titleColor(Color.WHITE)
                        .titleBgColor(Color.parseColor("#3F51B5"))
                        .allImagesText("全部图片")
                        .cropSize(1, 1, 200, 200)
                        .needCrop(false)
                        // 第一个是否显示相机
                        .needCamera(true)
                        // 最大选择图片数量
                        .maxNum(4)
                        .build();

                ImgSelActivity.startActivity(DailyCheckListFillActivity.this, config, REQUEST_CODE);
                break;
            default:
                break;
        }
    }

    /**
     * 保存数据到数据库
     */
    private void cacheSignData() {
        String userId = (String) SPUtils.get(this, SPConstant.USERID, "");
        DailyCheck check = new DailyCheck(null, userId, checkUnitId, checkProjectId, reslut, strContent
                , fen, checkId, pathList);
        DBManager<DailyCheck> dbManager = OnWifiLoadDailyCheck.getInstance().getDbManager();
        dbManager.insertObj(check);
    }


    private void submitdailycheck() {
        dailyCheckListFillPresenter.sumbitFill(checkUnitId, checkProjectId, userid, tvDailycheckresult.getText().toString()
                , tvDailyspecificsituation.getText().toString(), tvDailycheckpoints.getText().toString(), checkId
                , listfile);
    }

    private ImageLoader loader = new ImageLoader() {
        @Override
        public void displayImage(Context context, String path, ImageView imageView) {
            Glide.with(context).load(path).into(imageView);
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("test", "onResume");
        for (int i = 0; i < pathList.size(); i++) {
            String s = pathList.get(i);
            File file = new File(s);
            if (file.exists()) {

            } else {
                pathList.remove(s);
            }
        }
        showResult(pathList);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            pathList = data.getStringArrayListExtra(ImgSelActivity.INTENT_RESULT);
            Log.i("test", "路径:" + pathList.toString());
//            showResult(pathList);
            //每次添加图片之前清空集合中的数据，确保list中无重复的图片
            if (!listfile.isEmpty()) {
                listfile.clear();
            }
            for (int i = 0; i < pathList.size(); i++) {
                listfile.add(new File(pathList.get(i)));
                Log.i("test", "listfile:" + listfile.toString());
            }
        }
    }


    List<File> listfile = new ArrayList<File>();

    private void showResult(List<String> pathList) {
        if (mResults == null) {
            mResults = new ArrayList<>();
        }
        Log.i("test", "pathList:" + pathList.size());
        mResults.clear();
        mResults.addAll(pathList);
        Log.i("test", mResults.size() + "");
        if (mAdapter == null) {
            mAdapter = new GridAdapter(mResults);
            gdDailyimg.setAdapter(mAdapter);
        } else {
            Log.i("test", "shuaxin");
            mAdapter.setPathList(mResults);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void showWaiting() {
        svProgressHUD.showWithStatus("正在提交...");
    }

    @Override
    public void colseWaiting() {
        svProgressHUD.dismissImmediately();
    }

    @Override
    public void showExpretion(String msg) {
        svProgressHUD.showInfoWithStatus(msg);
    }

    @Override
    public void fillSuccess() {
        Toast toast = Toast.makeText(getApplicationContext(), "提交成功", Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
        svProgressHUD.showSuccessWithStatus("提交成功！");
        ImgSelActivity.getInstance().destoryActivity();
        finish();
    }

    @Override
    public void fillFaile() {
        svProgressHUD.showErrorWithStatus("提交失败！");
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        ImgSelActivity.getInstance().destoryActivity();
    }

    public void getEditTextString() {
        reslut = tvDailycheckresult.getText().toString().trim();
        strContent = tvDailyspecificsituation.getText().toString().trim();
        fen = tvDailycheckpoints.getText().toString().trim();
    }


    private class GridAdapter extends BaseAdapter {
        private List<String> pathList;

        public GridAdapter(List<String> listUrls) {
            this.pathList = listUrls;
        }

        @Override
        public int getCount() {
            return pathList.size();
        }

        @Override
        public String getItem(int position) {
            return pathList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        public void setPathList(List<String> pathList) {
            this.pathList = pathList;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ImageView imageView;
            if (convertView == null) {
                convertView = getLayoutInflater().inflate(R.layout.item_checklistfill_gridview, null);
                imageView = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(imageView);
            } else {
                imageView = (ImageView) convertView.getTag();
            }
//            ImageLoader.getInstance().display(getItem(position), imageView, mColumnWidth, mColumnWidth);
            File file = new File(getItem(position));
            Log.i("test", "file:" + file);
            Glide.with(getApplicationContext()).load(file).into(imageView);
            return convertView;

        }
    }

}