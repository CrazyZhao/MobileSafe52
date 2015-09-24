package com.itheima52.mobilesafe.receiver;

import com.itheima52.mobilesafe.R;
import com.itheima52.mobilesafe.service.LocationService;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.telephony.SmsMessage;

/**
 * 拦截短信
 * 
 * @author baoliang.zhao
 * 
 */
public class SmsReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		Object[] objects = (Object[]) intent.getExtras().get("pdus");

		for (Object object : objects) {

			SmsMessage message = SmsMessage.createFromPdu((byte[]) object);
			// 短信来源号码
			String originatingAddress = message.getOriginatingAddress();
			// 短信内容
			String messageBody = message.getMessageBody();

			if ("#*alarm*#".equals(messageBody)) {
				// 播放报警音乐
				MediaPlayer player = MediaPlayer.create(context, R.raw.ylzs);
				player.setVolume(1f, 1f);
				player.setLooping(true);
				player.start();

				abortBroadcast();// 中断短信的传递，从而系统短信app就收不到短信了
			} else if ("#*location*#".equals(messageBody)) {
				// 获取经纬度坐标
				context.startService(new Intent(context, LocationService.class));// 开启定位服务

				SharedPreferences sp = context.getSharedPreferences("config",
						Context.MODE_PRIVATE);
				String location = sp.getString("location",
						"getting location...");
				System.out.println(location);
				abortBroadcast();// 中断短信的传递，从而系统短信app就收不到短信了
			}
		}
	}
}
