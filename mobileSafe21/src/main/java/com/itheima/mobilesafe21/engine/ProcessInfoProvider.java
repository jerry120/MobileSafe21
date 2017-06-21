package com.itheima.mobilesafe21.engine;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageManager.NameNotFoundException;
import android.content.pm.ProviderInfo;
import android.content.pm.ServiceInfo;
import android.graphics.drawable.Drawable;
import android.os.Build;

import com.itheima.mobilesafe21.R;
import com.itheima.mobilesafe21.bean.ProcessBean;

public class ProcessInfoProvider {
	// 获取系统中正在执行的进程数
	public static int getRunningProcessCount(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = activityManager
				.getRunningAppProcesses();

		return runningAppProcesses.size();
	}

	// 获取系统中所有的进程数
	public static int getAllProcessCount(Context context) {
		// 获取安装在设备上的所有的应用包，获取每个应用包的process属性值(application与四大组件的所有的process属性)
		PackageManager packageManager = context.getPackageManager();
		// 标记设置四大组件的标记，能够获取所有的数据
		List<PackageInfo> installedPackages = packageManager
				.getInstalledPackages(PackageManager.GET_ACTIVITIES
						| PackageManager.GET_PROVIDERS
						| PackageManager.GET_RECEIVERS
						| PackageManager.GET_SERVICES);
		Set<String> processes = new HashSet<String>();// 创建一个去重的数据集合

		for (PackageInfo packageInfo : installedPackages) {
			ApplicationInfo applicationInfo = packageInfo.applicationInfo;
			// 记录application的进程名
			String processName = applicationInfo.processName;
			processes.add(processName);
			// 获取四大组件的进程名
			ActivityInfo[] activities = packageInfo.activities;
			if (activities != null) {
				for (ActivityInfo activityInfo : activities) {
					processes.add(activityInfo.processName);
				}

			}

			ProviderInfo[] providers = packageInfo.providers;
			if (providers != null) {
				for (ProviderInfo providerInfo : providers) {
					processes.add(providerInfo.processName);
				}
			}

			ActivityInfo[] receivers = packageInfo.receivers;
			if (receivers != null) {

				for (ActivityInfo activityInfo : receivers) {
					processes.add(activityInfo.processName);
				}
			}

			ServiceInfo[] services = packageInfo.services;
			if (services != null) {

				for (ServiceInfo serviceInfo : services) {
					processes.add(serviceInfo.processName);
				}
			}

		}

		return processes.size();
	}

	// 获取可用的内存数
	public static long getAvialMemory(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		activityManager.getMemoryInfo(outInfo);// 赋值函数
		long availMem = outInfo.availMem;
		return availMem;
	}

	// 获取总的内存数
	@SuppressLint("NewApi")
	public static long getTotalMemory(Context context) {
		ActivityManager activityManager = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		activityManager.getMemoryInfo(outInfo);// 赋值函数
		// 如果硬件的sdk>=16，直接使用字段，如果<16那就使用别的方法
		// build是应用运行时生成的系统文件
		long totalMem = 0;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
			totalMem = outInfo.totalMem;
		} else {
			// 使用低版本的方式获取总的内存
			//读取系统文件proc/meminfo
			File file = new File("proc/meminfo");
			try {
				BufferedReader br = new BufferedReader(new FileReader(file));
				String readLine = br.readLine();//MemTotal:         513492 kB
				readLine = readLine.replace("MemTotal:", "").trim();
				readLine = readLine.replace("kB", "").trim();
				totalMem = Integer.valueOf(readLine)*1024;
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

		return totalMem;
	}
	
	
	//用来获取正在运行的进程的信息javabean
	public static List<ProcessBean> getRunningProcessInfos(Context context){
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
		//遍历集合，封装每个进程的信息（应用图标，应用名，应用内存）
		
		PackageManager packageManager = context.getPackageManager();
		
		List<ProcessBean> datas = new ArrayList<ProcessBean>();
		
		for (RunningAppProcessInfo runningAppProcessInfo : runningAppProcesses) {
			String packageName = runningAppProcessInfo.processName;//进程名与包名一致
			
			
			ProcessBean bean = new ProcessBean();
			
			try {
				PackageInfo packageInfo = packageManager.getPackageInfo(packageName, 0);
				ApplicationInfo applicationInfo = packageInfo.applicationInfo;
//				applicationInfo.icon;//得到了别人的应用的图片资源id，对于本应用来说不可以
//				applicationInfo.labelRes;//得到了别人的应用的String资源id，对于本应用来说不可以
				Drawable appIcon = applicationInfo.loadIcon(packageManager);
				String appName = applicationInfo.loadLabel(packageManager).toString();
				
				int flags = applicationInfo.flags;
				boolean isSystem;
				
				if ((flags&ApplicationInfo.FLAG_SYSTEM) == ApplicationInfo.FLAG_SYSTEM) {
					isSystem = true;
				}else{
					isSystem = false;
				}
				
				bean.appIcon = appIcon;
				bean.appName = appName;
				
				bean.isSystem = isSystem;
				
			} catch (NameNotFoundException e) {
				e.printStackTrace();
				bean.appName = packageName;
				bean.appIcon = context.getResources().getDrawable(R.drawable.ic_launcher);
				bean.isSystem = true;
			}
			
			//获取每个进程的内存占用信息
			//参数：要查询的进程id数组
			android.os.Debug.MemoryInfo[] infos = activityManager.getProcessMemoryInfo(new int[]{runningAppProcessInfo.pid});
			android.os.Debug.MemoryInfo memoryInfo = infos[0];
			int totalPss = memoryInfo.getTotalPss();//一个进程的内存包含底层c代码的内存+虚拟机内存+应用层的内存
			bean.appMemory = totalPss*1024;
			bean.packageName = packageName;
			datas.add(bean);
		}
		return datas;
	}
	
	
	//清理进程
	public static void cleanProcess(Context context, String packageName){
		ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		
		activityManager.killBackgroundProcesses(packageName);
	}
	
//直接清理掉所有的后台进程
	public static void cleanAllProcess(Context context) {
		List<ProcessBean> datas = getRunningProcessInfos(context);
		for (ProcessBean processBean : datas) {
			cleanProcess(context, processBean.packageName);
		}
	}
}
