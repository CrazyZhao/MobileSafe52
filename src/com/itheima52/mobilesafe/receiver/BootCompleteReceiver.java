package com.itheima52.mobilesafe.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
				context.MODE_PRIVATE);
		String sim = sp.getString("sim", null);
		if (!TextUtils.isEmpty(sim)) {
			// 获取当前手机的SIM卡
			TelephonyManager tm = (TelephonyManager) context
					.getSystemService(Context.TELEPHONY_SERVICE);
			String currentSim = tm.getSimSerialNumber(); // 拿到当前手机的SIM卡序列号

			if (sim.equals(currentSim)) {

			} else {

			}
		}
	}

}
