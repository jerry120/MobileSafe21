package com.itheima.mobilesafe21.service;

import java.util.List;

import com.itheima.mobilesafe21.PassWordActivity;
import com.itheima.mobilesafe21.db.AppLockDao;
import com.itheima.mobilesafe21.utils.Constant;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningTaskInfo;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;
import android.os.SystemClock;
import android.text.TextUtils;

public class WatchDogService extends Service {

	private ActivityManager activityManager;
	private AppLockDao appLockDao;
	private String skipPackageName;
	private boolean isRunning;//控制子线程的打开与关闭
	private BroadcastReceiver receiver = new BroadcastReceiver(){


		@Override
		public void onReceive(Context context, Intent intent) {
			skipPackageName = intent.getStringExtra(Constant.KEY_PACKAGE_NAME);
		}
		
	};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("电子狗服务打开了");
		appLockDao = new AppLockDao(this);
		activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
		IntentFilter filter = new IntentFilter();
		filter.addAction(Constant.ACTION_SKIP_PACKAGE_NAME);
		registerReceiver(receiver, filter);
		startWatch();
	}
	
	private void startWatch() {
		isRunning = true;
		new Thread(){
			

			public void run() {
				while (isRunning) {
					//用户打开了哪一个应用程序
					List<RunningTaskInfo> runningTasks = activityManager.getRunningTasks(1);
					RunningTaskInfo runningTaskInfo = runningTasks.get(0);//得到当前用户打开的任务栈
					ComponentName topActivity = runningTaskInfo.topActivity;
					String packageName = topActivity.getPackageName();
					if (TextUtils.equals(packageName, skipPackageName)) {
						continue;
					}
					
					if (appLockDao.query(packageName)) {
						System.out.println("当前是一个已加锁的应用："+packageName);
						Intent intent = new Intent(WatchDogService.this, PassWordActivity.class);
						//FLAG_ACTIVITY_NEW_TASK，如果有本应用的任务栈直接将PassWordActivity添加进入，如果不存在，那么新建再放入
						intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
						intent.putExtra(Constant.KEY_PACKAGE_NAME, packageName);
						startActivity(intent);
					}
					
					SystemClock.sleep(100);
				}
			};
		}.start();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		System.out.println("电子狗服务关闭了");
		unregisterReceiver(receiver);
		
		isRunning = false;
	}

}
