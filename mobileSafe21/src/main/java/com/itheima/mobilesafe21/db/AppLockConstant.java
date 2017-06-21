package com.itheima.mobilesafe21.db;

public interface AppLockConstant {

	public String DB_NAME = "applock";
	public int DB_VERSION = 1;
	String TABLE_NAME = "applock";
	String COLUMN_PACKAGE_NAME = "packageName";
	String DB_SQL = "create table "+TABLE_NAME+" (_id integer primary key ,"+COLUMN_PACKAGE_NAME+" varchar(200))";
	
	
}
