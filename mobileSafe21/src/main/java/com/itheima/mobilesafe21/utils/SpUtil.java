package com.itheima.mobilesafe21.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class SpUtil {
	private static final String SP_NAME = "config";
	private static SharedPreferences sp;

	// 保存boolean
	public static void saveBoolean(Context context, String key, boolean value) {
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putBoolean(key, value).commit();
	}

	// 获取boolean

	public static boolean getBoolean(Context context, String key,
			boolean defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}

		return sp.getBoolean(key, defValue);
	}

	// 保存int
	public static void saveInt(Context context, String key, int value) {
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}
		sp.edit().putInt(key, value).commit();
	}

	// 获取int

	public static int getInt(Context context, String key, int defValue) {
		if (sp == null) {
			sp = context.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE);
		}

		return sp.getInt(key, defValue);
	}

}
