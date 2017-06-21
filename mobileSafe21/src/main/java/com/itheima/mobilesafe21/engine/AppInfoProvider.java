package com.itheima.mobilesafe21.engine;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.Drawable;

import com.itheima.mobilesafe21.bean.AppInfoBean;

public class AppInfoProvider {
	//获取所有的应用信息
	public static List<AppInfoBean> getAllAppInfos(Context context){
		//获取安装在设备上的所用的应用包信息
		PackageManager packageManager = context.getPackageManager();
		List<PackageInfo> installedPackages = packageManager.getInstalledPackages(0);
		List<AppInfoBean> datas = new ArrayList<AppInfoBean>();
		for (PackageInfo packageInfo : installedPackages) {
			AppInfoBean appInfoBean = new AppInfoBean();
			
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			Drawable appIcon = applicationInfo.loadIcon(packageManager);
			String appName = applicationInfo.loadLabel(packageManager).toString();
			appInfoBean.appIcon = appIcon;
			appInfoBean.appName = appName;
			appInfoBean.packageName = packageInfo.packageName;//包名
			appInfoBean.apkPath = applicationInfo.sourceDir;
			datas.add(appInfoBean);
		}
		
		return datas;
	}

}
