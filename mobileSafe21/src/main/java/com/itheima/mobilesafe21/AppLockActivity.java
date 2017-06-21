package com.itheima.mobilesafe21;

import java.util.ArrayList;
import java.util.List;

import com.itheima.mobilesafe21.adapter.AppLockAdapter;
import com.itheima.mobilesafe21.bean.AppInfoBean;
import com.itheima.mobilesafe21.db.AppLockDao;
import com.itheima.mobilesafe21.engine.AppInfoProvider;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class AppLockActivity extends Activity implements OnClickListener {

	private Button bt_unlock;
	private Button bt_locked;
	private TextView tv_applock;
	private ListView lv_locked;
	private ListView lv_unlock;
	private View ll_loading;

	private List<AppInfoBean> unLockDatas = new ArrayList<AppInfoBean>();// 未加锁的数据集合
	private List<AppInfoBean> lockedDatas = new ArrayList<AppInfoBean>();// 已加锁的数据集合
	private AppLockDao appLockDao;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_app_lock);
		initView();
		initData();
	}

	private void initView() {
		bt_unlock = (Button) findViewById(R.id.bt_unlock);
		bt_locked = (Button) findViewById(R.id.bt_locked);
		tv_applock = (TextView) findViewById(R.id.tv_applock);
		lv_locked = (ListView) findViewById(R.id.lv_locked);
		lv_unlock = (ListView) findViewById(R.id.lv_unlock);

		bt_unlock.setOnClickListener(this);
		bt_locked.setOnClickListener(this);

		ll_loading = findViewById(R.id.ll_loading);

		appLockDao = new AppLockDao(AppLockActivity.this);
	}

	private void initData() {
		new Thread() {
			public void run() {
				SystemClock.sleep(2000);

				List<AppInfoBean> allAppInfos = AppInfoProvider
						.getAllAppInfos(AppLockActivity.this);
				// 遍历数据，使用数据库查询区分未加锁与已加锁数据

				List<String> queryAll = appLockDao.queryAll();
				for (AppInfoBean appInfoBean : allAppInfos) {
					// if (appLockDao.query(appInfoBean.packageName)) {
					if (queryAll.contains(appInfoBean.packageName)) {//只查询一次数据库直接用对象的形式来判断
						lockedDatas.add(appInfoBean);
					} else {
						unLockDatas.add(appInfoBean);
					}
					// 为了显示效果，先添加静态数据
//					if (TextUtils.equals(appInfoBean.packageName,
//							"com.android.browser")) {
//						lockedDatas.add(appInfoBean);
//					}

				}
				// unLockDatas.addAll(allAppInfos);

				runOnUiThread(new Runnable() {
					public void run() {
						ll_loading.setVisibility(View.INVISIBLE);
						
						tv_applock.setText("未加锁("+unLockDatas.size()+")个");
						
						AppLockAdapter unLockAdapter = new AppLockAdapter(
								AppLockActivity.this, unLockDatas,lockedDatas, false);
						lv_unlock.setAdapter(unLockAdapter);
						AppLockAdapter lockedAdapter = new AppLockAdapter(
								AppLockActivity.this, lockedDatas,unLockDatas, true);
						lv_locked.setAdapter(lockedAdapter);

					}
				});
			};
		}.start();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.bt_locked:// 已加锁
			bt_unlock
					.setBackgroundResource(R.drawable.applock_tableft_normal_shape);
			bt_locked
					.setBackgroundResource(R.drawable.applock_tabright_pressed_shape);

			bt_unlock.setTextColor(getResources().getColor(R.color.color_app));// 如果是颜色引用型，必须使用getResources()对象
			bt_locked.setTextColor(Color.WHITE);
			tv_applock.setText("已加锁("+lockedDatas.size()+")个");
			lv_locked.setVisibility(View.VISIBLE);
			lv_unlock.setVisibility(View.GONE);

			break;
		case R.id.bt_unlock:// 未加锁
			bt_unlock
					.setBackgroundResource(R.drawable.applock_tableft_pressed_shape);
			bt_locked
					.setBackgroundResource(R.drawable.applock_tabright_normal_shape);

			bt_unlock.setTextColor(Color.WHITE);
			bt_locked.setTextColor(getResources().getColor(R.color.color_app));// 如果是颜色引用型，必须使用getResources()对象
			tv_applock.setText("未加锁("+unLockDatas.size()+")个");
			lv_unlock.setVisibility(View.VISIBLE);
			lv_locked.setVisibility(View.GONE);

			break;

		default:
			break;
		}
	}
	
	//activity用来修改数量的方法
	public void updateCount(boolean isLocked){
		if (isLocked) {
			tv_applock.setText("已加锁("+lockedDatas.size()+")个");
		}else{
			tv_applock.setText("未加锁("+unLockDatas.size()+")个");
		}
	}
}
