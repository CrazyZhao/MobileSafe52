package com.itheima52.mobilesafe.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

/**
 * 服务状态的工具类
 * 
 * @author baoliang.zhao
 * 
 */
public class ServiceStatusUtils {

	/**
	 * 检测服务是否在运行
	 * 
	 * @return
	 */
	public static boolean isServiceRunning(Context ctx, String serviceName) {
		ActivityManager am = (ActivityManager) ctx
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 获取系统所有正在运行的服务，最多一百个
		List<RunningServiceInfo> runningServices = am.getRunningServices(100);
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			String className = runningServiceInfo.service.getClassName();// 获取服务的名称
			// System.out.println(className);
			if (className.equals(serviceName)) {// 服务存在
				return true;
			}
		}
		return false;
	}
}
