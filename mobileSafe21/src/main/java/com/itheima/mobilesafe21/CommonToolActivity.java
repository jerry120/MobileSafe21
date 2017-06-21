package com.itheima.mobilesafe21;

import com.itheima.mobilesafe21.service.WatchDogService;
import com.itheima.mobilesafe21.utils.SeriveStateUtil;
import com.itheima.mobilesafe21.view.SettingItemView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import com.itheima.mobilesafe21.CommonNumberActivity;;


public class CommonToolActivity extends Activity implements OnClickListener {

	private SettingItemView siv_commomtool_location;
	private SettingItemView siv_commomtool_number;
	private SettingItemView siv_commomtool_applock;
	private SettingItemView siv_commomtool_dog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_common_tool);
		initView();
	}

	private void initView() {
		siv_commomtool_location = (SettingItemView) findViewById(R.id.siv_commomtool_location);
		siv_commomtool_number = (SettingItemView) findViewById(R.id.siv_commomtool_number);
		siv_commomtool_applock = (SettingItemView) findViewById(R.id.siv_commomtool_applock);
		siv_commomtool_dog = (SettingItemView) findViewById(R.id.siv_commomtool_dog);
		
		siv_commomtool_location.setOnClickListener(this);
		siv_commomtool_number.setOnClickListener(this);
		siv_commomtool_applock.setOnClickListener(this);
		siv_commomtool_dog.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.siv_commomtool_location:
			startActivity(new Intent(CommonToolActivity.this, LocationActivity.class));
			
			break;
		case R.id.siv_commomtool_number://常用号码
			startActivity(new Intent(CommonToolActivity.this,CommonNumberActivity.class));
			
			
			break;
		case R.id.siv_commomtool_applock://程序锁
			startActivity(new Intent(CommonToolActivity.this,AppLockActivity.class));
			
			break;
		case R.id.siv_commomtool_dog://电子狗服务
			siv_commomtool_dog.toggle();
			if (SeriveStateUtil.isServiceRunning(getApplicationContext(), WatchDogService.class)) {
				stopService(new Intent(CommonToolActivity.this, WatchDogService.class));
			}else{
				startService(new Intent(CommonToolActivity.this, WatchDogService.class));
			}
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		if (SeriveStateUtil.isServiceRunning(getApplicationContext(), WatchDogService.class)) {
			siv_commomtool_dog.setToggle(true);
		}else{
			siv_commomtool_dog.setToggle(false);
		}
	}
}
