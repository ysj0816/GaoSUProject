package com.chuyu.gaosuproject.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.IdRes;
import android.support.annotation.RequiresApi;
import android.support.v4.content.FileProvider;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.adapter.SignAdapter;
import com.chuyu.gaosuproject.base.MVPBaseActivity;
import com.chuyu.gaosuproject.bean.daobean.SignAndLeaveData;
import com.chuyu.gaosuproject.bean.daobean.SignDataDao;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.dao.DBManager;
import com.chuyu.gaosuproject.presenter.SignPresenter;
import com.chuyu.gaosuproject.receviver.NetCheckReceiver;
import com.chuyu.gaosuproject.util.DateUtils;
import com.chuyu.gaosuproject.util.LocationCityUtil;
import com.chuyu.gaosuproject.util.NetworkUtils;
import com.chuyu.gaosuproject.util.PermissionsChecker;
import com.chuyu.gaosuproject.util.PictureUtil;
import com.chuyu.gaosuproject.util.SPUtils;
import com.chuyu.gaosuproject.util.ToastUtils;
import com.chuyu.gaosuproject.util.observer.NetChangeObserver;
import com.chuyu.gaosuproject.util.upload.OnWifiUpLoadSign;
import com.chuyu.gaosuproject.util.upload.SignLeaveDao;
import com.chuyu.gaosuproject.view.ISignsView;
import com.chuyu.gaosuproject.widget.AlertDialog;
import com.chuyu.gaosuproject.widget.RadioButtonDialog;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 签到
 */
public class SignActivity extends MVPBaseActivity<ISignsView, SignPresenter> implements ISignsView {

    @BindView(R.id.up_radio)
    RadioButton upRadio;
    @BindView(R.id.down_rb)
    RadioButton downRb;
    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.signTime)
    TextView signTime;
    @BindView(R.id.signLocation)
    TextView signLocation;
    @BindView(R.id.refres_bt)
    Button refresBt;
    @BindView(R.id.camerll)
    LinearLayout camerll;
    @BindView(R.id.graidview)
    GridView graidview;
    @BindView(R.id.typeText)
    TextView typeText;
    @BindView(R.id.typell)
    RelativeLayout typell;
    @BindView(R.id.commit_bt)
    Button commitBt;
    @BindView(R.id.edit_Remark)
    EditText editRemark;
    @BindView(R.id.scrollview)
    ScrollView scrollview;
    @BindView(R.id.remarkLenth)
    TextView remarkLenth;
    // 需检查的全部敏感权限
    static final String[] PERMISSIONS = new String[]{
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.CAMERA"
    };
    private static final int APP_HANDLER = 0x1;
    private static final long SPLASH_DELAY_MILLIS = 1000;
    private static final int REQUEST_CODE = 0; // 请求码
    private static final int REQUEST_CODE_WRITE_SETTINGS = 1;//写入设置
    private static final int SING_ONESELF = 11;
    private static final int TAKE_PICTURE = 100;
    private static final int takePicture = 10;


    private Uri imageUri;//拍照保存图片路径
    private File file;//拍照图片
    private String cacheImg;
    // 权限检测器
    private PermissionsChecker mPermissionsChecker;

    private SignPresenter signPresenter;
    //声明AMapLocationClient类对象
    public AMapLocationClient mLocationClient = null;
    //声明AMapLocationClientOption对象
    public AMapLocationClientOption mLocationOption = null;
    private AMapLocation amapLocation;
    private int DutyType = 1;// 签到类型
    private int teqingType = 0;//特情类型
    private RadioButtonDialog dialog;
    private List<String> listImg;
    private SignAdapter signAdapter;
    private boolean isLocationSuccess = false;//定位是否成功
    private SVProgressHUD svProgressHUD;
    private DBManager<SignAndLeaveData> dbManager;
    @Override
    protected SignPresenter initPresenter() {
        signPresenter = new SignPresenter();
        return signPresenter;
    }

    @Override
    protected int initContent() {
        return R.layout.activity_signs;
    }

    @Override
    protected void initView() {
        final Intent intent = new Intent(this, PhotoSeeActivity.class);
        svProgressHUD = new SVProgressHUD(this);
        //初始化定位
        mLocationClient = new AMapLocationClient(getApplicationContext());
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        //定位
        LocationCityUtil.getInstance().getNowLocaiton(mLocationClient, mLocationOption, new LocationCityUtil.LocationListener() {
            @Override
            public void locationSuccess(AMapLocation amapLocation) {
                //可在其中解析amapLocation获取相应内容。
                isLocationSuccess = true;
                SignActivity.this.amapLocation = amapLocation;
                String address = amapLocation.getAddress();//地址
                float accuracy = amapLocation.getAccuracy();//精度
                signLocation.setText(address + " (参考距离约" + accuracy + "米)");
                stopLoaction();
            }

            @Override
            public void locationFaile(AMapLocation amapLocation) {
                isLocationSuccess = false;
                if (amapLocation.getErrorCode() == 4) {
                    ToastUtils.show(SignActivity.this, "网络连接异常！");
                }
                signLocation.setText("定位失败！");
                stopLoaction();
            }
        });
        dialog = new RadioButtonDialog(this, R.style.Dialogstyle, new RadioButtonDialog.SelectLinstener() {

            @Override
            public void setSelectType(int type, String selectType) {
                typeText.setText(selectType);
                teqingType = type;
                dialog.dismiss();
            }
        });
        signAdapter = new SignAdapter(this, listImg);
        graidview.setAdapter(signAdapter);
        graidview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (listImg.size() == 0) {

                } else {
                    //跳转预览
                    intent.putExtra("path", listImg.get(position));
                    startActivity(intent);
                }
            }
        });


        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (checkedId == R.id.up_radio) {
                    DutyType = 1;
                    //上班
                } else {
                    //下班
                    DutyType = 2;
                }
            }
        });
    }

    @Override
    protected void initData() {
        dbManager = SignLeaveDao.getInstace().getDbManager();
        listImg = new ArrayList<>();
        String time = DateUtils.getNowTime();
        signTime.setText(time);
        editRemark.setFilters(new InputFilter[]{new InputFilter.LengthFilter(255)});
        //输入框触摸事件拦截
        editRemark.setOnTouchListener(new View.OnTouchListener() {
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
        editRemark.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                int length = s.length();
                remarkLenth.setText(length + "/255");
            }
        });

    }
    /**
     * 检查是否重复提交
     */
    public String date;
    public int dutyType;
    public String userId;

    @OnClick({R.id.refres_bt, R.id.camerll, R.id.typell, R.id.commit_bt, R.id.img_back})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.refres_bt:
                isLocationSuccess=false;
                //刷新定位
                LocationCityUtil.getInstance().reFreshLocation();
                signLocation.setText("获取当前定位中...");
                break;
            case R.id.camerll:
                //先检查权限
                // initPermission();
                takePicture();
                break;
            case R.id.typell:
                //弹窗
                dialog.show();
                break;
            case R.id.commit_bt:

                if (listImg.size() == 0) {
                    ToastUtils.show(this, "请上传照片！");
                    return;
                }
                date = DateUtils.getNowDate();
                dutyType = DutyType;
                userId = (String) SPUtils.get(this, SPConstant.USERID, "");
                //判断是否有网 以及是否是wifi
                if (NetworkUtils.isConnected()) {
                    if (NetworkUtils.getNetworkType() == NetworkUtils.NetworkType.NETWORK_WIFI) {
                        /**
                         * 检差判断是否重复提交
                         */
                        signPresenter.isSign(userId, date, dutyType);
                    } else {
                        new AlertDialog(this)
                                .builder()
                                .setMsg("当前网络不是wifi,将使用流量,确认提交吗?")
                                .setTitle("确认提交")
                                .setPositiveButton("确认", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        /**
                                         * 检差判断是否重复提交
                                         */
                                        signPresenter.isSign(userId, date, dutyType);
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
                    svProgressHUD.showInfoWithStatus("无网络，数据已缓存，将在WiFi状态下自动提交！");
                    cacheSignData();
                }


                break;
            case R.id.img_back:
                finish();
                break;
            default:
                break;
        }
    }

    //int i;

    /**
     * 拍照
     * top1 .检查权限
     * top2. 开始拍照
     */
    private void takePicture() {

        //执行拍照前，应该先判断SD卡是否存在
        String SDState = Environment.getExternalStorageState();
        if (SDState.equals(Environment.MEDIA_MOUNTED)) {
            Intent openCameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            if (openCameraIntent.resolveActivity(getPackageManager()) != null) {

                createFiels();
                //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
                //调用相机
                startCamer(openCameraIntent);

                /**-----------------*/
            } else {
                Toast.makeText(this, "没找到摄像头", Toast.LENGTH_LONG).show();
            }

        } else {
            Toast.makeText(this, "内存卡不存在", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 缓存签到数据
     */
    public void cacheSignData(){
        String s = signLocation.getText().toString();
        //提交
        int dutyType = DutyType;
        String dutyDate = signTime.getText().toString();
        String location = "";
        double lng = 0.0;
        double lat = 0.0;
        if (isLocationSuccess) {
            //如果定位成功
            lng = amapLocation.getLongitude();
            lat = amapLocation.getLatitude();
            location = s;
        }
        String userId = (String) SPUtils.get(this, SPConstant.USERID, "");
        int teType = teqingType;
        String remark = editRemark.getText().toString().trim();
        String filpath = listImg.get(0);

        String img64=PictureUtil.bitmapToBase64(filpath);

        SignAndLeaveData signAndLeaveData = new SignAndLeaveData(null, userId, dutyDate,
                teType + "", dutyType, location, lng + "", lat + "", "", "", remark, img64);

        /**
         * 数据库插入一条数据
         */
         dbManager.insertObj(signAndLeaveData);

    }
    
    /**
     * 创建文件夹
     */
    private void createFiels() {
        String name = "image.jpg";
        //文件夹路径
        String ImgFile = Environment.getExternalStorageDirectory() + File.separator + this.getPackageName() + File.separator + "Cache";//默认路径
        File folderAddr = new File(ImgFile);
        if (!folderAddr.exists() || !folderAddr.isDirectory()) {
            folderAddr.mkdirs();
        }
        //图片路径
        cacheImg = ImgFile + File.separator + name;
        file = new File(cacheImg);
        imageUri = Uri.fromFile(file);
    }

    /**
     * 开始拍照
     *
     * @param openCameraIntent
     */
    private void startCamer(Intent openCameraIntent) {
        File file1 = new File(cacheImg);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {

            imageUri = FileProvider.getUriForFile(this, "com.ysj.fileprovider", file1);//通过FileProvider创建一个content类型的Uri
            openCameraIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION); //添加这一句表示对目标应用临时授权该Uri所代表的文件
        }

        openCameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(openCameraIntent, TAKE_PICTURE);
    }

    @Override
    public void showWaiting() {
        svProgressHUD.showWithStatus("请稍等...");
    }

    @Override
    public void closeWaitting() {
        svProgressHUD.dismissImmediately();
    }

    @Override
    public void showMesg(String msg) {

    }

    @Override
    public void showPrograss() {

    }

    @Override
    public void showSubmitFaile() {
        svProgressHUD.showErrorWithStatus("提交失败！");
    }

    @Override
    public void showSubSuccess() {
        svProgressHUD.showSuccessWithStatus("提交成功！");
        //延迟跳转
        mHandler.sendEmptyMessageDelayed(SING_ONESELF, 500);
    }

    /**
     * 判断是否重复签到
     *
     * @param isSign
     */
    @Override
    public void isReprSign(boolean isSign, String msg) {
        if (isSign) {
            String s = signLocation.getText().toString();
            //提交
            String date = DateUtils.getNowDate();
            int dutyType = DutyType;
            String dutyTime = signTime.getText().toString();
            String location = "";
            double lng = 0.0;
            double lat = 0.0;
            if (isLocationSuccess) {
                //如果定位成功
                lng = amapLocation.getLongitude();
                lat = amapLocation.getLatitude();
                location = s;
            }
            String userId = (String) SPUtils.get(this, SPConstant.USERID, "");
            int teType = teqingType;
            String remark = editRemark.getText().toString().trim();
            File file = new File(listImg.get(0));

            //提交
            signPresenter.submitData(userId, dutyTime, dutyType + "", location, lng + "", lat + "", teType + "", remark, file);

        } else {
            svProgressHUD.showInfoWithStatus(msg);
        }
    }


    /**
     * 检查敏感权限
     */
    private void initPermission() {
        mPermissionsChecker = new PermissionsChecker(this);
        // 缺少权限时, 进入权限配置页面
        if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
            //请求
            startPermissionsActivity();
            //
        } else {
            mHandler.sendEmptyMessageDelayed(APP_HANDLER, SPLASH_DELAY_MILLIS);
        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case APP_HANDLER:
                    break;
                case takePicture:
                    Bitmap canvasBitmap = (Bitmap) msg.obj;
                    //保存图片
                    if (canvasBitmap != null) {
                        String saveFiles = PictureUtil.saveFile(canvasBitmap, SignActivity.this);
                        listImg.clear();
                        listImg.add(0, saveFiles);
                        signAdapter.setImgUrl(listImg);
                        signAdapter.notifyDataSetChanged();
                    }
                    break;
                case SING_ONESELF:
                    Intent intent = new Intent(SignActivity.this, SignOnserlfActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // 拒绝时, 缺少主要权限, 无法运行
        if (requestCode == REQUEST_CODE && resultCode == PermissionsActivity.PERMISSIONS_DENIED) {
            ToastUtils.showSure(this, "缺少主要权限，可能影响某些功能的使用！");

        } else if (requestCode == REQUEST_CODE_WRITE_SETTINGS) {
            if (Settings.System.canWrite(this)) {
                Log.i("test", "onActivityResult write settings granted");
            } else {
                Log.i("test", "onActivityResult write settings unGranted");
            }

            mHandler.sendEmptyMessageDelayed(APP_HANDLER, SPLASH_DELAY_MILLIS);
        } else {

            mHandler.sendEmptyMessageDelayed(APP_HANDLER, SPLASH_DELAY_MILLIS);
        }

        //拍照  的照片返回
        if (requestCode == TAKE_PICTURE && resultCode == RESULT_OK) {
            Bitmap bitmap =PictureUtil. zoomCompressImg(cacheImg,SignActivity.this);
            Message message = Message.obtain();
            message.what = takePicture;
            message.obj = bitmap;
            mHandler.sendMessageDelayed(message, SPLASH_DELAY_MILLIS);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        /**
         *停止定位服务
         */
        stopServer();
    }

    /**
     * 停止定位服务
     */
    public void stopServer() {
        /**
         * 停止定位服务
         */
        LocationCityUtil.getInstance().stopServer();
    }

    /**
     * 停止定位
     */
    public void stopLoaction() {
        /**
         * 停止定位
         */
        LocationCityUtil.getInstance().stopLcaction();
    }

}
