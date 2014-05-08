package com.remembook.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class MySQLiteOpenHelper extends SQLiteOpenHelper {
	 // 데이터베이스 생성
	 public MySQLiteOpenHelper(Context context, String name, CursorFactory factory, int version) {
	  super(context, name, factory, version);
	 }
	 // 테이블 생성
	 public void onCreate(SQLiteDatabase db) {
	  Log.i("xxx", "onCreate >>>>>>>>>>>>>>>.....");
	   
	  String sql = "create table BookList ( " +
	          " _id integer primary key autoincrement, " +
	          "_image text, " +
	          "_title text, " +
	          "_author text, " +
	          "_publisher text, " +
	          "UNIQUE(_title))";
	        db.execSQL(sql);
	 }
	 // 테이블 삭제
	 public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		 Log.i("xxx", "onUpgrade >>>>>>>>>>>>>>>.....");
		 
		 String sql = "drop table if exists BookList";
		 
		 db.execSQL(sql);
		 onCreate(db);
	 }
}

