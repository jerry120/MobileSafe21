package com.itheima.mobilesafe21.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.Service;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.ComponentName;
import android.content.Context;
import android.text.TextUtils;

public class SeriveStateUtil {

	// 判断服务是否正在运行
	public static boolean isServiceRunning(Context context,Class<? extends Service> clazz) {
		// ActivityManager还安卓操作系统的任务管理
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		// 假设系统中有1000个服务正在运行，那么直接返回前100个服务集合，假设系统中只有20个服务正在运行，那就直接将20个服务全部返回
		List<RunningServiceInfo> runningServices = activityManager
				.getRunningServices(100);
		//遍历正在运行的服务与传递进来的服务做比较，如果有相同的则传递进来的服务是运行状态
		for (RunningServiceInfo runningServiceInfo : runningServices) {
			ComponentName componentName = runningServiceInfo.service;
			if (TextUtils.equals(componentName.getClassName(), clazz.getName())) {
				return true;
			}
			
		}

		return false;
	}

}
