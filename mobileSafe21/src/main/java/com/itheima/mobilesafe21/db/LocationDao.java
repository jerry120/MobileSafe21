package com.itheima.mobilesafe21.db;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

public class LocationDao {
	// 查询
	public static String queryLocation(Context context,String number) {
		File dbFile = new File(context.getFilesDir(), "address.db");
		
		//使用正则判断是否是手机号码如果不是按长度来区分
		//1 3-8 匹配一次 0-9 匹配9次
//		String reg = "^1[3-8]{1}$[0-9]{9}";
		String reg = "^1[3-8]{1}\\d{9}$";
		String location = "未知";
		if (number.matches(reg)) {//满足正则，当前是手机号码
			//获取本地已经存在的数据库对象
			/**
			 * 参数一：数据库文件的绝对路径
			 * 参数二：cursor工厂 null
			 * 参数三：标记 
			 */
			SQLiteDatabase database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
			
			String sql ="select cardtype from info where mobileprefix = ?";
			String prefix = number.substring(0, 7);
			
			String[] selectionArgs = new String[]{prefix};
			
			Cursor cursor = database.rawQuery(sql, selectionArgs);
			if (cursor != null&&cursor.moveToNext()) {
				location = cursor.getString(0);
			}
			
			cursor.close();
			database.close();
		}else{
			switch (number.length()) {
			case 3://紧急号码
				location = "紧急号码";
				break;
			case 4://模拟器
				location = "模拟器";
				
				break;
			case 5://服务号
				location = "服务号";
				
				break;
			case 7://本地座机
			case 8:
				location = "本地座机";
				
				break;
				
			case 10://带有区号的座机  3+7 3+8 4+7 4+8
			case 11:
			case 12:
				//select distinct city from info where area = '021'
				//先查询前3位，如果查不到再查前4位
				SQLiteDatabase database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
				String sql3 = "select distinct city from info where area = ?";
				String[] selectionArgs3 = new String []{number.substring(0, 3)};
				Cursor cursor3 = database.rawQuery(sql3, selectionArgs3);
				if (cursor3!= null&&cursor3.moveToNext()) {
					location= cursor3.getString(0);
				}
				cursor3.close();
				
				if (TextUtils.equals(location, "未知")) {
					String sql4 = "select distinct city from info where area = ?";
					String[] selectionArgs4 = new String []{number.substring(0, 4)};
					Cursor cursor4 = database.rawQuery(sql4, selectionArgs4);
					if (cursor4!= null&&cursor4.moveToNext()) {
						location= cursor4.getString(0);
					}
					cursor4.close();
				}
				database.close();
				
				break;

			default:
				break;
			}
		}
		
		
		
		return location;
	}

}
