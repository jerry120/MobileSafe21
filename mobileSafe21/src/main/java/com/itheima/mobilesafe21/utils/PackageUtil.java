package com.itheima.mobilesafe21.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;

public class PackageUtil {
	//获取版本名
	public static String getVersionName(Context context, String packageName){
		PackageManager packageManager = context.getPackageManager();
		/**
		 * 参数一：要查询的包名
		 * 参数二：标记 给0，获取清单文件的基本信息(如果要获取对应的信息（四大组件，权限要设置对应的标记）)
		 */
		String versionName = "";
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
			versionName = packageInfo.versionName;
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versionName;
	}
	//获取版本号
	public static int getVersionCode(Context context, String packageName){
		PackageManager packageManager = context.getPackageManager();
		/**
		 * 参数一：要查询的包名
		 * 参数二：标记 给0，获取清单文件的基本信息(如果要获取对应的信息（四大组件，权限要设置对应的标记）)
		 */
		int versionCode = 1;
		try {
			PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
			versionCode = packageInfo.versionCode;
			
		} catch (NameNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return versionCode;
	}
}
