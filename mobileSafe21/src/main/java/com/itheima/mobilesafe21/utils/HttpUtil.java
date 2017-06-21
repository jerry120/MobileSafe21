package com.itheima.mobilesafe21.utils;

import java.io.IOException;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class HttpUtil {
	
	public static Response httpGet(String url) throws IOException{
		  // 01. 定义okhttp
	    OkHttpClient okHttpClient_get = new OkHttpClient();
	    // 02.请求体
	    Request request = new Request.Builder()
	        .get()//get请求方式
	        .url(url)//网址
	        .build();
	        
	    // 03.执行okhttp
	    
	    Response response = okHttpClient_get.newCall(request).execute();
	    // 打印数据
	    //一次请求，一次响应
//	    System.out.println(response.body().string());response.body().byteStream()(这2个方法一次请求只能被调用一次)
	    
		return response;
	}

}
