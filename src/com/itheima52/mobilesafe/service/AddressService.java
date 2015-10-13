package com.itheima52.mobilesafe.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.dao.AddressDao;

/**
 * 来电提醒的服务
 * 
 * @author baoliang.zhao
 * 
 */
public class AddressService extends Service {

	private TelephonyManager tm;
	private MyListener listener;
	private OutCallReceiver receiver;
	private WindowManager mWM;
	private View view;
	private SharedPreferences mPref;
	protected int startX;
	protected int startY;
	private WindowManager.LayoutParams params;
	private int winWidth;
	private int winHeight;

	@Override
	public IBinder onBind(Intent intent) {

		return null;
	}

	@Override
	public void onCreate() {
		super.onCreate();
		mPref = getSharedPreferences("config", MODE_PRIVATE);

		tm = (TelephonyManager) getSystemService(TELEPHONY_SERVICE);
		listener = new MyListener();
		tm.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);// 监听来电的状态

		receiver = new OutCallReceiver();
		IntentFilter filter = new IntentFilter(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);// 动态注册广播
	}

	class MyListener extends PhoneStateListener {
		@Override
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_RINGING:// 电话铃响
				System.out.println("电话铃响了!");
				String address = AddressDao.getAddress(incomingNumber);// 根据来电号码查询归属地
				// Toast.makeText(AddressService.this, address,
				// Toast.LENGTH_LONG)
				// .show();
				showToast(address);
				break;
			case TelephonyManager.CALL_STATE_IDLE:// 电话闲置
				if (mWM != null && view != null) {
					mWM.removeView(view);// 从WindowManager中移除View
					view = null;
				}
				break;

			default:
				break;
			}
			super.onCallStateChanged(state, incomingNumber);
		}
	}

	/**
	 * 监听去电的广播接收者 需要权限：android.permission.PROCESS_OUTGOING_CALLS
	 * 
	 * @author baoliang.zhao
	 * 
	 */
	class OutCallReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			String number = getResultData();// 获得去电的号码
			String address = AddressDao.getAddress(number);
			// Toast.makeText(context, address, Toast.LENGTH_LONG).show();
			showToast(address);
		}

	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		tm.listen(listener, PhoneStateListener.LISTEN_NONE);// 停止来电监听
		unregisterReceiver(receiver);// 注销广播
	}

	/**
	 * 自定义归属地浮窗 需要权限android.permission.SYSTEM_ALERT_WINDOW
	 */
	private void showToast(String text) {
		mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
		// 获取屏幕宽度和高度
		winWidth = mWM.getDefaultDisplay().getWidth();
		winHeight = mWM.getDefaultDisplay().getHeight();

		params = new WindowManager.LayoutParams();
		params.height = WindowManager.LayoutParams.WRAP_CONTENT;
		params.width = WindowManager.LayoutParams.WRAP_CONTENT;
		params.format = PixelFormat.TRANSLUCENT;
		params.type = WindowManager.LayoutParams.TYPE_PHONE;//需要权限android.permission.SYSTEM_ALERT_WINDOW
		params.setTitle("Toast");
		params.gravity = Gravity.LEFT + Gravity.TOP;// 将重心位置设置为左上方(0,0),而不是默认的中心位置
		params.flags = WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON
				| WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;

		int lastX = mPref.getInt("lastX", 0);
		int lastY = mPref.getInt("lastY", 0);
		// 设置浮窗的位置：基于左上方的偏移量
		params.x = lastX;
		params.y = lastY;

		// view = new TextView(this);
		view = View.inflate(this, R.layout.toast_address, null);

		int[] bgs = new int[] { R.drawable.call_locate_white,
				R.drawable.call_locate_orange, R.drawable.call_locate_blue,
				R.drawable.call_locate_gray, R.drawable.call_locate_green };
		int style = mPref.getInt("address_style", 0);// 读取保存的

		view.setBackgroundResource(bgs[style]);// 根据存储的样式更新提示框风格
		TextView tvText = (TextView) view.findViewById(R.id.tv_number);
		tvText.setText(text);
		mWM.addView(view, params);// 将view添加到屏幕上：Window、

		view.setOnTouchListener(new OnTouchListener() {

			@Override
			public boolean onTouch(View v, MotionEvent event) {
				switch (event.getAction()) {
				case MotionEvent.ACTION_DOWN:
					// 初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_MOVE:
					int endX = (int) event.getRawX();
					int endY = (int) event.getRawY();
					// 计算移动偏移量
					int dx = endX - startX;
					int dy = endY - startY;
					// 更新左上右下距离
					params.x += dx;
					params.y += dy;
					//防止拖拽出屏幕边界
					if (params.x < 0) {
						params.x = 0;
					}
					if (params.y < 0) {
						params.y = 0;
					}
					if (params.x > winWidth - view.getWidth()) {
						params.x = winWidth - view.getWidth();
					}
					if (params.y > winHeight - view.getHeight()) {
						params.y = winHeight - view.getHeight();
					}

					mWM.updateViewLayout(view, params);
					// 重新初始化起点坐标
					startX = (int) event.getRawX();
					startY = (int) event.getRawY();
					break;
				case MotionEvent.ACTION_UP:
					// 记录坐标点
					Editor editor = mPref.edit();
					editor.putInt("lastX", params.x);
					editor.putInt("lastY", params.y);
					editor.commit();
					break;

				default:
					break;
				}
				return true;
			}
		});
	}

}
