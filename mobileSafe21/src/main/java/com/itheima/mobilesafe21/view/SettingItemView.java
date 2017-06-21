package com.itheima.mobilesafe21.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.itheima.mobilesafe21.R;

public class SettingItemView extends RelativeLayout {
	private TextView tv_title;
	private ImageView iv_switch;//开关图片
	
	private boolean flag = false;//用来记录开关的状态

	// 当创建带有样式的控件时，调用该构造方法
	public SettingItemView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	// 在xml文件引用该控件时，调用该构造方法
	public SettingItemView(Context context, AttributeSet attrs) {
		super(context, attrs);//super是将对应的属性值传递给了父控件
		// 通过第二个参数AttributeSet获取设置在xml中的属性值
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.SettingItemView);
		String title = a.getString(R.styleable.SettingItemView_title);

		// 方式一：返回的view是布局文件对应的view
		// View view = View.inflate(context, R.layout.view_settingitemview,
		// null);
		// this.addView(view);
		// 方式二：返回的view是this这个容器视图对象
		View view = View.inflate(context, R.layout.view_settingitemview, this);
		tv_title = (TextView) view.findViewById(R.id.tv_title);
		tv_title.setText(title);

		// 获取背景类型，并设置对应背景图片
		int bgType = a.getInt(R.styleable.SettingItemView_bgType, -1);
		
		if (bgType == -1) {
			throw new RuntimeException("必须设置bgType属性");
		}

		switch (bgType) {
		case 0:
			this.setBackgroundResource(R.drawable.selector_first_bg);
			break;
		case 1:

			this.setBackgroundResource(R.drawable.selector_middle_bg);
			break;
		case 2:

			this.setBackgroundResource(R.drawable.selector_last_bg);
			break;

		default:
			break;
		}
		
		iv_switch = (ImageView) view.findViewById(R.id.iv_switch);

		//获取enable属性的值设置开关是否可见
		boolean enable = a.getBoolean(R.styleable.SettingItemView_enable, true);
		iv_switch.setVisibility(enable?View.VISIBLE:View.INVISIBLE);
	}

	// 通过代码创建该控件时，调用该构造方法
	public SettingItemView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public void setTitle(String title) {
		tv_title.setText(title);
	}

	public void setToggle(boolean b) {
		if (b) {
			iv_switch.setImageResource(R.drawable.on);
		}else{
			iv_switch.setImageResource(R.drawable.off);
		}
		
		this.flag = b;
		
	}

	public boolean isToggle() {
		return flag;
	}

	public void toggle() {
		setToggle(!this.flag);
	}

}
