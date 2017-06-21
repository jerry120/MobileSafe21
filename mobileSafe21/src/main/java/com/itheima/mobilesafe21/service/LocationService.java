package com.itheima.mobilesafe21.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.WindowManager;
import android.widget.TextView;
import android.widget.Toast;

import com.itheima.mobilesafe21.db.LocationDao;
import com.itheima.mobilesafe21.view.LocationToast;

public class LocationService extends Service {

	private TelephonyManager telephonyManager;
	private PhoneStateListener listener = new PhoneStateListener() {
		/**
		 * 参数一：电话状态 参数二：来电电话号码
		 */
		public void onCallStateChanged(int state, String incomingNumber) {
			switch (state) {
			case TelephonyManager.CALL_STATE_IDLE: // 空闲状态
//				hideLocationToast();
				locationToast.hideLocationToast();

				break;
			case TelephonyManager.CALL_STATE_RINGING: // 响铃状态
				String location = LocationDao.queryLocation(
						getApplicationContext(), incomingNumber);
				Toast.makeText(getApplicationContext(), location, 0).show();
				// 显示归属地
//				showLocationToast(location);
				locationToast.showLocationToast( location);

				break;
			case TelephonyManager.CALL_STATE_OFFHOOK: // 摘机状态

				break;

			default:
				break;
			}
		};
	};

	private BroadcastReceiver receiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			// 获取去电的号码，查询
			String number = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER);
			String location = LocationDao.queryLocation(
					getApplicationContext(), number);
			Toast.makeText(getApplicationContext(), location, 0).show();

			// 显示归属地
//			showLocationToast(location);
			locationToast.showLocationToast( location);
		}
	};
//	private TextView mView;
//	private WindowManager mWM;


	private LocationToast locationToast;	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

//	// 显示归属地
//	protected void showLocationToast(String location) {
//
//		// 窗口管理器 添加，删除，修改窗口
//		// 窗口：是android中最顶层元素，view一定是基于window才能显示，activity、dialog、toast 都是window
//
//		mWM = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
//		mView = new TextView(this);
//		mView.setText(location);
//		mView.setTextColor(Color.RED);
//
//		// 设置窗口一些属性
//		WindowManager.LayoutParams mParams = new WindowManager.LayoutParams();
//		mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
//		mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
//		mParams.flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE//设置不可获取焦点
//				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE//设置不可触摸
//				| WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;//设置始终亮屏
//		mParams.format = PixelFormat.TRANSLUCENT;
//		mParams.type = WindowManager.LayoutParams.TYPE_TOAST;//设置窗口类型
//		mParams.setTitle("Toast");
//
//		// 创建出一个视图为mView的窗口并添加给窗口管理
//		mWM.addView(mView, mParams);
//	}
//
//	// 隐藏归属地
//	public void hideLocationToast() {
//		if (mView != null) {
//			// note: checking parent() just to make sure the view has
//			// been added... i have seen cases where we get here when
//			// the view isn't yet added, so let's try not to crash.
//			if (mView.getParent() != null) {
//				mWM.removeView(mView);
//			}
//
//			mView = null;
//		}
//	}

	@Override
	public void onCreate() {
		super.onCreate();

		locationToast = new LocationToast(this);
		
		System.out.println("归属地服务打开了");

		// 监听来去电
		// telephonyManager监听来电
		telephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_CALL_STATE);

		// 广播监听去电
		IntentFilter filter = new IntentFilter();
		filter.addAction(Intent.ACTION_NEW_OUTGOING_CALL);
		registerReceiver(receiver, filter);

	}

	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		super.onDestroy();
		System.out.println("归属地服务关闭了");

		// 服务关闭时，注销电话监听
		telephonyManager.listen(listener, PhoneStateListener.LISTEN_NONE);

		// 服务关闭时，注销去电广播
		unregisterReceiver(receiver);
	}

}
