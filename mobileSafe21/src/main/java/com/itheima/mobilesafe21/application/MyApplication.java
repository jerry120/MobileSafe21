package com.itheima.mobilesafe21.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintStream;
import java.lang.Thread.UncaughtExceptionHandler;

import android.app.Application;
import android.os.Environment;
import android.os.Process;

public class MyApplication extends Application {
	
	public String data ="heima21";
	
	//MyApplication是优先于四大组件被创建出来的第一个上下文，它是贯穿整个应用的生命周期,Application是全局唯一
	
	/**
	 * 用来创建全局的成员变量与成员方法
	 */
	@Override
	public void onCreate() {
		super.onCreate();
		System.out.println("MyApplication被创建");
		//设置一些sdk的初始化操作(三方分享，百度地方，推送)
		
		//设置一个未捕获异常
		Thread.setDefaultUncaughtExceptionHandler(new MyHandler());
		
	}
	
	private class MyHandler implements UncaughtExceptionHandler{

		//只要应用出现未捕获异常时，该方法执行
		@Override
		public void uncaughtException(Thread thread, Throwable ex) {
			//3.当应用打开时，先判断是否有错误信息如果有直接上传给服务器
			
			//2.记录错误信息
			File file = new File(Environment.getExternalStorageDirectory(),"error21.txt");
			PrintStream err;
			try {
				err = new PrintStream(file);
				ex.printStackTrace(err);
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			//1.直接关闭掉应用，闪退，不要让用户看到错误信息
			Process.killProcess(Process.myPid());
		}
		
	}
	
	
	public void doSomething(){
		System.out.println("doSomething");
	}

}
