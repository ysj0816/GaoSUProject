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
import com.chuyu.gaosuproject.bean.logmanagebean.ManageLog;
import com.chuyu.gaosuproject.constant.SPConstant;
import com.chuyu.gaosuproject.constant.UrlConstant;
import com.chuyu.gaosuproject.dao.DBManager;
import com.chuyu.gaosuproject.receviver.NetCheckReceiver;
import com.chuyu.gaosuproject.util.NetworkUtils;
import com.chuyu.gaosuproject.util.OtherUtils;
import com.chuyu.gaosuproject.util.SPUtils;
import com.chuyu.gaosuproject.util.SystemBarTintManager;
import com.chuyu.gaosuproject.util.SystemStatusBar;
import com.chuyu.gaosuproject.util.ToastUtils;
import com.chuyu.gaosuproject.util.observer.NetChangeObserver;
import com.chuyu.gaosuproject.util.upload.OnWifiUpLoadLog;
import com.chuyu.gaosuproject.util.upload.OnWifiUpLoadWaterLog;
import com.chuyu.gaosuproject.widget.AlertDialog;
import com.chuyu.gaosuproject.widget.MyEditText;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.BaseRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

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
	private OnWifiUpLoadWaterLog upLoadWaterLog;
	private boolean iscommit = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_water_electrician);
		ButterKnife.bind(this);
		initview();
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


		NetChangeObserver observer = new NetChangeObserver() {
			@Override
			public void onNetConnected(NetworkUtils.NetworkType type) {
				Log.i("test", "水电工有网");
				if (type == NetworkUtils.NetworkType.NETWORK_WIFI) {
					Log.i("test", "水电工有网WIFI");
					//有网情况下获取数据库存的离线日志
					List<ManageLog> manageLogs = dbManager.queryAllList(dbManager.getQueryBuiler());
					Log.i("test", "manageLogs:" + manageLogs.toString());
					if (manageLogs.size() > 0) {
						Log.i("test", "水电工数据库有数据");
						upLoadWaterLog.upLoadLog(manageLogs);
					}

				}
			}

			@Override
			public void onNetDisConnect() {
				Log.i("test", "网络连接没有连接");
			}
		};
		NetCheckReceiver.registerObserver(observer);
		upLoadWaterLog = OnWifiUpLoadWaterLog.getInstace();
		dbManager = upLoadWaterLog.getDbManager();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.layout_back:
//                if (tag.equals("one")){
//                    Intent intent=new Intent(AddWaterElectricianActivity.this,MainActivity.class);
//                    startActivity(intent);
//                }else if (tag.equals("two")){
//                    finish();
//                }
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
									if (iscommit) {
										svProgressHUD.showInfoWithStatus("签到数据已缓存，将在WiFi状态下自动提交！");
									} else {
										svProgressHUD.showInfoWithStatus("内存中已有缓存");
									}

								}


							})
							.show();
				}
			} else {
				cacheSignData();
				if (iscommit) {
					svProgressHUD.showInfoWithStatus("无网络，签到数据已缓存，将在WiFi状态下自动提交！");
				} else {
					svProgressHUD.showInfoWithStatus("内存中已有缓存");
				}


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
		/**
		 * 数据库插入一条数据
		 */
		List<ManageLog> manageLogs = dbManager.queryAllList(dbManager.getQueryBuiler());
		if (manageLogs != null) {
			for (int i = 0; i < manageLogs.size(); i++) {
				String createTime = manageLogs.get(i).getCreateTime();
				String currenttime = OtherUtils.GetcurrentTime();
				Log.i("test", "createTime:" + createTime + "\n" + "currenttime:" + currenttime);
				Log.i("test", "数据库大小:" + manageLogs.size());
				String[] splitcreateTime = createTime.split(" ");
				String[] splitcurrenttime = currenttime.split(" ");
				if (splitcreateTime[0].equals(splitcurrenttime[0])) {
					if (manageLogs.get(i).getCategory().equals("2")) {
						iscommit = false;
						return;
					}
				}

			}
			if (iscommit) {
				dbManager.insertObj(manageLog);
			}
		}

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
