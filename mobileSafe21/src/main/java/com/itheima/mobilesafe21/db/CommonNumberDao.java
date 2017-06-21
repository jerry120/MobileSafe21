package com.itheima.mobilesafe21.db;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class CommonNumberDao {

	// 查询组的数量
	public static int getGroupCount(Context context) {
		File dbFile = new File(context.getFilesDir(), "commonnum.db");
		SQLiteDatabase database = SQLiteDatabase.openDatabase(
				dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
		String sql = "select count(*) from classlist";
		int groupcount = 0;
		Cursor cursor = database.rawQuery(sql, null);
		if (cursor != null && cursor.moveToNext()) {
			groupcount = cursor.getInt(0);
		}

		cursor.close();
		database.close();
		return groupcount;
	}

	// 查询某一个组的子条目数量
	public static int getChildrenCount(int groupPosition, Context context) {
		File dbFile = new File(context.getFilesDir(), "commonnum.db");
		SQLiteDatabase database = SQLiteDatabase.openDatabase(
				dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);

		String sql = "select count(*) from table"
				+ String.valueOf(groupPosition + 1);
		Cursor cursor = database.rawQuery(sql, null);
		int childrencount = 0;
		if (cursor != null && cursor.moveToNext()) {
			childrencount = cursor.getInt(0);
		}

		cursor.close();
		database.close();
		return childrencount;
	}

	// 用来查询某一个组的文本
	public static String getGroupText(int groupPosition, Context context) {
		File dbFile = new File(context.getFilesDir(), "commonnum.db");
		SQLiteDatabase database = SQLiteDatabase.openDatabase(
				dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
		String sql = "select name from classlist where idx = ?";
		String[] selectionArgs = new String[] { String
				.valueOf(groupPosition + 1) };
		Cursor cursor = database.rawQuery(sql, selectionArgs);
		String grouptext = "";
		if (cursor != null && cursor.moveToNext()) {
			grouptext = cursor.getString(0);
		}

		cursor.close();
		database.close();
		return grouptext;
	}

	//查询子条目的文本
	public static String[] getChildText(int groupPosition, int childPosition,
			Context context) {
		File dbFile = new File(context.getFilesDir(), "commonnum.db");
		SQLiteDatabase database = SQLiteDatabase.openDatabase(
				dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
		
		String sql = "select name , number from table"+String.valueOf(groupPosition+1)+" where _id = ?";
		String[] selectionArgs = new String []{String.valueOf(childPosition+1)};
		Cursor cursor = database.rawQuery(sql , selectionArgs );
		String [] data = new String[2];
		if (cursor != null && cursor.moveToNext()) {
			String childrenText = cursor.getString(0);
			String number = cursor.getString(1);
			
			data[0] = childrenText;
			data[1] = number;
		}

		cursor.close();
		database.close();
		return data;
	}

}
