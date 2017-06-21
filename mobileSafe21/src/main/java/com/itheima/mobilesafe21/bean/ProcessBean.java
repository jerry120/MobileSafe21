package com.itheima.mobilesafe21.bean;

import android.graphics.drawable.Drawable;

public class ProcessBean {

	public Drawable appIcon;
	public String appName;
	public long appMemory;
	public boolean isSystem;//用来区分用户进程与系统进程
	
	public boolean isChecked = false;//用来记录当前条目是否被勾选
	
	public String packageName;
}
