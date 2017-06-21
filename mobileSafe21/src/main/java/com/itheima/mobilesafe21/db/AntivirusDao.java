package com.itheima.mobilesafe21.db;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class AntivirusDao {

	public static boolean queryAntivirus(Context context,String md5){
		//select md5 from datable where md5 = 'ed6ec67fb1d39dd9bbd105653400b086'
		File dbFile = new File(context.getFilesDir(),"antivirus.db");
		
		SQLiteDatabase database = SQLiteDatabase.openDatabase(dbFile.getAbsolutePath(), null, SQLiteDatabase.OPEN_READWRITE);
		Cursor cursor = database.rawQuery("select md5 from datable where md5 = ?", new String[]{md5});
		boolean result = false;
		if (cursor != null&&cursor.moveToNext()) {
			result = true;
		}
		cursor.close();
		database.close();
		return result;
	}

}
