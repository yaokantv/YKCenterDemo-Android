package com.ykan.sdk.example;

import java.util.ArrayList;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.gizwits.gizwifisdk.enumration.GizUserAccountType;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.yaokan.sdk.api.YkanSDKManager;
import com.yaokan.sdk.utils.Logger;
import com.yaokan.sdk.utils.ProgressDialogUtils;
import com.yaokan.sdk.wifi.DeviceManager;
import com.yaokan.sdk.wifi.GizWifiCallBack;

public class MainActivity extends BaseActivity {
	protected String TAG = MainActivity.class.getSimpleName();
	private EditText etName, etPsw;
	private ProgressDialogUtils dialogUtils;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// 初始化SDK
		YkanSDKManager.init(this);//
		// 设置Log信息是否打印
		YkanSDKManager.getInstance().setLogger(true);
		initView();
		initListener();
	}

	private void initListener() {
		DeviceManager.instanceDeviceManager(getApplicationContext()).setGizWifiCallBack(new GizWifiCallBack() {
			/** 用于用户登录的回调 */
			@Override
			public void userLoginCb(GizWifiErrorCode result, String uid, String token) {
				Logger.d(TAG, "didUserLogin result:" + result + " uid:" + uid + " token:" + token);
				dialogUtils.sendMessage(ProgressDialogUtils.DISMISS);
				if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {// 登陆成功
 					toDeviceList();
				} else if (result == GizWifiErrorCode.GIZ_OPENAPI_USER_NOT_EXIST) {// 用户不存在
					toast(R.string.GIZ_OPENAPI_USER_NOT_EXIST);
				} else if (result == GizWifiErrorCode.GIZ_OPENAPI_USERNAME_PASSWORD_ERROR) {//// 用户名或者密码错误
					toast(R.string.GIZ_OPENAPI_USERNAME_PASSWORD_ERROR);
				} else {
					toast("登陆失败，请重新登录");
				}
			}
			/** 用于用户注册的回调 */
			@Override
			public void registerUserCb(GizWifiErrorCode result, String uid, String token) {
				Logger.d(TAG, "registerUserCb result:" + result + " uid:" + uid + " token:" + token);
				dialogUtils.sendMessage(ProgressDialogUtils.DISMISS);
				/** 用于用户注册 */
				if (result == GizWifiErrorCode.GIZ_SDK_SUCCESS) {
					Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_LONG).show();
					toDeviceList();
					finish();
				} else if (result == GizWifiErrorCode.GIZ_OPENAPI_USERNAME_UNAVALIABLE) {
					Toast.makeText(getApplicationContext(), "userName  unavaliabale", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), "注册失败，请重新注册", Toast.LENGTH_LONG).show();
				}
				
			};
		});
	}
	private void toDeviceList() {
		toast("登陆成功");
		startActivity(new Intent(this,DeviceListActivity.class));
		finish();
	}
	
	private void initView() {
		etName = (EditText) findViewById(R.id.et_name);
		etPsw = (EditText) findViewById(R.id.et_psw);
		dialogUtils = new ProgressDialogUtils(this);
	}

	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.btn_login:
			if (!TextUtils.isEmpty(etName.getText().toString()) && !TextUtils.isEmpty(etPsw.getText().toString())) {
				dialogUtils.sendMessage(ProgressDialogUtils.SHOW);
				DeviceManager.instanceDeviceManager(getApplicationContext()).userLogin(etName.getText().toString(), etPsw.getText().toString());
			} else {
				toast("请输入完整信息");
			}
			break;
		case R.id.btn_register:
			if (!TextUtils.isEmpty(etName.getText().toString()) && !TextUtils.isEmpty(etPsw.getText().toString())) {
				dialogUtils.sendMessage(ProgressDialogUtils.SHOW);
				DeviceManager.instanceDeviceManager(getApplicationContext()).registerUser(etName.getText().toString(), etPsw.getText().toString(),"",GizUserAccountType.GizUserNormal);
			} else {
				toast("请输入完整信息");
			}
			break;
		case R.id.btn_anonymous_login:
			dialogUtils.sendMessage(ProgressDialogUtils.SHOW);
			DeviceManager.instanceDeviceManager(getApplicationContext()).userLoginAnonymous();
			break;
		default:
			break;
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
	}

	@Override
	public void onBackPressed() {
		finish();
	};

}
