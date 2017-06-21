package com.itheima.mobilesafe21.view;

import com.itheima.mobilesafe21.R;
import com.itheima.mobilesafe21.utils.Constant;
import com.itheima.mobilesafe21.utils.SpUtil;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class LocationToast implements OnTouchListener {
	
	private View mView;
	private WindowManager mWM;
	private WindowManager.LayoutParams mParams;
	private Context mContext;
	private int startX;
	private int startY;
	
	LinearLayout dd;
	RelativeLayout das;

	public LocationToast(Context context) {
		this.mContext = context;
		
		// 窗口管理器 添加，删除，修改窗口
		// 窗口：是android中最顶层元素，view一定是基于window才能显示，activity、dialog、toast 都是window

		mWM = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	
		//1.在xml中部分属性是以layout_开头，这些属性是控件所决定不了，必须与父控件进行“商量”
		//2.在xml中部分属性是不以layout_开头，这些属性是控件所能够决定的，不需要与父控件进行“商量”
		//3.在xml中部分属性是不以layout_开头属性可以直接通过控件的set等方法直接修改
		//4.在xml中部分属性是以layout_开头,需要通过布局参数（LayoutParams）来设置
		//5.在创建布局参数（LayoutParams）时，必须是当前控件的父容器类型的布局参数
		
//		LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(8, 8);
//		RelativeLayout.LayoutParams params1 = new RelativeLayout.LayoutParams(8, 8);
		
//		layoutParams.setMargins(left, top, right, bottom)
//		layoutParams.leftMargin = 5;
//		layoutParams.rightMargin = 7;
//		TextView tv = new TextView(mContext);
//		tv.setText(text);
//		tv.setTextColor(colors);
//		tv.setLayoutParams(layoutParams);
		
		

		mParams = new WindowManager.LayoutParams();
		mParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
		mParams.flags = 
				WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE// 设置不可获取焦点
//				| WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE// 设置不可触摸
				|
				WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON;// 设置始终亮屏
		mParams.format = PixelFormat.TRANSLUCENT;
		mParams.type = WindowManager.LayoutParams.TYPE_PRIORITY_PHONE;// 设置窗口类型(注意权限)
		mParams.setTitle("Toast");
	}



	// 显示归属地
	public void showLocationToast(String location) {
		
		mView = View.inflate(mContext, R.layout.view_location, null);

		TextView tv = (TextView) mView.findViewById(R.id.tv_location);
		tv.setText(location);
		//给view设置触摸监听
		mView.setOnTouchListener(this);
		
		//动态将记录的颜色设置给归属地即可
		int bgId = SpUtil.getInt(mContext, Constant.KEY_LOCATION_STYLE_COLOR, R.drawable.shape_location_bg);
		mView.setBackgroundResource(bgId);

		// 创建出一个视图为mView的窗口并添加给窗口管理
		mWM.addView(mView, mParams);
	}

	// 隐藏归属地
	public void hideLocationToast() {
		if (mView != null) {
			// note: checking parent() just to make sure the view has
			// been added... i have seen cases where we get here when
			// the view isn't yet added, so let's try not to crash.
			if (mView.getParent() != null) {
				mWM.removeView(mView);
			}

			mView = null;
		}
	}



	//mView被触摸后，该方法就会执行
	/**
	 * v:被触摸的view
	 * event：触摸事件
	 */
	@Override
	public boolean onTouch(View v, MotionEvent event) {
		System.out.println("onTouch");
		int action = event.getAction();
		System.out.println("action:----"+action);
		//1次按下n次移动，1次抬起，0,2，1
		switch (action) {
		case MotionEvent.ACTION_DOWN:
			//1.记录起始点
			startX = (int) event.getRawX();
			startY = (int) event.getRawY();
			
			break;
		case MotionEvent.ACTION_MOVE:
			//2.记录结束点
			int endX = (int) event.getRawX();//屏幕的单位是像素，只有整数个像素点
			int endY = (int) event.getRawY();
			//3.计算出间距
			int diffX = endX - startX;
			int diffY = endY - startY;
			
			//4.让view进行相应间距的位移(view是基于窗口的，所以要修改窗口的位置即可)
			mParams.x += diffX;
			mParams.y += diffY;
			
			mWM.updateViewLayout(mView, mParams);
			//5.初始化起始点，为下一次移动做准备
			startX = endX;
			startY = endY;
			
			break;
		case MotionEvent.ACTION_UP:
			
			break;

		default:
			break;
		}
		
		
		return true;//自己处理当前触摸事件（消费）
	}
	
}
