package com.itheima.mobilesafe21.view;

import android.content.Context;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.ViewDebug.ExportedProperty;
import android.widget.TextView;

public class MarqueeTextView extends TextView {

	//当创建带有样式的控件时，调用该构造方法
	public MarqueeTextView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	//在xml文件引用该控件时，调用该构造方法
	public MarqueeTextView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	//通过代码创建该控件时，调用该构造方法
	public MarqueeTextView(Context context) {
		super(context);
	}
	
	
	//判断当前控件是否获取了焦点
	@Override
	@ExportedProperty(category = "focus")
	public boolean isFocused() {
		// TODO Auto-generated method stub
		//欺骗系统，已经获取了焦点
		return true;
	}
	
	//当焦点被抢占时，该方法执行
	@Override
	protected void onFocusChanged(boolean focused, int direction,
			Rect previouslyFocusedRect) {
//		System.out.println("focused:"+focused);
		super.onFocusChanged(true, direction, previouslyFocusedRect);
	}
	
	
	//当窗口的焦点被抢占时，该方式执行
	@Override
	public void onWindowFocusChanged(boolean hasWindowFocus) {
		System.out.println("hasWindowFocus:"+hasWindowFocus);
		// TODO Auto-generated method stub
		super.onWindowFocusChanged(true);
	}

	
}
