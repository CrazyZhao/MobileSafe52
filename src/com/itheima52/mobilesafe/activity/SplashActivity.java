package com.itheima52.mobilesafe.activity;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.utils.StreamUtils;

public class SplashActivity extends Activity {

	protected static final int CODE_UPDATE_DIALOG = 0;

	protected static final int CODE_URL_ERROR = 1;

	protected static final int CODE_NET_ERROR = 2;

	protected static final int CODE_JSON_ERROR = 3;
	
	protected static final int CODE_ENTER_HOME = 4;

	private TextView tvVersion;
	
	//服务器返回的信息
	private String mVersionName;//版本名
	private int mVersionCode;//版本号
	private String mDesc;//版本描述
	private String mUrl;//下载链接

	private Handler mHandler = new Handler(){
		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case CODE_UPDATE_DIALOG:
				showUpdateDialog();
				break;
			case CODE_URL_ERROR:
				Toast.makeText(SplashActivity.this, "URL错误", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_NET_ERROR:
				Toast.makeText(SplashActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_JSON_ERROR:
				Toast.makeText(SplashActivity.this, "JSON数据解析错误", Toast.LENGTH_SHORT).show();
				enterHome();
				break;
			case CODE_ENTER_HOME:
				enterHome();
				break;
			default:
				break;
			}
		}
	};
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		tvVersion = (TextView)findViewById(R.id.tv_version);
		tvVersion.setText("版本号："+getVersionName());
		
		checkVersion();
	}
	
	
	/**
	 * 获取版本名
	 * @return
	 */
	private String getVersionName(){
		
		PackageManager packageManager = getPackageManager();
		String versionName = "";
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			int versionCode = packageInfo.versionCode;
			versionName = packageInfo.versionName;
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versionName;
	}
	/**
	 * 获取版本号
	 * @return
	 */
	private int getVersionCode(){
		
		PackageManager packageManager = getPackageManager();
		int versionCode = 0;
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(getPackageName(), 0);
			versionCode = packageInfo.versionCode;
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versionCode;
	}
	
	/**
	 * 从服务器获取版本信息进行校验
	 */
	private void checkVersion(){
		
		final long startTime = System.currentTimeMillis();
		//启动子线程异步加载
		new Thread(){
			@Override
			public void run() {
				
				Message msg = Message.obtain();
				HttpURLConnection connection = null;
				try {
					//本机地址用localhost，但是如果用模拟器加载本机地址时，可以用（10.0.2.2）来替换
					URL url = new URL("http://10.37.0.6:8080/update.json");
					connection = (HttpURLConnection)url.openConnection();
					connection.setRequestMethod("GET");
					connection.setConnectTimeout(5000);
					connection.setReadTimeout(5000);//响应超时
					connection.connect();
					
					int responseCode = connection.getResponseCode();//获取响应码
					if(responseCode == 200){
						InputStream inputStream = connection.getInputStream();
						String result = StreamUtils.readFromStream(inputStream);
						System.out.println("网络返回："+result);
						//解析json
						JSONObject jo = new JSONObject(result);
						mVersionName = jo.getString("versionName");
						mVersionCode = jo.getInt("versionCode");
						mDesc = jo.getString("description");
						mUrl = jo.getString("downloadUrl");
					//	System.out.println("版本号："+mVersionCode);
						
						//判断是否有更新
						if(mVersionCode>getVersionCode()){
							//说明有更新，弹出对话框
							msg.what = CODE_UPDATE_DIALOG;
							
						}else{
							//没有版本更新
							msg.what = CODE_ENTER_HOME;
						}
						
					}
					
				} catch (MalformedURLException e) {
					// URL错误异常
					msg.what = CODE_URL_ERROR;
					e.printStackTrace();
				} catch (IOException e) {
					// 网络错误异常
					msg.what = CODE_NET_ERROR;
					e.printStackTrace();
				} catch (JSONException e) {
					// json解析异常
					msg.what = CODE_JSON_ERROR;
					e.printStackTrace();
				} finally {
					long endTime = System.currentTimeMillis();
					long timeUsed = endTime - startTime;//访问网络花费的时间
					
					if(timeUsed<2000){
						//强制休眠一段时间，保证闪屏页展示两秒
						try {
							Thread.sleep(2000-timeUsed);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					mHandler.sendMessage(msg);
					if(connection!=null){
						connection.disconnect();
					}
				}
			}
		}.start();	
	}

	/**
	 * 升级对话框
	 */
	protected void showUpdateDialog() {

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("最新版本："+mVersionName);
		builder.setMessage(mDesc);
		builder.setPositiveButton("立即更新", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				System.out.println("立即更新");
			}
		});
		builder.setNegativeButton("以后再说", new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Auto-generated method stub
				enterHome();
			}
		});
		builder.show();
	}
	
	/**
	 * 进入主页面
	 */
	private void enterHome(){
		Intent intent = new Intent(this, HomeActivity.class);
		startActivity(intent);
		finish();
	}
}
