package com.itheima.mobilesafe21.activity;

import java.util.ArrayList;
import java.util.List;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.itheima.mobilesafe21.AntvirusActivity;
import com.itheima.mobilesafe21.CommonToolActivity;
import com.itheima.mobilesafe21.ProcessManager1Activity;
import com.itheima.mobilesafe21.ProcessManagerActivity;
import com.itheima.mobilesafe21.R;
import com.itheima.mobilesafe21.SettingActivity;
import com.itheima.mobilesafe21.adapter.HomeAdapter;
import com.itheima.mobilesafe21.application.MyApplication;
import com.itheima.mobilesafe21.bean.HomeBean;

public class HomeActivity extends Activity implements OnItemClickListener {

	private ImageView iv_logo;
	private GridView gridview;

	private final static String[] TITLES = new String[] { "常用工具", "进程管理",
			"手机杀毒", "功能设置" };

	private final static String[] DESCS = new String[] { "工具大全", "管理运行进程",
			"病毒无处藏身", "管理您的软件" };

	private final static int[] ICONS = new int[] { R.drawable.cygj,
			R.drawable.jcgl, R.drawable.sjsd, R.drawable.rjgj };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		System.out.println("HomeActivity");
		MyApplication application = (MyApplication) getApplication();
		System.out.println("application.data :"+application.data);
		
		setContentView(R.layout.activity_home);
		initView();
		initData();
	}

	private void initData() {
		// 给logo执行动画
		// iv_logo.setRotation(rotation);//相当于一张纸在桌面上转动
		// iv_logo.setRotationX(rotationX)//相当于笔记本电脑的开合
		// iv_logo.setRotationY(rotationY)//相当于开门关门的效果
		// offloat方式是由属性的类型决定的，如果是int型，使用ofint方法
		ObjectAnimator oa = ObjectAnimator.ofFloat(iv_logo, "rotationY", 0, 45,
				90, 180, 270, 360);

		oa.setDuration(1000);
		oa.setRepeatMode(Animation.REVERSE);// 设置重复模式
		oa.setRepeatCount(Animation.INFINITE);// 设置重复次数
		oa.start();

		// 设置gridview界面
		List<HomeBean> datas = new ArrayList<HomeBean>();
		for (int i = 0; i < TITLES.length; i++) {
			HomeBean homeBean = new HomeBean();
			homeBean.title = TITLES[i];
			homeBean.desc = DESCS[i];
			homeBean.imageId = ICONS[i];
			datas.add(homeBean);
			
		}
		
		
		
		HomeAdapter homeAdapter = new HomeAdapter(datas,HomeActivity.this);
		gridview.setAdapter(homeAdapter);
		
		gridview.setOnItemClickListener(this);
	}

	private void initView() {
		iv_logo = (ImageView) findViewById(R.id.iv_logo);
		gridview = (GridView) findViewById(R.id.gridview);
		TextView tv = (TextView) findViewById(R.id.tv);
//		Typeface tf = Typeface.createFromAsset(getAssets(), "font/xxx.ttf");
//		tv.setTypeface(tf);
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		switch (position) {
		case 0:
			System.out.println("常用工具");
			startActivity(new Intent(HomeActivity.this, CommonToolActivity.class));
			break;
		case 1:
			System.out.println("进程管理");
			startActivity(new Intent(HomeActivity.this, ProcessManager1Activity.class));
//			startActivity(new Intent(HomeActivity.this, ProcessManagerActivity.class));
			
			break;
		case 2:
			System.out.println("手机杀毒");
			startActivity(new Intent(HomeActivity.this, AntvirusActivity.class));
			
			break;
		case 3:
			System.out.println("功能设置");
			startActivity(new Intent(HomeActivity.this, SettingActivity.class));
			break;

		default:
			break;
		}
	}

	// public void show(View v){
	// Dialog dialog = new Dialog(HomeActivity.this);
	// dialog.show();
	// }
	
	//当手指触摸了屏幕该方法就会执行
//	@Override
//	public boolean onTouchEvent(MotionEvent event) {
//		// TODO Auto-generated method stub
//		return super.onTouchEvent(event);
//	}
}
