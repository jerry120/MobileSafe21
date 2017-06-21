package com.itheima.mobilesafe21;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

import com.itheima.mobilesafe21.service.LocationService;
import com.itheima.mobilesafe21.utils.Constant;
import com.itheima.mobilesafe21.utils.SeriveStateUtil;
import com.itheima.mobilesafe21.utils.SpUtil;
import com.itheima.mobilesafe21.view.LocationStyleDialog;
import com.itheima.mobilesafe21.view.SettingItemView;

public class SettingActivity extends Activity implements OnClickListener {

	private SettingItemView siv_autoupdate;
	private SettingItemView siv_location;
	private SettingItemView siv_location_style;
	
	

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_setting);
		initView();
	}

	private void initView() {
		siv_autoupdate = (SettingItemView) findViewById(R.id.siv_autoupdate);
		siv_location = (SettingItemView) findViewById(R.id.siv_location);
		siv_location_style = (SettingItemView) findViewById(R.id.siv_location_style);
		
		siv_autoupdate.setOnClickListener(this);
		siv_location.setOnClickListener(this);
		siv_location_style.setOnClickListener(this);
		
		//根据sp记录的状态动态设置开关
//		SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
//		boolean flag = sp.getBoolean("autoupdate", true);
		siv_autoupdate.setToggle(SpUtil.getBoolean(SettingActivity.this, Constant.KEY_AUTO_UPDATE, true));
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.siv_autoupdate://自动更新
//			siv_autoupdate.setToggle(true);
//			siv_autoupdate.setToggle(false);
			
//			if (flag) {
//				siv_autoupdate.setToggle(false);
//				flag = false;
//			}else{
//				siv_autoupdate.setToggle(true);
//				flag = true;
//			}
			
//			siv_autoupdate.setToggle(!flag);
//			flag = !flag;
			
//			siv_autoupdate.setToggle(!siv_autoupdate.isToggle());
			
			siv_autoupdate.toggle();
//			SharedPreferences sp = getSharedPreferences("config", MODE_PRIVATE);
//			sp.edit().putBoolean("autoupdate", siv_autoupdate.isToggle()).commit();
			SpUtil.saveBoolean(SettingActivity.this, Constant.KEY_AUTO_UPDATE, siv_autoupdate.isToggle());
			break;
		case R.id.siv_location://归属地显示风格
			siv_location.toggle();
			//根据服务的状态来切换，如果服务是打开的就关闭，如果如果是关闭的就打开
			if (SeriveStateUtil.isServiceRunning(getApplicationContext(), LocationService.class)) {
				stopService(new Intent(SettingActivity.this, LocationService.class));
			}else{
				startService(new Intent(SettingActivity.this, LocationService.class));
			}
			
			
			
			break;
		case R.id.siv_location_style://显示设置
			LocationStyleDialog dialog = new LocationStyleDialog(SettingActivity.this);
			dialog.setTitle("xxxx");
			dialog.show();
			
			break;

		default:
			break;
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		//当界面重新显示时，直接根据服务的状态动态设置开关
		if (SeriveStateUtil.isServiceRunning(getApplicationContext(), LocationService.class)) {
			siv_location.setToggle(true);
		}else {
			siv_location.setToggle(false);
		}
	}
}
