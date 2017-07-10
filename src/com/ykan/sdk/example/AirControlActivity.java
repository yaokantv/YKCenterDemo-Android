package com.ykan.sdk.example;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import com.gizwits.gizwifisdk.api.GizWifiDevice;
import com.gizwits.gizwifisdk.enumration.GizWifiDeviceNetStatus;
import com.gizwits.gizwifisdk.enumration.GizWifiErrorCode;
import com.google.gson.reflect.TypeToken;
import com.yaokan.sdk.api.JsonParser;
import com.yaokan.sdk.model.DeviceDataStatus;
import com.yaokan.sdk.model.KeyCode;
import com.yaokan.sdk.utils.Logger;
import com.yaokan.sdk.utils.Utility;
import com.yaokan.sdk.wifi.DeviceController;
import com.yaokan.sdk.wifi.listener.IDeviceControllerListener;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
public class AirControlActivity  extends BaseActivity implements IDeviceControllerListener{
	protected static final String TAG = AirControlActivity.class.getSimpleName();
	
	/** The tv MAC */
	private TextView tvMAC;

	/** The GizWifiDevice device */
	private GizWifiDevice device;
	
	private String rcCommand = "";
	
     private HashMap<String,KeyCode> codeDatas = new HashMap<String,KeyCode>();
	
	private List<String> codeKeys = new ArrayList<String>();
	
	private DeviceController driverControl = null;
	
 	
 	
	JsonParser jsonParser = new JsonParser();
	
	 
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.air_control);
		initDevice();
		initView();
	}
	
	
	
	private void initDevice() {
		Intent intent = getIntent();
		device = (GizWifiDevice) intent.getParcelableExtra("GizWifiDevice");
		rcCommand = intent.getStringExtra("rcCommand");
	}
	
	
	
	
	private void initView() {
		driverControl = new DeviceController(getApplicationContext(),device,this);
		//获取设备硬件相关信息
		driverControl.getDevice().getHardwareInfo();
		//修改设备显示名称
		driverControl.getDevice().setCustomInfo("alias", "遥控中心产品");
		tvMAC = (TextView) findViewById(R.id.tvMAC);
 		if (null != device) {
			tvMAC.setText("MAC: " + device.getMacAddress().toString());
		}
		if(!Utility.isEmpty(rcCommand)){
			codeDatas = new HashMap<String,KeyCode>();
			Type type = new TypeToken<HashMap<String, KeyCode>>() {}.getType();
			codeDatas = jsonParser.parseObjecta(rcCommand, type);
			codeKeys = new ArrayList<String>(codeDatas.keySet());
 			 
			 
		}
	}



	@Override
	public void didGetHardwareInfo(GizWifiErrorCode result,GizWifiDevice device, ConcurrentHashMap<String, String> hardwareInfo) {
		Logger.d(TAG, "获取设备信息 :");
		if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
			Logger.d(TAG, "获取设备信息 : hardwareInfo :" + hardwareInfo);
		} else {
		}
	}



	@Override
	public void didReceiveData(DeviceDataStatus arg0, String arg1) {
		// TODO Auto-generated method stub
		
	}



	@Override
	public void didSetCustomInfo(GizWifiErrorCode result, GizWifiDevice device) {
		Logger.d(TAG, "自定义设备信息回调");
		if (GizWifiErrorCode.GIZ_SDK_SUCCESS == result) {
			Logger.d(TAG, "自定义设备信息成功");
		}
	}


	@Override
	public void didUpdateNetStatus(GizWifiDevice device,GizWifiDeviceNetStatus netStatus) {
		switch (device.getNetStatus()) {
		case GizDeviceOffline:
			Logger.d(TAG, "设备下线");
			break;
		case GizDeviceOnline:
			Logger.d(TAG, "设备上线");
			break;
		default:
			break;
		}
	}
	
	

}
