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
import com.chuyu.gaosuproject.bean.daobean.SignDataDao;
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
import com.chuyu.gaosuproject.util.upload.OnWifiUpLoadSign;
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
	private NetChangeObserver observer;//网络观察者
	private OnWifiUpLoadLog onWifiUpLoadLog;
	private String edtManagementchecksituation;
	private String edtCommonwealchecksituation;
	private String edtOndutyrecord;
	private String edtRemarks;
	private DBManager<ManageLog> dbManager;
	private boolean isfirst=true;
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

		//添加一个网络观察者
		observer = new NetChangeObserver() {
			@Override
			public void onNetConnected(NetworkUtils.NetworkType type) {
				Log.i("test", "有网");
				if ( type== NetworkUtils.NetworkType.NETWORK_WIFI) {
					Log.i("test", "有网WIFI");
					//有网情况下获取数据库存的离线日志
					List<ManageLog> manageLogs = dbManager.queryAllList(dbManager.getQueryBuiler());
					Log.i("test", "manageLogs:" + manageLogs.toString());
					if (manageLogs.size()>0) {
						Log.i("test","数据库有数据");
						onWifiUpLoadLog.upLoadLog(manageLogs);
					}

				}
			}

			@Override
			public void onNetDisConnect() {
				Log.i("test", "网络没有连接");
			}
		};
		NetCheckReceiver.registerObserver(observer);
		onWifiUpLoadLog = OnWifiUpLoadLog.getInstace();

		dbManager = onWifiUpLoadLog.getDbManager();
//		List<ManageLog> manageLogs = dbManager.queryAllList(dbManager.getQueryBuiler());
//		Log.i("test", "manageLogs:" + manageLogs.toString());
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
					Log.i("test","当前wifi网络");
					Log.i("test", "数据已提交");
					submitLog();
				} else {
					new AlertDialog(this)
							.builder()
							.setMsg("当前网络不是wifi,将使用流量,确认提交吗?")
							.setTitle("确认提交")
							.setPositiveButton("确认", new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									submitLog();
								}
							})
							.setNegativeButton("取消", new View.OnClickListener() {
								@Override
								public void onClick(View v) {
									/**
									 * 取消后，提示数据缓存
									 */
									cacheSignData();
									if (isfirst) {
										svProgressHUD.showInfoWithStatus("签到数据已缓存，将在WiFi状态下自动提交！");
									} else {
										svProgressHUD.showInfoWithStatus("内存中已有缓存");
									}

								}
							})
							.show();
				}
			} else {
				Log.i("test","当前无网");
				cacheSignData();
				if (isfirst) {
					svProgressHUD.showInfoWithStatus("无网络，签到数据已缓存，将在WiFi状态下自动提交！");
				} else {
					svProgressHUD.showInfoWithStatus("内存中已有缓存");
				}
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

	private void cacheSignData() {
		String userId = (String) SPUtils.get(this, SPConstant.USERID, "");
		String nowtime = OtherUtils.GetcurrentTime();
		ManageLog lognamage = new ManageLog(null, userId, nowtime, tvManagementchecksituation.getText().toString(),
				tvCommonwealchecksituation.getText().toString(), tvOndutyrecord.getText().toString(),
				tvRemarks.getText().toString(), "1");
		/**
		 * 数据库插入一条数据
		 */
		List<ManageLog> manageLogs = dbManager.queryAllList(dbManager.getQueryBuiler());
		for (int i = 0; i < manageLogs.size(); i++) {
			String createTime = manageLogs.get(i).getCreateTime();
			String currenttime = OtherUtils.GetcurrentTime();
			Log.i("test", "createTime:" + createTime + "\n" + "currenttime:" + currenttime);
			Log.i("test", "水电工数据库大小:" + manageLogs.size());
			String[] splitcreateTime = createTime.split(" ");
			String[] splitcurrenttime = currenttime.split(" ");
			if (splitcreateTime[0].equals(splitcurrenttime[0])) {
				if (manageLogs.get(i).getCategory().equals("1")) {
					isfirst = false;
					return;
				}
			}

		}
		if (isfirst){
			dbManager.insertObj(lognamage);
		}
		Log.i("test", "lognamage:" + lognamage.toString());
	}

	private void submitLog() {
		//上报水电工日志
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
	}

}
