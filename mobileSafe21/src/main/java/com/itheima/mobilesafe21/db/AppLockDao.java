package com.itheima.mobilesafe21.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AppLockDao {

	private AppLockOpenHelper appLockOpenHelper;

	public AppLockDao(Context context) {
		appLockOpenHelper = new AppLockOpenHelper(context);
	}

	// 添加
	public boolean insert(String packageName) {
		SQLiteDatabase database = appLockOpenHelper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(AppLockConstant.COLUMN_PACKAGE_NAME, packageName);
		long insert = database.insert(AppLockConstant.TABLE_NAME, null, values);

		database.close();
		// if (insert == -1) {
		// return false;
		// }else{
		// return true;
		// }

		return insert != -1;
	}

	// 删除
	public boolean delete(String packageName) {
		// delete from applock where packageName = 'com.itheima.xx'
		SQLiteDatabase database = appLockOpenHelper.getWritableDatabase();
		int delete = database.delete(AppLockConstant.TABLE_NAME,
				AppLockConstant.COLUMN_PACKAGE_NAME + " = ?",
				new String[] { packageName });
		database.close();
		return delete != 0;

	}

	// 查询
	public boolean query(String packageName) {
		// select packageName from applock where packageName = 'com.itheima.xx'
		SQLiteDatabase database = appLockOpenHelper.getWritableDatabase();
		String[] columns = new String[] { AppLockConstant.COLUMN_PACKAGE_NAME };
		String selection = AppLockConstant.COLUMN_PACKAGE_NAME + " = ?";
		String[] selectionArgs = new String[] { packageName };
		Cursor cursor = database.query(AppLockConstant.TABLE_NAME, columns,
				selection, selectionArgs, null, null, null);
		boolean result = false;
		if (cursor != null && cursor.moveToNext()) {
			result = true;
		}

		cursor.close();
		database.close();
		return result;
	}

	// 查询所有的加锁包名
	public List<String> queryAll() {
		// select packageName from applock 
		SQLiteDatabase database = appLockOpenHelper.getWritableDatabase();
		String[] columns = new String[] { AppLockConstant.COLUMN_PACKAGE_NAME };
		Cursor cursor = database.query(AppLockConstant.TABLE_NAME, columns,
				null, null, null, null, null);
		List<String> datas = new ArrayList<String>();
		while (cursor.moveToNext()) {
			String packageName = cursor.getString(0);
			datas.add(packageName);
		}

		cursor.close();
		database.close();
		return datas;
	}

}
