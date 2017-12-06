package com.chuyu.gaosuproject.activity;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.maps2d.model.MyLocationStyle;
import com.bigkoo.svprogresshud.SVProgressHUD;
import com.chuyu.gaosuproject.R;
import com.chuyu.gaosuproject.adapter.InfoAdapter;
import com.chuyu.gaosuproject.bean.SignBean;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.util.DateUtils;
import com.chuyu.gaosuproject.util.LocationCityUtil;
import com.chuyu.gaosuproject.util.SPUtils;
import com.chuyu.gaosuproject.util.ToastUtils;
import com.chuyu.gaosuproject.util.Utility;
import com.chuyu.gaosuproject.widget.AlertDialog;
import com.chuyu.gaosuproject.widget.RightTopWindow;
import com.google.gson.Gson;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.zaaach.toprightmenu.MenuItem;
import com.zaaach.toprightmenu.TopRightMenu;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Response;

/**
 * 移动签到
 */
public class MobileSignActivity extends AppCompatActivity implements  AMap.InfoWindowAdapter {

    @BindView(R.id.textView)
    TextView textView;
    @BindView(R.id.map)
    MapView mMapView;
    @BindView(R.id.searchImg)
    ImageView searchImg;
    @BindView(R.id.signText)
    TextView signText;
    @BindView(R.id.levalText)
    TextView levalText;

    AMap aMap;
    MyLocationStyle myLocationStyle;
    Gson gson;

    private MarkerOptions markerOption;
    InfoAdapter infoAdapter;
    private SVProgressHUD svProgressHUD;
    protected String callStr="";
    private AMapLocationClient mlocationClient;
    private AMapLocationClientOption mLocationOption;
    private boolean isMarkshow=false;//是否显示
    private View infowindow;
    private View infowindow2;
    private ListView infolist;
    private static final int GPSLOCATION=11;
    private LatLng latlng;//地图上的经纬度
    private Marker marker;//地图的infowindow
    private double latitude;
    private double longitude;
    private RightTopWindow rightTopWindow;
    private int showMarker=1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moblie_sign);
        ButterKnife.bind(this);
        svProgressHUD = new SVProgressHUD(this);
//        svProgressHUD.showWithStatus("请稍等！");
        //在activity执行onCreate时执行mMapView.onCreate(savedInstanceState)，创建地图
        mMapView.onCreate(savedInstanceState);
        //初始化地图控制器对象
        gson = new Gson();
        if (aMap == null) {
            aMap = mMapView.getMap();
        }
        //初始化定位
        mlocationClient = new AMapLocationClient(getApplicationContext());
        //初始化AMapLocationClientOption对象
        mLocationOption = new AMapLocationClientOption();
        initData();
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        //  myLocationStyle.showMyLocation(true);//设置是否显示定位小蓝点，用于满足只想使用定位，不想使用定位小蓝点的场景，设置false以后图面上不再有定位蓝点的概念，但是会持续回调位置信息。
        // aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
        aMap.getUiSettings().setMyLocationButtonEnabled(true);//设置默认定位按钮是否显示，非必需设置。
        // aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        //UiSettings uiSettings = aMap.getUiSettings();
        //uiSettings.setCompassEnabled(true);//指南针
        //uiSettings.setScaleControlsEnabled(true);//比例尺
        //uiSettings.setMyLocationButtonEnabled(true);//定位角标
        // 缩放级别（zoom）：地图缩放级别范围为【4-20级】，值越大地图越详细
        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
        //设置中心点
        //aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlng));
        // 设置定位监听
        //aMap.setLocationSource(this);
        // 设置为true表示显示定位层并可触发定位，false表示隐藏定位层并不可触发定位，默认是false
        aMap.setMyLocationEnabled(true);
        // 设置定位的类型为定位模式，有定位、跟随或地图根据面向方向旋转几种
        aMap.setMyLocationType(AMap.MAP_TYPE_SATELLITE);
        aMap.setOnMarkerClickListener(new AMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                if (!isMarkshow){
                    isMarkshow=true;
                    marker.showInfoWindow();
                    //显示
                    return false;
                }else {
                    //隐藏
                    isMarkshow=false;
                    marker.hideInfoWindow();
                    return true;
                }

            }
        });
        aMap.setInfoWindowAdapter(this);
        //地图弹窗infowindow
        infoAdapter = new InfoAdapter(this, null);
        //右上角弹窗选择
        rightTopWindow=new RightTopWindow(this, new RightTopWindow.LookLinsenter() {
            @Override
            public void clickPosition(int i) {
                switch (i) {
                    case 0:
                        Intent intent = new Intent(MobileSignActivity.this, SignOnserlfActivity.class);
                        startActivity(intent);
                        break;
                    case 1:
                        Intent intent2 = new Intent(MobileSignActivity.this, SignAllPersonNodeActivity.class);
                        startActivity(intent2);
                        break;
                    case 2:
                        Intent intent3 = new Intent(MobileSignActivity.this, SignNotActivity.class);
                        startActivity(intent3);
                        break;
                    default:
                        break;
                }
            }
        });
        if(infowindow == null) {
            infowindow = LayoutInflater.from(this).inflate(
                    R.layout.map_infowindow_list, null);
        }
        infolist = (ListView) infowindow.findViewById(R.id.infoList);
        infolist.setDivider(null);
        infolist.setAdapter(infoAdapter);
    }
    /**
     * 初始化数据
     */
    private void initData() {
        String nowDate = DateUtils.getNowDate();
        Log.i("test","nowDate"+nowDate);
        String userid = (String) SPUtils.get(this, SPConstant.USERID, "");
        OkGo.post(UrlConstant.formatUrl(UrlConstant.ALLPRESONURL))
                .connTimeOut(10000)
                .params("UserId", userid)
                .params("DutyDate",nowDate)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //剪切
                        try{
                            callStr = s;
                            //绘制第一个人的地点
                            SignBean signBean = gson.fromJson(s, SignBean.class);
                            Log.i("test","signBean"+signBean.toString());
                            String lat = signBean.getRows().get(0).getLat();
                            String lng=signBean.getRows().get(0).getLng();
                            if ("0.0".equals(lat)||"0.0".equals(lng)||lat==null||lng==null){
                                //定位当前位置
                                getLocationData();
                                showMarker=1;
                            }else{
                                latitude=Double.parseDouble(lat);
                                longitude=Double.parseDouble(lng);
                                latlng = new LatLng(latitude, longitude);
                                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlng));
                                aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                                addMarkersToMap();
                                svProgressHUD.dismissImmediately();
                                showMarker=1;
                            }

                        }catch (Exception e){
                            getLocationData();
                            svProgressHUD.dismissImmediately();
                            showMarker=2;
                        }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        svProgressHUD.dismissImmediately();
                        getLocationData();
                        showMarker=2;
                    }
                });


    }

    /**
     * 定位，获取当前位置信息
     */
    private void getLocationData(){
        //定位
        LocationCityUtil.getInstance().getNowLocaiton(mlocationClient, mLocationOption, new LocationCityUtil.LocationListener() {
            @Override
            public void locationSuccess(AMapLocation amapLocation) {
                svProgressHUD.dismissImmediately();
                //可在其中解析amapLocation获取相应内容。
                String address = amapLocation.getAddress();
                Log.i("test","address"+address);
                latitude = amapLocation.getLatitude();
                longitude = amapLocation.getLongitude();
                latlng = new LatLng(latitude, longitude);
                aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlng));
                aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                /**
                 * 地图上添加标记
                 */
                addMarkersToMap();
                /**
                 * 停止定位
                 */
                stopLoaction();

            }

            @Override
            public void locationFaile(AMapLocation amapLocation) {
                boolean oPen = isOPen(MobileSignActivity.this);
                if (!oPen){
                    //询问
                    questOpenService();
                }else {
                    /**
                     * 停止定位
                     */
                    stopLoaction();
                }
            }
        });
    }

    /**
     * 停止定位服务
     */
    public void stopServer(){
        /**
         * 停止定位服务
         */
        LocationCityUtil.getInstance().stopServer();
    }

    /**
     * 停止定位
     */
    public void stopLoaction(){
        /**
         * 停止定位
         */
        LocationCityUtil.getInstance().stopLcaction();
    }

    /**
     * 询问打开定位服务
     */
    private void questOpenService() {
        new AlertDialog(this).builder().setTitle("打开定位服务")
                .setMsg("打开定位服务？")
                .setPositiveButton("确认打开", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openGPS(MobileSignActivity.this);
                    }
                }).setNegativeButton("取消", new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(MobileSignActivity.this,"不能获取当前位置！");
            }
        }).show();
    }



    /**
     * 判断GPS是否开启，GPS或者AGPS开启一个就认为是开启的
     * @param context
     * @return true 表示开启
     */
    public static final boolean isOPen(final Context context) {
        LocationManager locationManager
                = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }


    /**
     * 强制帮用户打开GPS
     * @param context
     */
    public  final void openGPS(Context context) {

        // 转到手机设置界面，用户设置GPS
        Intent intent = new Intent(
                Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, GPSLOCATION); // 设置完成后返回到原来的界面


    }

    /**
     * 在地图上添加marker
     */
    private void addMarkersToMap() {
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory.fromBitmap(BitmapFactory
                .decodeResource(getResources(),R.drawable.icon_locaiton)))
                .title("标题")
                .snippet("详细信息")
                .position(latlng)
                .draggable(true);
        marker = aMap.addMarker(markerOption);
        marker.showInfoWindow();
        isMarkshow=true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
        stopServer();
        if (null != mlocationClient) {
            mlocationClient.onDestroy();
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        callStr="";

        String nowDate =DateUtils.getNowDate();
        String userid = (String) SPUtils.get(this, SPConstant.USERID, "");
        OkGo.post(UrlConstant.formatUrl(UrlConstant.ALLPRESONURL))
                .connTimeOut(10000)
                .params("UserId", userid)
                .params("DutyDate",nowDate)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        //剪切
                       try{
                           callStr = s;
                           SignBean signBean = gson.fromJson(s, SignBean.class);
                           infoAdapter.setList(signBean.getRows());
                           Utility.setListViewHeightBasedOnChildren(infolist);
                           svProgressHUD.dismissImmediately();
                           showMarker=1;
                       }catch (Exception e){
                           List<SignBean.Rows> list=new ArrayList<SignBean.Rows>();
                           infoAdapter.setList(list);
                           Utility.setListViewHeightBasedOnChildren(infolist);
                           svProgressHUD.dismissImmediately();
                           showMarker=2;
                       }

                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        super.onError(call, response, e);
                        svProgressHUD.dismissImmediately();
                        svProgressHUD.showErrorWithStatus("网络错误");
                        svProgressHUD.dismissImmediately();
                        showMarker=2;
                    }
                });

    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        mMapView.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
        if (marker!=null){
            marker.hideInfoWindow();
            isMarkshow=false;
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }


    @Override
    public View getInfoWindow(Marker marker) {

        //获取infowindow
        if (showMarker==1){

            SignBean signBean = gson.fromJson(callStr, SignBean.class);
            infoAdapter.setList(signBean.getRows());
            Utility.setListViewHeightBasedOnChildren(infolist);
            return infowindow;
        }else {

            if (infowindow2==null){
                infowindow2=LayoutInflater.from(this).inflate(
                        R.layout.map_infowindow_text, null);
            }
            return infowindow2;
        }


    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }

    @OnClick({R.id.searchImg, R.id.signText, R.id.levalText,R.id.ll_back})
    public void onClick(View view) {
        switch (view.getId()) {
            //右上角pappowindow
            case R.id.searchImg:
                rightTopWindow.showPopupWindow(searchImg);

                break;
            //签到页面
            case R.id.signText:
                startActivity(new Intent(this, SignActivity.class));
                break;
            //请假页面
            case R.id.levalText:
                Intent intent = new Intent(this, LeaveActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_back:
                finish();
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==GPSLOCATION){
            Log.i("test","从GPS返回出来！"+resultCode);
            if (resultCode==RESULT_OK){
                //定位
                LocationCityUtil.getInstance().getNowLocaiton(mlocationClient, mLocationOption, new LocationCityUtil.LocationListener() {
                    @Override
                    public void locationSuccess(AMapLocation amapLocation) {
                        svProgressHUD.dismissImmediately();
                        //可在其中解析amapLocation获取相应内容。

                        double latitude = amapLocation.getLatitude();
                        double longitude = amapLocation.getLongitude();
                        latlng = new LatLng(latitude, longitude);
                        aMap.moveCamera(CameraUpdateFactory.changeLatLng(latlng));
                        aMap.moveCamera(CameraUpdateFactory.zoomTo(17));
                        addMarkersToMap();
                        stopLoaction();
                    }

                    @Override
                    public void locationFaile(AMapLocation amapLocation) {
                        svProgressHUD.showErrorWithStatus("获取定位失败,请检查网络！");
                    }
                });
            }else {
                ToastUtils.show(this,"定位服务未打开，不能获取位置！");
            }

        }
    }


}
