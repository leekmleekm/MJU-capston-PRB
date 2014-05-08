package com.remembook.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class MySQLiteHandler  {
	 MySQLiteOpenHelper helper;
	 SQLiteDatabase db;
	  
	 //초기화 작업
	 public MySQLiteHandler(Context context) {
	  helper = new MySQLiteOpenHelper(context, "BookList.sqlite", null, 1);
	 }
	  
	 //테이블 열기
	 public static MySQLiteHandler open(Context context) {
	  return new MySQLiteHandler(context);
	 }
	  
	 //테이블 닫기
	 public void close() {
	  db.close();
	 }
	  
	 //저장
	 public void insert(String image, String title, String author, String publisher) {
	  db = helper.getWritableDatabase();
	  ContentValues values = new ContentValues();
	        values.put("_image", image);
	        values.put("_title", title);
	        values.put("_author", author);
	        values.put("_publisher", publisher);
	        db.insert("BookList", null, values);
	 }
	  
	 //수정
	 public void update(String image, String title, String author, String publisher) {
		 db = helper.getWritableDatabase();
		 ContentValues values = new ContentValues();
		 db.update("BookList", values, "_title=?", new String[]{title});
	 }
	 
	 //삭제
	 public void delete(String title) {
		 db = helper.getWritableDatabase();
		 db.delete("BookList", "_title=?", new String[]{title});
	 }
	  
	 //검색
	 public Cursor select() {
		 db = helper.getReadableDatabase();
		 Cursor c = db.query("BookList", null, null, null, null, null, null);
		 return c;
	 }
}

