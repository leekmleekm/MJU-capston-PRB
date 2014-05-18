package com.remembook.main;

import java.io.File;
import java.util.ArrayList;

import com.remembook.R;
import com.remembook.database.MySQLiteHandler;
import com.remembook.search.*;
import com.remembook.imageview.*;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;


public class MainActivity extends Activity implements OnClickListener, OnItemClickListener, OnItemLongClickListener {
	
	ListView list;
	Cursor cursor;
	ArrayList<BookData> data;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		Button button_search = (Button)findViewById(R.id.main_button_search);
		MySQLiteHandler handler = MySQLiteHandler.open(getApplicationContext());

		list = (ListView)findViewById(R.id.main_list);
		cursor = handler.select();
		
		list.setOnItemClickListener(this);
		list.setOnItemLongClickListener(this);;
		button_search.setOnClickListener(this);
		
		if(cursor.getCount() > 0 ){ //db에 책이 하나 이상 있을 경우 전부 읽어옴
			DBcursor dbAdapter = new DBcursor(this, cursor);
			list.setAdapter(dbAdapter);
	    }
		else if(cursor.getCount() == 0){ //db에 책이 하나도 없을 경우
			Toast.makeText(getApplicationContext(), "책이 없습니다.\n책을 추가하세요.", Toast.LENGTH_LONG).show();
		}
	}

	public void onClick(View v){
    	switch (v.getId()){
    		case R.id.main_button_search: //책 검색 버튼을 누르면 searchMain 인텐트로 이동
    			Intent intent_search = new Intent(this, SearchMain.class);
    			startActivity(intent_search);
    			break;
    	}
    }
    
    public void onItemClick(AdapterView<?> parent, View v, int position, long id){
    	Intent intent = new Intent(this, GridViewActivity.class);
		intent.putExtra("path", cursor.getString(cursor.getColumnIndex("_title"))); //책 title명 gridviewactivity에게 보냄
		startActivity(intent);
    }
    
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id){
    	MySQLiteHandler handler = MySQLiteHandler.open(getApplicationContext());
    	
    	handler.delete(cursor.getString(cursor.getColumnIndex("_title"))); //db 데이터 삭제
    	handler.close(); //db 사용 끝을 명시
    	//폴더 삭제
    	File folder = new File(Environment.getExternalStorageDirectory() + "/DCIM/RememBook/"
    											+ cursor.getString(cursor.getColumnIndex("_title")));
		
	    File[] childFileList = folder.listFiles();
	    for(File childFile : childFileList) {
	    	if(childFile.isDirectory()) {
	    		//하위 디렉토리 루프 
	         }
	         else {
	        	 childFile.delete(); //하위 파일삭제
	         }
	    }      
	    folder.delete(); //root 삭제 
    	
    	Toast.makeText(getApplicationContext(), "삭제하였습니다.", Toast.LENGTH_SHORT).show();
    	cursor.requery(); //리스트 갱신

		return true;
	}

}
