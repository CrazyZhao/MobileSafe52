package com.itheima52.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.telephony.SmsManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

/**
 * 监听手机开机启动的广播
 * 
 * @author baoliang.zhao
 * 
 */
public class BootCompleteReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {

		SharedPreferences sp = context.getSharedPreferences("config",
				Context.MODE_PRIVATE);
		boolean protect = sp.getBoolean("protect", false);
		// 只有在防盗保护开启的前提下开机才进行sim卡判断
		if (protect) {
			String sim = sp.getString("sim", null);
			if (!TextUtils.isEmpty(sim)) {
				// 获取当前手机的SIM卡
				TelephonyManager tm = (TelephonyManager) context
						.getSystemService(Context.TELEPHONY_SERVICE);
				String currentSim = tm.getSimSerialNumber() + "11"; // 拿到当前手机的SIM卡序列号

				if (sim.equals(currentSim)) {
					System.out.println("手机安全！");
				} else {
					System.out.println("手机被盗！");
					String phone = sp.getString("safe_phone", "");
					// 发送短信给安全号码
					SmsManager smsManager = SmsManager.getDefault();
					smsManager.sendTextMessage(phone, null,
							"sim card changed!", null, null);

				}
			}
		}

	}

}
