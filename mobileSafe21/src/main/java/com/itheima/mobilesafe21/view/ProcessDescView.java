package com.itheima.mobilesafe21.view;

import com.itheima.mobilesafe21.R;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class ProcessDescView extends LinearLayout {

	private TextView tv_process_title;
	private TextView tv_process_left;
	private TextView tv_process_right;
	private ProgressBar pb_process;

	public ProcessDescView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
	}

	public ProcessDescView(Context context, AttributeSet attrs) {
		super(context, attrs);
		View view = View.inflate(context, R.layout.view_process_desc, null);
		this.addView(view);
		
		tv_process_title = (TextView) view.findViewById(R.id.tv_process_title);
		tv_process_left = (TextView) view.findViewById(R.id.tv_process_left);
		tv_process_right = (TextView) view.findViewById(R.id.tv_process_right);
		pb_process = (ProgressBar) view.findViewById(R.id.pb_process);
		
		pb_process.setMax(100);
	}

	public ProcessDescView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	
	//修改标题
	public void setTitle(String title){
		tv_process_title.setText(title);
	}
	//修改左边文字
	public void setLeftText(String text){
		tv_process_left.setText(text);
	}
	//修改右边文字
	public void setRightText(String text){
		tv_process_right.setText(text);
	}
	//动态设置进度值 
	/**
	 * 
	 * @param progress 当前最大值是100
	 */
	public void setProgress(int progress){
		pb_process.setProgress(progress);
	}
	

}
