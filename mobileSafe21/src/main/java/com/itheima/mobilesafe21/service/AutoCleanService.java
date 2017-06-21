package com.itheima.mobilesafe21.service;

import com.itheima.mobilesafe21.engine.ProcessInfoProvider;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class AutoCleanService extends Service {

	private BroadcastReceiver receiver = new BroadcastReceiver(){

		@Override
		public void onReceive(Context context, Intent intent) {
			ProcessInfoProvider.cleanAllProcess(AutoCleanService.this);
		}};

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	
	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("自动清理服务开启了");
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		//注册广播，监听锁频操作，将后台的进程自动清理掉
		registerReceiver(receiver, filter );
	}
	
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("自动清理服务关闭了");
		unregisterReceiver(receiver);
	}
}
